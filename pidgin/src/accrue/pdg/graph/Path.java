package accrue.pdg.graph;

import java.util.LinkedList;
import java.util.List;

import accrue.pdg.PDGEdge;
import accrue.pdg.PDGPath;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.node.AbstractPDGNode;

/**
 * A sequence of program dependence graph nodes giving a path in the PDG
 */
public class Path implements PDGPath {

    /**
     * Edges along the path the dest for edge i is the source for edge i+1
     */
    private List<PDGEdge> edges;

    /**
     * Graph this is a path within
     */
    private ProgramDependenceGraph pdg;

    /**
     * Create a path with the given edges
     * 
     * @param pdg
     *            PDG we want the path to be in
     * @param edges
     *            Edges along the path the dest for edge i must be the source
     *            for edge i+1
     */
    public Path(ProgramDependenceGraph pdg, List<PDGEdge> edges) {
        // Check that this is a valid path
        for (int i = 0; i < edges.size() - 1; i++) {
            if (!edges.get(i).getTarget().equals(edges.get(i + 1).getSource())) {
                throw new RuntimeException("Invalid path: source of " +
                        edges.get(i) +
                        " does not equal destination for " +
                        edges.get(i + 1));
            }
        }
        this.edges = edges;
        this.pdg = pdg;
    }

//    /**
//     * Get a {@link ProgramDependenceGraph} consisting of just this path and the
//     * nodes on it
//     * 
//     * @return a new PDG representation of the path
//     */
//    public ProgramDependenceGraph pathAsGraph() {
//        return PDGFactory.graphFromEdges(edges);
//    }

    @Override
    public String toString() {
        return edges.toString();
    }

    public List<PDGEdge> getEdgeList() {
        return edges;
    }

    public AbstractPDGNode getEndVertex() {
        return edges.get(edges.size() - 1).getTarget();
    }

    public ProgramDependenceGraph getGraph() {
        return pdg;
    }

    public AbstractPDGNode getStartVertex() {
        return edges.get(0).getSource();
    }

    /**
     * Get an ordered list of the nodes occurring on this path
     * 
     * @return List of nodes on the path
     */
    public List<AbstractPDGNode> getNodeList() {
        List<AbstractPDGNode> nodes = new LinkedList<AbstractPDGNode>();
        nodes.add(getStartVertex());
        for (PDGEdge e : edges) {
            nodes.add(e.getTarget());
        }
        return nodes;
    }
    
    @Override
    public boolean equals(Object other) {
    	if (!(other instanceof PDGPath)) {
    		return false;
    	}
    	PDGPath p = (PDGPath)other;
    	return p.getEdgeList().equals(this.getEdgeList());
    }
    
    @Override
    public int hashCode() {
    	return edges.hashCode();
    }
}