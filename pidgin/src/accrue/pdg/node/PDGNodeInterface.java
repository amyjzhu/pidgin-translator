package accrue.pdg.node;

import accrue.pdg.util.Position;


/**
 * Program dependence graph node interface used to aid with serialization
 */
public interface PDGNodeInterface {
    /**
     * Name for the group this node belongs to, used for partitioning and
     * display (this could be the procedure this node was created for)
     * 
     * @return descriptive name for the group this node belongs to.
     */
    public String groupingName();

    /**
     * Program dependence graph node type. Each type has its own specified
     * semantics
     * 
     * @return PDG node type
     */
    public PDGNodeType getNodeType();

    /**
     * Get the position in the source code where this node was created 
     * @return source code position
     */
    public Position getPosition();
}
