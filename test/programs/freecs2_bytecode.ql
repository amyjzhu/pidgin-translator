// Find receivers of the sendMessage call
let findReceiver(G,sendMessage) = G.backwardSlice(sendMessage and G.forExpression("formal-0"),1).selectNodes(LOCAL) in

// Outputs are messages
let sendMessage = pdg.forExpression(".sendMessage").selectNodes(FORMAL_ASSIGNMENT) in
let sendMessage = pdg.findReceiver(sendMessage) in

////////////////// Investigate punishments
let isPunished = pdg.returnsOf("isPunished") in
// nodes where the isPunished check is false
let mayBePunished = pdg.removeGuardedBy(isPunished, FALSE) in

// Error messages back to the sender can still be sent
let errorMessage = sendMessage.forExpression(".sender") in


// These 20 commands can be sent by punished users... domain specific whether these are actually ok
let allowed =     
                  /////// Actually the group has to allow banned users to join, but use this simplification for now
                  pdg.forProcedure("CmdAck.execute") OR
                  pdg.forProcedure("CmdJoin.execute") OR
                  pdg.forProcedure("CmdJoinClosed.execute") OR
                  /////// 
                  pdg.forProcedure("CmdListVips.execute") OR
                  pdg.forProcedure("AbstractCommand.sendPrivateMessage") OR
                  pdg.forProcedure("CmdRightChange.execute") OR
                  // Users are allowed to change groups
                  pdg.forProcedure("User.setGroup") OR
                  // And remove themselves
                  pdg.forProcedure("User.removeNow") OR
                  // Even punished users can quit
                  pdg.forProcedure("sendQuitMessage") 
in

// If we are in another method called sendMessage then we already covered it
let inAnotherSendMessage =  pdg.forProcedure(".sendMessage") in

// Handle system messages specially, sending one requires a special privilege
let sys = pdg.forProcedure("CmdSys.execute") in
let hasRight = pdg.forProcedure("CmdSys.execute").forExpression("hasRight").forExpression("NT_RET").selectNodes(EXIT_ASSIGNMENT) in
let maybeNotGod = pdg.removeGuardedBy(hasRight,TRUE) in
let badSysMessages = maybeNotGod AND sys.findReceiver(sendMessage) AND sys.removeNodes(errorMessage) /*EMPTY*/ in

let nodesToRemove = errorMessage OR allowed OR inAnotherSendMessage OR sys in

///////////// Some functions are disallowed but still safe since they are never called from in a bad context. 
///////////// Check the rest of the places messages are sent to make sure they have already been taken care of
///////////// then remove them from the unacceptable set
let removeUser = pdg.forExpression("Group.removeUser").selectNodes(FORMAL_ASSIGNMENT) in
let badRemoveUser = removeUser and mayBePunished.removeNodes(nodesToRemove) /*EMPTY*/ in
let nodesToRemove = nodesToRemove OR pdg.forProcedure("Group.removeUser") in

let sendModeratedMessage = pdg.forExpression("Group.sendModeratedMessage").selectNodes(FORMAL_ASSIGNMENT) in
let badModMessage = sendModeratedMessage and mayBePunished.removeNodes(nodesToRemove) /*EMPTY*/ in
let nodesToRemove = nodesToRemove OR pdg.forProcedure("Group.sendModeratedMessage") in

let sendMsg = pdg.forExpression("Group.sendMsg").selectNodes(FORMAL_ASSIGNMENT)  in
let badSendMsg = sendMsg and mayBePunished.removeNodes(nodesToRemove) in
let nodesToRemove = nodesToRemove OR pdg.forProcedure("Group.sendMsg") in

sendMessage and mayBePunished.removeNodes(nodesToRemove) OR badSysMessages OR badSendMsg OR badModMessage OR badRemoveUser is empty

