// Outputs are messages

// find the local variable that is the receiver of the send message call
let findMessageState(G,sendMessage) = G.backwardSlice(sendMessage,2).selectNodes(LOCAL) and G.removeNodes(G.forExpression("=")) in
// find the assignments into the template field of the receiver of the sendMessage call (this says what kind of message it is)
let findTemplates(G,sendMessage) = G.forwardSlice(findMessageState(G,sendMessage), 1).forExpression("msgState.msgTemplate") in
// find templates that are error messages
let findErrors(G,sendMessage) = findTemplates(G,sendMessage).forExpression("error") in

let sys = pdg.forProcedure("CmdSys.execute") in
let sendMessage = sys.forExpression(".sendMessage")
                     .selectNodes(FORMAL_ASSIGNMENT) in

//////////////////// Sys messages are only available to GOD
// The god right check
let hasRight = pdg.forProcedure("CmdSys.execute").forExpression("hasRight").forExpression("NT_RET").selectNodes(EXIT_ASSIGNMENT) in
let maybeNotGod = pdg.removeGuardedBy(hasRight,TRUE) in
let uncheckedMessages = maybeNotGod and sys.findTemplates(sendMessage) in
//uncheckedMessages.removeNodes(sys.findErrors(sendMessage)) is empty

// Find receivers of the sendMessage call
let findReceiver(G,sendMessage) = G.backwardSlice(sendMessage and G.forExpression("formal-0"),1).selectNodes(LOCAL) in
// All unguarded messages are sent back to the sender
maybeNotGod AND sys.findReceiver(sendMessage) AND sys.removeNodes(sys.forExpression(".sender")) is empty