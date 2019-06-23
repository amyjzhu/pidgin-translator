package test.unit;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import junit.framework.TestCase;
import accrue.pdg.PDGEdge;
import accrue.pdg.PDGEdgeType;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.graph.PDGFactory;
import accrue.pdg.node.AbstractPDGNode;
import accrue.pdg.node.PDGNodeType;

/**
 * Test the node and edge indices internal to a {@link ProgramDependenceGraph}
 */
public class TestPDGIndices extends TestCase {

    /**
     * PDG created for the tests
     */
    ProgramDependenceGraph pdg;
    /**
     * node in the PDG
     */
    AbstractPDGNode n1 = Helper.createPCMerge("n1");
    /**
     * node in the PDG
     */
    AbstractPDGNode n2 = Helper.createNonPCMerge("n2");
    /**
     * node in the PDG
     */
    AbstractPDGNode n3 = Helper.createPCMerge("n3");
    /**
     * edge in the PDG
     */
    PDGEdge e1 = PDGFactory.edge(n1, n2, PDGEdgeType.CONJUNCTION, null);
    /**
     * edge in the PDG
     */
    PDGEdge e2 = PDGFactory.edge(n2, n3, PDGEdgeType.MERGE, null);

    /**
     * Init the PDG
     */
    public void setUp() {
        AbstractPDGNode n4 = Helper.createNonPCMerge("n4");
        AbstractPDGNode n5 = Helper.createPCMerge("n5");

        PDGEdge e3 = PDGFactory.edge(n3, n4, PDGEdgeType.MERGE, null);
        PDGEdge e4 = PDGFactory.edge(n3, n5, PDGEdgeType.CONJUNCTION, null);
        PDGEdge e5 = PDGFactory.edge(n5, n3, PDGEdgeType.MERGE, null);

        Set<PDGEdge> edges = new LinkedHashSet<PDGEdge>();
        edges.add(e1);
        edges.add(e2);
        edges.add(e3);
        edges.add(e4);
        edges.add(e5);

        pdg = PDGFactory.graphFromEdges(edges);
    }

    /**
     * Make sure the right number of edges are in the PDG for each type
     */
    public void testEdgeTypeIndex() {
        assertEquals("wrong number 1", 2, pdg.getEdgesOfType(PDGEdgeType.CONJUNCTION).size());
        assertEquals("wrong number 2", 3, pdg.getEdgesOfType(PDGEdgeType.MERGE).size());
        assertEquals("wrong number 3", 0, pdg.getEdgesOfType(PDGEdgeType.EXP).size());
        pdg = PDGFactory.removeEdge(pdg, e1);
        assertEquals("wrong number 4", 1, pdg.getEdgesOfType(PDGEdgeType.CONJUNCTION).size());
    }

    /**
     * Make sure the right number of edges are in the PDG for each type
     */
    public void testEdgeTypeIndex2() {
        assertEquals("wrong number 1", 2, pdg.getEdgesOfType(PDGEdgeType.CONJUNCTION).size());
        assertEquals("wrong number 2", 3, pdg.getEdgesOfType(PDGEdgeType.MERGE).size());
        assertEquals("wrong number 3", 0, pdg.getEdgesOfType(PDGEdgeType.EXP).size());
        pdg = PDGFactory.removeEdge(pdg, e1);
        assertEquals("wrong number 4", 1, pdg.getEdgesOfType(PDGEdgeType.CONJUNCTION).size());

        pdg = PDGFactory.removeEdge(pdg, e2);
        assertEquals("wrong number 5", 2, pdg.getEdgesOfType(PDGEdgeType.MERGE).size());

        pdg = PDGFactory.union(pdg, PDGFactory.graphFromEdges(Collections.singleton(e2)));
        assertEquals("wrong number 6", 3, pdg.getEdgesOfType(PDGEdgeType.MERGE).size());

        pdg = PDGFactory.union(pdg, PDGFactory.graphFromEdges(Collections.singleton(e1)));
        assertEquals("wrong number 7", 2, pdg.getEdgesOfType(PDGEdgeType.CONJUNCTION).size());
    }

    /**
     * Make sure the right number of edges are in the PDG for each type
     */
    public void testEdgeTypeIndex3() {
        AbstractPDGNode n6 = Helper.createNonPCMerge("n6");
        AbstractPDGNode n7 = Helper.createPCMerge("n7");
        PDGEdge e6 = PDGFactory.edge(n6, n7, PDGEdgeType.CONJUNCTION, null);
        PDGEdge e7 = PDGFactory.edge(n7, n6, PDGEdgeType.EXP, null);
        Set<PDGEdge> edges = new LinkedHashSet<PDGEdge>();
        edges.add(e6);
        edges.add(e7);
        pdg = PDGFactory.union(pdg, PDGFactory.graphFromEdges(edges));
        assertEquals("wrong number 8", 3, pdg.getEdgesOfType(PDGEdgeType.CONJUNCTION).size());
        assertEquals("wrong number 9", 3, pdg.getEdgesOfType(PDGEdgeType.MERGE).size());
        assertEquals("wrong number 10", 1, pdg.getEdgesOfType(PDGEdgeType.EXP).size());
        assertEquals("wrong number 11", 0, pdg.getEdgesOfType(PDGEdgeType.COPY).size());
    }

    /**
     * Make sure the right number of edges are in the PDG for each type
     */
    // public void testEdgeTypeIndex4() {
    // Set<PDGEdge> edges = new LinkedHashSet<PDGEdge>();
    // edges.add(e1);
    // edges.add(e2);
    // pdg.removeAllEdges(edges);
    // assertEquals("wrong number 1", 1,
    // pdg.getEdgesOfType(PDGEdgeType.CONJUNCTION).size());
    // assertEquals("wrong number 2", 2,
    // pdg.getEdgesOfType(PDGEdgeType.MERGE).size());
    // }

    /**
     * Make sure the right number of nodes are in the PDG for each type
     */
    public void testNodeTypeIndex() {
        assertEquals("wrong number 1", 3, pdg.getNodeOfType(PDGNodeType.PC_MERGE).size());
        assertEquals("wrong number 2", 2, pdg.getNodeOfType(PDGNodeType.ABSTRACT_LOCATION).size());
        assertEquals("wrong number 3", 0, pdg.getNodeOfType(PDGNodeType.LOCAL).size());

        pdg = PDGFactory.removeNodes(pdg, Collections.singleton(n1));
        assertEquals("wrong number 4", 2, pdg.getNodeOfType(PDGNodeType.PC_MERGE).size());
        assertEquals("wrong number 5", 1, pdg.getEdgesOfType(PDGEdgeType.CONJUNCTION).size());
    }

    /**
     * Make sure the right number of nodes are in the PDG for each type
     */
    public void testNodeTypeIndex2() {
        Set<AbstractPDGNode> nodes = new LinkedHashSet<AbstractPDGNode>();
        nodes.add(n1);
        nodes.add(n2);

        pdg = PDGFactory.removeNodes(pdg, nodes);
        assertEquals("wrong number 1", 2, pdg.getNodeOfType(PDGNodeType.PC_MERGE).size());
        assertEquals("wrong number 2", 1, pdg.getNodeOfType(PDGNodeType.ABSTRACT_LOCATION).size());
        assertEquals("wrong number 3", 1, pdg.getEdgesOfType(PDGEdgeType.CONJUNCTION).size());
        assertEquals("wrong number 4", 2, pdg.getEdgesOfType(PDGEdgeType.MERGE).size());
    }

    /**
     * Make sure the right number of nodes are in the PDG for each type
     */
    public void testNodeTypeIndex3() {
        AbstractPDGNode n6 = Helper.createNonPCMerge("n6");

        pdg = PDGFactory.union(pdg, PDGFactory.graph(Collections.singleton(n6), Collections.<PDGEdge> emptySet()));
        assertEquals("wrong number 1", 3, pdg.getNodeOfType(PDGNodeType.PC_MERGE).size());
        assertEquals("wrong number 2", 3, pdg.getNodeOfType(PDGNodeType.ABSTRACT_LOCATION).size());

        AbstractPDGNode n7 = Helper.createPCMerge("n7");
        AbstractPDGNode n8 = Helper.createNonPCMerge("n8");
        Set<AbstractPDGNode> nodes = new LinkedHashSet<AbstractPDGNode>();
        nodes.add(n7);
        nodes.add(n8);
        pdg = PDGFactory.union(pdg, PDGFactory.graph(nodes, Collections.<PDGEdge> emptySet()));

        assertEquals("wrong number 1", 4, pdg.getNodeOfType(PDGNodeType.PC_MERGE).size());
        assertEquals("wrong number 2", 4, pdg.getNodeOfType(PDGNodeType.ABSTRACT_LOCATION).size());
    }

    /**
     * Check whether IDs make it into the index
     */
    public void testIDIndex() {
        assertEquals("wrong node 1", n1, pdg.getNodeById(n1.getNodeId()));
        assertEquals("wrong node 2", n2, pdg.getNodeById(n2.getNodeId()));
        assertEquals("wrong node 3", n3, pdg.getNodeById(n3.getNodeId()));
        assertNull("was supposed to be null 1", pdg.getNodeById(123456));

        pdg = PDGFactory.removeNodes(pdg, Collections.singleton(n1));
        assertNull("was supposed to be null 2", pdg.getNodeById(n1.getNodeId()));
    }

    /**
     * Check whether IDs make it into the index
     */
    public void testIDIndex2() {
        Set<AbstractPDGNode> nodes = new LinkedHashSet<AbstractPDGNode>();
        nodes.add(n1);
        nodes.add(n2);

        pdg = PDGFactory.removeNodes(pdg, nodes);
        assertNull("was supposed to be null 1", pdg.getNodeById(n1.getNodeId()));
        assertNull("was supposed to be null 2", pdg.getNodeById(n2.getNodeId()));
    }

    /**
     * Make sure the right number of nodes are in the PDG for each type
     */
    public void testIDIndex3() {
        AbstractPDGNode n6 = Helper.createNonPCMerge("n6");

        pdg = PDGFactory.union(pdg, PDGFactory.graph(Collections.singleton(n6), Collections.<PDGEdge> emptySet()));
        assertEquals("wrong node 1", n6, pdg.getNodeById(n6.getNodeId()));

        AbstractPDGNode n7 = Helper.createPCMerge("n7");
        AbstractPDGNode n8 = Helper.createNonPCMerge("n8");
        Set<AbstractPDGNode> nodes = new LinkedHashSet<AbstractPDGNode>();
        nodes.add(n7);
        nodes.add(n8);

        pdg = PDGFactory.union(pdg, PDGFactory.graph(nodes, Collections.<PDGEdge> emptySet()));
        assertEquals("wrong node 2", n7, pdg.getNodeById(n7.getNodeId()));
        assertEquals("wrong node 3", n8, pdg.getNodeById(n8.getNodeId()));
    }
}
