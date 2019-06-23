package accrue.pdg;

import java.util.List;

import accrue.pdg.node.AbstractPDGNode;

/**
 * A sequence of program dependence graph nodes giving a path in the PDG
 */
public interface PDGPath {

    /**
     * Get all the edges on the path in order
     * 
     * @return edges on the path
     */
    List<PDGEdge> getEdgeList();

    /**
     * Get a list of all nodes on the path in order
     * 
     * @return nodes on the path
     */
    List<AbstractPDGNode> getNodeList();

    /**
     * Get the last node on the path
     * 
     * @return last node on the path
     */
    AbstractPDGNode getEndVertex();

    /**
     * Get the first node on the path
     * 
     * @return first node on the path
     */
    AbstractPDGNode getStartVertex();

}