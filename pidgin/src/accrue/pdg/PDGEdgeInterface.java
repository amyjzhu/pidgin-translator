package accrue.pdg;

import accrue.pdg.util.CallSiteEdgeLabel;


/**
 * Edge in a program dependence graph. These (directed) edges represent a
 * dependence between two values in a program, represented by nodes in the PDG.
 */

/**
 * Edge in a program dependence graph. These (directed) edges represent a
 * dependence between two values in a program, represented by nodes in the PDG.
 * 
 * @param <N> Type of the PDG Nodes
 */
public interface PDGEdgeInterface<N> {

    /**
     * Source node of the edge. The value represented by the <code>dest</code>
     * node depends on the value represented by the <code>source</code> node.
     * 
     * @return Source node
     */
    public N getSource();

    /**
     * Destination node of the edge. The value represented by the
     * <code>dest</code> node depends on the value represented by the
     * <code>source</code> node.
     * 
     * @return Destination node
     */
    public N getTarget();

    /**
     * Type of dependency this edge represents
     * 
     * @return Type of dependency
     */
    public PDGEdgeType getType();

    /**
     * If this edge enters or leaves a procedure call, this label indicates the callSite
     * 
     * @return position for call site
     */
    public CallSiteEdgeLabel getEdgeLabel();
}
