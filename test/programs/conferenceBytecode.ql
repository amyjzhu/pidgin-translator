// File: queries/conferenceBytecode.ql

// Define outputs
let io = pdg.formalsOf("PrintStream.print") in
let client = pdg.backwards("RequestHandler.getReviews") OR
             pdg.backwards("RequestHandler.getAuthors") OR
             pdg.backwards("RequestHandler.getPaperText") OR
             pdg.backwards("RequestHandler.getPaperStatus") in
             
let output = io OR client in

// Define access checks
let isReviewerOf = pdg.backwards("isReviewerOf") in
let isReviewer = pdg.backwards("isReviewer") in
let isAuthor = pdg.backwards("isAuthor(") in
let isAuthorOf = pdg.backwards("isAuthorOf") in
let isPCMember = pdg.backwards("isPCMember") in
let isPCChair = pdg.backwards("isPCChair") in
let isAdmin = pdg.backwards("isAdmin") in
let subPast = pdg.backwards("submissionDeadlinePassed") in
let notifyPast = pdg.backwards("notificationDeadlinePassed") in
let cameraPast = pdg.backwards("cameraReadyDeadlinePassed") in
let isAccepted = pdg.backwards("getStatus") in
let hasConflict = pdg.backwards("hasConflict") in

// Only Authors can add papers before the deadline
let addPaper = pdg.forwards("PaperDatabase.addPaper") in
let policy1 = pdg.removeGuardedByBool(isAuthor && !subPast) and addPaper in
policy1 is empty