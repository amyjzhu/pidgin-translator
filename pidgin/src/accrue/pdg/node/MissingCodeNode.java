package accrue.pdg.node;

import accrue.pdg.node.PDGNodeFactory.AbstractPDGNodeInfo;

/**
 * Node representing code that is missing during analysis.
 * 
 * @see MissingCodePDGNode
 */
public class MissingCodeNode extends AbstractPDGNode {

    /**
     * Name of the procedure missing source code
     */
    private String procedureName;
    /**
     * true if the receiver is missing as well (i.e. the points to set is empty)
     */
    private boolean isMissingReceiver;
    /**
     * if this node is a summary node for values upon procedure exit then this
     * is the {@link ExitMap} {@link Key} this node is for
     */
    private String exitKey;

    /**
     * This should only be called from {@link PDGNodeFactory}
     * 
     * @param info General information about the PDG node
     * @param procedureName Name of the procedure missing source code
     * @param isMissingReceiver true if the receiver is missing as well (i.e. the points to set is empty)
     * @param exitKey if this node is a summary node for values upon procedure exit then this
     * is the {@link ExitMap} {@link Key} this node is for
     * 
     * @see MissingCodePDGNode
     */
    protected MissingCodeNode(AbstractPDGNodeInfo info, String procedureName, boolean isMissingReceiver, String exitKey) {
        super(info);
        this.procedureName = procedureName;
        this.isMissingReceiver = isMissingReceiver;
        this.exitKey = exitKey;
    }

    /**
     * Name of the procedure missing source code
     * 
     * @return fully qualified procedure name
     */
    public String getProcedureName() {
        return procedureName;
    }

    /**
     * true if the points to set for the receiver was empty
     * 
     * @return whether the receiver was missing as well as the code
     */
    public boolean isMissingReceiver() {
        return isMissingReceiver;
    }

    /**
     * If this node is an exit summary then this is the key into the
     * {@link ExitMap}
     * 
     * @return string representation of {@link Key} or null if this is not an
     *         exit summary node
     */
    public String getExitKey() {
        return exitKey;
    }
    
    @Override
    public String getLongDescription() {
        return procedureName + " " + exitKey;
    }
}
