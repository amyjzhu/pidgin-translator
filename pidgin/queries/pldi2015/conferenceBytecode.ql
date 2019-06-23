// File: queries/conferenceBytecode.ql

// Define outputs
let io = pdg.formalsOf("PrintStream.print") in
let client = pdg.returnsOf("RequestHandler.getReviews") OR
             pdg.returnsOf("RequestHandler.getAuthors") OR
             pdg.returnsOf("RequestHandler.getPaperText") OR
             pdg.returnsOf("RequestHandler.getPaperStatus") in
             
let output = io OR client in

// Define access checks
let isReviewerOf = pdg.returnsOf("isReviewerOf") in
let isReviewer = pdg.returnsOf("isReviewer") in
let isAuthor = pdg.returnsOf("isAuthor(") in
let isAuthorOf = pdg.returnsOf("isAuthorOf") in
let isPCMember = pdg.returnsOf("isPCMember") in
let isPCChair = pdg.returnsOf("isPCChair") in
let isAdmin = pdg.returnsOf("isAdmin") in
let subPast = pdg.returnsOf("submissionDeadlinePassed") in
let notifyPast = pdg.returnsOf("notificationDeadlinePassed") in
let cameraPast = pdg.returnsOf("cameraReadyDeadlinePassed") in
let isAccepted = pdg.returnsOf("getStatus") in
let hasConflict = pdg.returnsOf("hasConflict") in

// Only Authors can add papers before the deadline
let addPaper = pdg.entriesOf("PaperDatabase.addPaper") in
let policy1 = pdg.removeGuardedByBool(isAuthor && !subPast) and addPaper in
policy1 is empty