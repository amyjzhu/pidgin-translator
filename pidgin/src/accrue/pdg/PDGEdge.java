package accrue.pdg;

import accrue.pdg.node.AbstractPDGNode;

/**
 * Edge in a program dependence graph. These (directed) edges represent a
 * dependence between two values in a program, represented by nodes in the PDG.
 */
public interface PDGEdge extends PDGEdgeInterface<AbstractPDGNode> {

    /**
     * Swap out the edge type in this edge. This is only valid when replacing singleton MERGE edges with COPY or CONJUNCTION edges.
     * 
     * @param newType type to replace the existing type with
     */
    void changeEdgeType(PDGEdgeType newType);
}
