let openDB = pdg.callSites("doOpenDatabaseActions") in
let passwordCheck = pdg.forExpression("passwordCorrect").selectNodes(LOCAL) in
let dbLoadedCheck = pdg.forExpression("reloadedDb == null").selectNodes(OTHER_EXPRESSION) in
let notLoaded = pdg.removeGuardedBy(dbLoadedCheck, FALSE) in
let successfullyDecryptedDb = pdg.forProcedure("syncWithRemoteDatabase").selectNodes(LOCAL).forExpression("v79"/*successfullyDecryptedDb"*/) in
let notChecked = pdg.removeGuardedBy(passwordCheck OR successfullyDecryptedDb, TRUE) in

// POLICY: The data base is not opened unless the password is correct or an equivalent check
notChecked and notLoaded and openDB and pdg.removeNodes(pdg.forProcedure("newDatabase")) is empty