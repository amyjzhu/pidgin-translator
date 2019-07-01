// Only administrators can add class notices
let addNotice = pdg.entriesOf("addNotice") in
let isAdmin = pdg.returnsOf("isCMSAdmin")  in
pdg.removeGuardedBy(isAdmin,TRUE) and addNotice is empty