package test.unit;

import java.util.LinkedHashSet;
import java.util.Set;

import junit.framework.TestCase;
import accrue.algorithm.SlicingAlgorithms;
import accrue.pdg.PDGEdge;
import accrue.pdg.PDGEdgeType;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.graph.GraphUnnecessaryMerges;
import accrue.pdg.graph.PDGFactory;
import accrue.pdg.node.AbstractPDGNode;

/**
 * Test the replacement of merge edges with copy edges
 */
public class TestReplaceUnecessaryMerges extends TestCase {
    
    /**
     * Replace some merge edges and check that they appear in the translated graph
     */
    public void testReplaceMerges() {
        // Load the file and check its consistency
//        ProgramDependenceGraph pdg = Helper.testAndLoad("test.programs.Sanitize");
        ProgramDependenceGraph pdg = PDGFactory.graphFromJSONFile("tests/test.programs.Sanitize.json", false);
        // Load the same file but replace the singleton merges
        ProgramDependenceGraph pdg2 = PDGFactory.graphFromJSONFile("tests/test.programs.Sanitize.json", true);
        
        checkSizes(pdg, pdg2);
        
        Set<PDGEdge> edges = new LinkedHashSet<PDGEdge>(pdg.edgeSet());
        Set<PDGEdge> edges2 = new LinkedHashSet<PDGEdge>(pdg2.edgeSet());

        edges.removeAll(pdg2.edgeSet());
        edges2.removeAll(pdg.edgeSet());
        
        for (PDGEdge e : edges) {
            PDGEdge newEdge = null;
            if (e.getTarget().getNodeType().isPathCondition()) {
                newEdge = PDGFactory.edge(e.getSource(), e.getTarget(), PDGEdgeType.CONJUNCTION, null);
            } else {
                newEdge = PDGFactory.edge(e.getSource(), e.getTarget(), PDGEdgeType.COPY, null);
            }
            assertTrue("New edges did not have " + newEdge, edges2.remove(newEdge));
        }
        assertTrue("Some extra edge snuck into the new graph", edges2.isEmpty());
    }
    
    /**
     * Replace some merge edges and check that they appear in the translated graph
     */
    public void testReplaceMerges4() {
        // Load the file and check its consistency
        ProgramDependenceGraph pdg = Helper.testAndLoad("test.programs.Loop");
        // Load the same file but replace the singleton merges
        ProgramDependenceGraph pdg2 = PDGFactory.graphFromJSONFile("tests/test.programs.Loop.json", true);
        
        checkSizes(pdg, pdg2);
        
        Set<PDGEdge> edges = new LinkedHashSet<PDGEdge>(pdg.edgeSet());
        edges.removeAll(pdg2.edgeSet());
        
        Set<PDGEdge> edges2 = new LinkedHashSet<PDGEdge>(pdg2.edgeSet());
        edges2.removeAll(pdg.edgeSet());
        
        for (PDGEdge e : edges) {
            PDGEdge newCopy = PDGFactory.edge(e.getSource(), e.getTarget(), PDGEdgeType.COPY, null);
            PDGEdge newTrue = PDGFactory.edge(e.getSource(), e.getTarget(), PDGEdgeType.CONJUNCTION, null);
            assertTrue("New edges did not have " + newCopy + " or " + newTrue, edges2.remove(newCopy) || edges2.remove(newTrue));
        }
        assertTrue("Some extra edge snuck into the new graph", edges2.isEmpty());
    }
    
    /**
     * Replace some merge edges and check that they appear in the translated graph
     */
    public void testReplaceMerges3() {
        // Load the file
        ProgramDependenceGraph pdg = PDGFactory.graphFromJSONFile("tests/llvm.guess.json", false);
        // Load the same file but replace the singleton merges
        ProgramDependenceGraph pdg2 = PDGFactory.graphFromJSONFile("tests/llvm.guess.json", true);
        
        checkSizes(pdg, pdg2);
        
        Set<PDGEdge> edges = new LinkedHashSet<PDGEdge>(pdg.edgeSet());
        edges.removeAll(pdg2.edgeSet());
        
        Set<PDGEdge> edges2 = new LinkedHashSet<PDGEdge>(pdg2.edgeSet());
        edges2.removeAll(pdg.edgeSet());
        
        for (PDGEdge e : edges) {
            PDGEdge newCopy = PDGFactory.edge(e.getSource(), e.getTarget(), PDGEdgeType.COPY, null);
            PDGEdge newTrue = PDGFactory.edge(e.getSource(), e.getTarget(), PDGEdgeType.CONJUNCTION, null);
            assertTrue("New edges did not have " + newCopy + " or " + newTrue, edges2.remove(newCopy) || edges2.remove(newTrue));
        }
        assertTrue("Some extra edge snuck into the new graph", edges2.isEmpty());
    }
    
    /**
     * Create some graphs and test the algorithm
     */
    public void testReplaceMergesProgramatic() {
        AbstractPDGNode n1 = Helper.createPCMerge("n1");
        AbstractPDGNode n2 = Helper.createPCMerge("n2");
        AbstractPDGNode n3 = Helper.createPCMerge("n3");
        AbstractPDGNode n4 = Helper.createPCMerge("n4");
        AbstractPDGNode n5 = Helper.createNonPCMerge("n5");
        
        PDGEdge e1 = PDGFactory.edge(n1, n2, PDGEdgeType.MERGE, null);
        PDGEdge e2 = PDGFactory.edge(n2, n3, PDGEdgeType.MERGE, null);
        PDGEdge e3 = PDGFactory.edge(n4, n3, PDGEdgeType.MERGE, null);
        PDGEdge e4 = PDGFactory.edge(n4, n5, PDGEdgeType.MERGE, null);


        Set<PDGEdge> edges = new LinkedHashSet<PDGEdge>();
        edges.add(e1);
        edges.add(e2);
        edges.add(e3);
        edges.add(e4);
        
        ProgramDependenceGraph pdg = PDGFactory.graphFromEdges(edges);
        
        pdg = GraphUnnecessaryMerges.construct(pdg);
        
        assertTrue("e2 should still be there", pdg.containsEdge(e2));
        assertTrue("e3 should still be there", pdg.containsEdge(e3));
        
        PDGEdge newE = PDGFactory.edge(n1, n2, PDGEdgeType.CONJUNCTION, null);
        assertFalse("e1 not should still be there", pdg.containsEdge(e1));
        assertTrue("e should have been added", pdg.containsEdge(newE));
        
        PDGEdge newE2 = PDGFactory.edge(n4, n5, PDGEdgeType.COPY, null);
        assertFalse("e1 not should still be there", pdg.containsEdge(e4));
        assertTrue("e should have been added", pdg.containsEdge(newE2));
    }
    
    /**
     * Create some graphs and test the algorithm
     */
    public void testReplaceMergesProgramatic2() {
        AbstractPDGNode n1 = Helper.createNonPCMerge("n1");
        AbstractPDGNode n2 = Helper.createNonPCMerge("n2");
        AbstractPDGNode n3 = Helper.createPCMerge("n3");
        
        PDGEdge e1 = PDGFactory.edge(n1, n2, PDGEdgeType.MERGE, null);
        PDGEdge e2 = PDGFactory.edge(n3, n2, PDGEdgeType.IMPLICIT, null);


        Set<PDGEdge> edges = new LinkedHashSet<PDGEdge>();
        edges.add(e1);
        edges.add(e2);
        
        ProgramDependenceGraph pdg = PDGFactory.graphFromEdges(edges);
        
        pdg = GraphUnnecessaryMerges.construct(pdg);
        
        assertTrue("e2 should still be there", pdg.containsEdge(e2));
        
        PDGEdge newE = PDGFactory.edge(n1, n2, PDGEdgeType.COPY, null);
        assertFalse("e1 not should still be there", pdg.containsEdge(e1));
        assertTrue("e should have been added", pdg.containsEdge(newE));
    }
    
    private void checkSizes(ProgramDependenceGraph pdg1, ProgramDependenceGraph pdg2) {
        int acc1 = 0;
        int acc2 = 0;
        for (PDGEdgeType t : PDGEdgeType.values()) {
            if (t == PDGEdgeType.MERGE || t == PDGEdgeType.COPY || t == PDGEdgeType.CONJUNCTION) {
                acc1 += pdg1.getEdgesOfType(t).size();
                acc2 += pdg2.getEdgesOfType(t).size();
            } else {
                assertTrue("edges not the same for " + t, pdg1.getEdgesOfType(t).size() == pdg2.getEdgesOfType(t).size()); 
            }
        }
        assertTrue("MERGE + COPY + CONJUGATION should be invariant", acc1 == acc2);
    }
}
