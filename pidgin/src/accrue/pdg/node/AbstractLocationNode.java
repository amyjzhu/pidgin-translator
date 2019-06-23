package accrue.pdg.node;

import accrue.pdg.node.PDGNodeFactory.AbstractPDGNodeInfo;

/**
 * The representation of the value of an abstract heap location. These locations
 * are flow insensitive.
 * 
 * @see AbstractLocationPDGNode
 */
public class AbstractLocationNode extends AbstractPDGNode {

    /**
     * String representation of the abstract location this represents
     */
    private String location;

    /**
     * This should only be called from {@link PDGNodeFactory}
     * 
     * @param info
     *            object containing general information about PDG nodes
     * @param location
     *            String representation of the abstract location this represents
     */
    protected AbstractLocationNode(AbstractPDGNodeInfo info, String location) {
        super(info);
        this.location = location;
    }

    /**
     * Get a string representation of the abstract location this node represents
     * 
     * @return location name
     */
    public String getLocation() {
        return location;
    }
    
    @Override
    public String getLongDescription() {
        return location;
    }
    
    @Override
    public String getProcedureName() {
        return groupingName();
    }
}
