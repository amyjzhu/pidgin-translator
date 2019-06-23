// Check that password only the hash of the password gets output
let passwords = pdg.forExpression("password =").selectNodes(LOCAL) in
let outputs = (pdg.forExpression("writeToFile") or pdg.forExpression("print")).selectNodes(FORMAL_ASSIGNMENT) in
let hashFormals = pdg.formalsOf("computeHash") in
pdg.removeNodes(hashFormals).between(passwords,outputs) is empty