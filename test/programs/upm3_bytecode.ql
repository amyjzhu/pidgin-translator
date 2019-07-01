let sources = pdg.returnsOf("askUserForPassword") in 
let sinks = pdg.formalsOf("javax.swing")
         OR pdg.formalsOf("Stream.print") 
         OR pdg.formalsOf("HTTPTransport") in

let exceptions = pdg.forExpression("EX_EXIT_PC") OR pdg.forExpression("EX-EXIT") OR pdg.forProcedure("Exception.<init>") in
let bouncycastle =
  pdg.forProcedure("org.bouncycastle.crypto.modes.CBCBlockCipher.init") OR
  pdg.forProcedure("org.bouncycastle.crypto.engines.AESEngine.encryptBlock") OR
  pdg.forProcedure("org.bouncycastle.crypto.engines.AESEngine.decryptBlock") in

let cryptofunctions = pdg.forProcedure("encrypt") OR pdg.forProcedure("decrypt") OR pdg.forProcedure("initCipher") in
let nullChecks = pdg.forExpression("!= null") OR pdg.forExpression("== null") in

pdg
.removeNodes(cryptofunctions)
.removeNodes(exceptions)
.removeNodes(nullChecks)
.between(sources,sinks) is empty