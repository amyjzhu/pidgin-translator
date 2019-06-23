// Only appropriate users can add new Students to a class
let addStudents = pdg.entriesOf("addStudentsToCourse") in
let perm = pdg.returnsOf("hasStudentsPageAccess") 
        or pdg.returnsOf("isGradesPrivByAssignment") 
        or pdg.returnsOf("isAdminPrivByCourse") in
pdg.removeGuardedBy(perm,TRUE) and addStudents is empty