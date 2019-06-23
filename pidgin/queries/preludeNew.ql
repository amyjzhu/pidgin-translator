// Graph with only PC nodes
let pcGraph(G) = 
     G.selectNodes(EXIT_PC_SUMMARY) 
  OR G.selectNodes(ENTRY_PC_SUMMARY) 
  OR G.selectNodes(BOOLEAN_TRUE_PC) 
  OR G.selectNodes(BOOLEAN_FALSE_PC) 
  OR G.selectNodes(PC_MERGE) 
  OR G.selectNodes(PC_OTHER) 
  OR G.selectNodes(EXIT_PC_JOIN) in

// TODO BROKEN, may remove too many
let predsOf(G, nodes) = G.backwardSlice(nodes,1).removeNodes(nodes) in

// PRIMITIVE EXPRESSION DEFINITION

let formalsOf(G,nnn) = (G.selectNodes(FORMAL_SUMMARY) OR G.selectNodes(THIS)).forProcedure(nnn) in

//let formalAssign(e) = pdg.backwardSlice(e.selectNodes(FORMAL_SUMMARY) or e.selectNodes(THIS),1).selectNodes(FORMAL_ASSIGNMENT) in
//let actualsOf(G,e) = G AND e.formalAssign in
let actualsOf(G,procedureName) = G.backwardSlice(G.formalsOf(procedureName),1).selectNodes(FORMAL_ASSIGNMENT) in

let directControlDeps(G,e) = G.backwardSlice(e,1).selectEdges(IMPLICIT).pcGraph in

let entriesOf(G,name) = G.selectNodes(ENTRY_PC_SUMMARY).forProcedure(name) in

// Should really be exits of
let returnsOf(G,name) = G.selectNodes(EXIT_SUMMARY).forProcedure(name).forExpression("NORM") in

let cfl(G, ins, outs) = G.restrict("CFLReachableRestrictor", ins, outs) in
let cflPath(G, ins, outs) = G.restrict("CFLPath", ins, outs) in

// USER DEFINED FUNCTIONS

//let allAssignedToActuals(e) = pdg.predsOf(e.formalAssign).removeNodes(pdg.pcGraph) in
//let assignedToActuals(G,e) = G AND allAssignedToActuals(e) in

let callSites(G, proc) = G.directControlDeps(pdg.actualsOf(proc)) in

// Get a graph containing any nodes on a path between a node in x and a node in y
let between(G,x,y) = G.forwardSlice(x).backwardSlice(y) in
  
let explicitGraph(G) = G.removeNodes(G.pcGraph) in

// TODO BROKEN, may remove too many
let succsOf(G, nodes) = G.forwardSlice(nodes,1).removeNodes(nodes) in

// Just a shortcut for FindChokePoints Restrictor
let findChokePoints(G,inputs,outputs) = G.restrict("FindChokePoints",inputs,outputs) in 

// TODO this is imprecise, but needs to be recursive to be precise
let findCopies(G,toCopy) =
  // everything that is copied into toCopy
  G.selectEdges(COPY).backwardSlice(toCopy) OR
  // everything that toCopy copies into
  G.selectEdges(COPY).forwardSlice(toCopy)
in

// check if everything in set2 is a copy of something in set1
let areCopies(G, set1, set2) =
set2.removeNodes(
  G.findCopies(G.findCopies(set1))
) 
in

let checkArgs(G, name, function1, argName1, function2 , argName2) = 
G.areCopies(
  G.forProcedure(name) AND pdg.actualsOf(pdg.formalsOf(function1)).forExpression(argName1), 
  G.forProcedure(name) AND pdg.actualsOf(pdg.formalsOf(function2)).forExpression(argName2)
) in

let clearCache(G) = G.restrict("ClearCache",G,G) in

// Get all the fields of a particular name
let fieldsNamed(G,classDotFieldName) = G.selectNodes(ABSTRACT_LOCATION).forExpression(classDotFieldName) in

// Exit nodes for normal return in the caller of a method
let normalExits(G,name) = G.selectNodes(EXIT_ASSIGNMENT).forExpression(name).forExpression("NT_RET") in