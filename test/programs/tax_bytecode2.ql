// Check that the salary is always encrypted before being written to disc
let salary = pdg.returnsOf("getSalary") in
let encryptFormals = pdg.formalsOf("encrypt") in
let outputs = pdg.formalsOf("writeToDataFile") in
//let emptyCheck(foo) = foo.forProcedure("") in // Returns all nodes, but fails on empty graph
//let assertExplicitFlow = emptyCheck(pdg.explicitGraph.between(salary,outputs)) in
pdg.removeNodes(encryptFormals).cfl(salary,outputs) is empty