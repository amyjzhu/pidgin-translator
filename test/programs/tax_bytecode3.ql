// values returned by the userLogin function
let normalReturns = pdg.returnsOf("userLogin").forExpression("NORMAL-EXIT") in
let returnsOfCheck = pdg.predsOf(normalReturns) in
// nonnull values returned by the userLogin function
let nonnull = returnsOfCheck.removeNodes(pdg.forExpression("null")) in 
// Check for a valid password
let validPass = pdg.forExpression("validPass").selectNodes(LOCAL) in
// Remove nodes where the password is valid
let invalidPass = pdg.removeGuardedBy(validPass,TRUE) in
// Is the return always null if the password is invalid
let badReturns = invalidPass and nonnull in

// calls to the load method
let loadCalls = pdg.entriesOf("loadRecord") in
// check the username against null
let nullCheck = pdg.forExpression("username == null").selectNodes(OTHER_EXPRESSION) in
// places where the username could be null
let couldBeNull = pdg.removeGuardedBy(nullCheck,FALSE) in
// Are there any calls to loadRecord where the user name could be null?
let badLoadCalls = couldBeNull and loadCalls in
badLoadCalls AND badReturns is empty