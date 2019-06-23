package test.unit;

import java.util.LinkedHashSet;
import java.util.Set;

import junit.framework.TestCase;
import test.programs.Sanitize;
import accrue.algorithm.SlicingAlgorithms;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.graph.PDGFactory;
import accrue.pdg.node.AbstractPDGNode;
import accrue.pdg.node.ExprNode;
import accrue.pdg.node.PDGNodeInterface;
import accrue.pdg.node.PDGNodeType;

/**
 * Test for {@link Sanitize}
 */
public class TestSanitize extends TestCase {

    /**
     * We want to find calls to the execute method, in particular we want to
     * know what flows into the formal parameter.
     */
    public void testFindExecute() {
        // Load the file and check its consistency
        ProgramDependenceGraph pdg = Helper.testAndLoad("test.programs.Sanitize");

        // Gather all the nodes that are formal arguments to the "execute"
        // function
        Set<AbstractPDGNode> argsToExecute = new LinkedHashSet<AbstractPDGNode>();
        for (AbstractPDGNode n : pdg.vertexSet()) {
            if (n instanceof ExprNode && n.getNodeType() ==  PDGNodeType.FORMAL_SUMMARY) {
                ExprNode p = (ExprNode) n;
                if (p.getProcedureName().contains("test.programs.Sanitize#execute")) {
                    argsToExecute.add(p);
                }              
            }
        }
        // Find all edges into the arguments to execute (at a particular depth)
        ProgramDependenceGraph subGraph = SlicingAlgorithms.backwardSlice(pdg, argsToExecute, 3);
        Set<PDGNodeInterface> args = new LinkedHashSet<PDGNodeInterface>(argsToExecute);
        PDGFactory.writeDotToFile(subGraph, "Sanitize-argsToExecute", "./out", false, true, 0.5, null, args);
    }
}
