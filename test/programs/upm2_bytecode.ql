let passwordInput = pdg.returnsOf("askUserForPassword") in
let output = pdg.formalsOf("javax.swing") or pdg.formalsOf("Stream.print") or pdg.formalsOf("HTTPTransport") or pdg.formalsOf("sun.swing") in
let bouncycastle =
  pdg.forProcedure("org.bouncycastle.crypto.engines.AESEngine.packBlock") OR
  pdg.forProcedure("org.bouncycastle.crypto.modes.CBCBlockCipher.decryptBlock") OR
  pdg.forProcedure("org.bouncycastle.crypto.engines.AESEngine.encryptBlock") OR
  pdg.forProcedure("org.bouncycastle.crypto.engines.AESEngine.decryptBlock") in
// POLICY: There are no (explicit) paths from the password to the GUI or console or network
// Except via bouncy castle encryption and decryption
pdg.explicitGraph
  .removeNodes(bouncycastle)
  .between(passwordInput, output) is empty
