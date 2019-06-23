package accrue.pdg.node;

import accrue.pdg.node.PDGNodeFactory.AbstractPDGNodeInfo;
import accrue.pdg.util.Position;

/**
 * Extending classes represent nodes in a program dependence graph. These nodes represent the value of a statement or
 * expression in the program or the value of a boolean expression representing the path condition at a program point.
 * 
 * @see PDGNode
 */
public abstract class AbstractPDGNode implements PDGNodeInterface {

    /**
     * Position in the source code related to this node in the PDG
     */
    private Position position;
    /**
     * Unique nodeID, used to determine node equality
     */
    private int nodeID;
    /**
     * name for the original object in the Infoflow PDG (just used as a label)
     */
    private String name;

    /**
     * type of the PDG node
     */
    private PDGNodeType nodeType;
    /**
     * Name used to group nodes for presentation
     */
    private String groupingName;
    /**
     * Structure holding the fields for AbstractPDGNodes used for serialization and hacks
     */
    protected final AbstractPDGNodeInfo info;

    /**
     * String representation of the analysis context or the String "null" if there is none
     */
    protected final String context;
    /**
     * Type of the expression or null if none
     */
    private String javaType;

    /**
     * Constructor taking an {@link AbstractPDGNodeInfo} which contains the values for the fields common to PDG nodes
     * 
     * @param info object containing general information about PDG nodes
     */
    protected AbstractPDGNode(AbstractPDGNodeInfo info) {
        this.info = info;
        this.position = info.position;
        this.nodeID = info.nodeID;
        this.name = info.name;
        this.nodeType = info.nodeType;
        this.groupingName = info.groupingName;
        this.context = info.context;
        this.javaType = info.javaType;
    }

    /**
     * Position in the source code related to this node in the PDG
     * 
     * @return Position in the source code related to this node in the PDG
     */
    @Override
    public accrue.pdg.util.Position getPosition() {
        return position;
    }

    @Override
    public int hashCode() {
        return nodeID;
    }

    /**
     * label for display purposes
     * 
     * @return name of node
     */
    public String getName() {
        return name;
    }

    /**
     * Type of the PDG node giving finer grained information than the class hierarchy
     * 
     * @return node type
     */
    @Override
    public PDGNodeType getNodeType() {
        return nodeType;
    }

    /**
     * String to be used to group nodes for display purposes nodes with the same grouping name are associated in some
     * way (i.e. they are created in the same procedure body, or they are heap nodes ...)
     * 
     * @return name used group nodes for display purposes
     */
    @Override
    public String groupingName() {
        return groupingName;
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     * Get a longer description for this node
     * 
     * @return string for description
     */
    public abstract String getLongDescription();

    /**
     * Gets the node id
     * 
     * @return unique node identifier
     */
    public int getNodeId() {
        return nodeID;
    }

    /**
     * String representation of the analysis context or the String "null" if there is none
     * 
     * @return context description or the String "null"
     */
    public String getContext() {
        return context;
    }

    /**
     * Get the type of the expression represented by the nodes. For nodes that do not have a type (such as PC nodes),
     * return null;
     * 
     * @return the Java type of the expression, or null if there is none
     */
    public String getJavaType() {
        return javaType;
    }

    /**
     * String for the fully qualified method name this node was created for or "HEAP" if this was an
     * AbstractLocationNode
     * 
     * @return string for the procedure name
     */
    public abstract String getProcedureName();

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractPDGNode)) {
            return false;
        }
        AbstractPDGNode other = (AbstractPDGNode) obj;
        return nodeID == other.nodeID;
    }
}
