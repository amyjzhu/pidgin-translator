package accrue.pdg.node;

import accrue.pdg.node.PDGNodeFactory.AbstractPDGNodeInfo;
import accrue.pdg.util.GeneratedExceptionType;

/**
 * Program dependence node representing the value of an expression or formal parameter.
 * 
 * @see ExprPDGNode
 */
public class ExprNode extends AbstractPDGNode {

    /**
     * Name of the procedure or initializer this node was created for
     */
    private final String codeName;
    /**
     * If this node was the result of a VM generated exception (such as a
     * {@link NullPointerException}) then this indicates the type of exception
     * otherwise it is null
     */
    private final GeneratedExceptionType exType;
    
    /**
     * Parameter name of formal if this is a formal assignment node
     */
    private final String paramName;
    
    /**
     * True if this node represents a binary && or ||
     */
    private final boolean isBinaryShortCircuit;
    /**
     * If this node is created as an exit node in the caller, this is the 
     * exit key      
     */
    private String exitKey;

    /**
     * This should only be called from {@link PDGNodeFactory}
     * 
     * @param info
     *            object containing general information about PDG nodes
     * @param codeName
     *            Name of the procedure or initializer this node was created for
     * @param exType
     *            If this node was the result of a VM generated exception (such
     *            as a {@link NullPointerException}) then this indicates the
     *            type of exception otherwise it is null
     * @param paramName
     *            Parameter name of formal if this is a formal assignment node
     * @param isBinaryShortCircuit
     *            True if this node represents a binary && or ||
     * @param exitKey
     *            If this node is created as an exit node in the caller, this is
     *            the exit key
     * @see ExprPDGNode
     */
    protected ExprNode(AbstractPDGNodeInfo info, String codeName, GeneratedExceptionType exType,
            String paramName, boolean isBinaryShortCircuit, String exitKey) {
        super(info);
        this.codeName = codeName;
        this.exType = exType;
        this.paramName = paramName;
        this.isBinaryShortCircuit = isBinaryShortCircuit;
        this.exitKey = exitKey;
    }

    /**
     * Name of the procedure or initializer this node was created for
     * 
     * @return Name of the procedure or initializer this node was created for
     */
    public String getProcedureName() {
        return codeName;
    }
    
    /**
     * If this node was the result of a VM generated exception (such as a
     * {@link NullPointerException}) then this indicates the type of exception
     * otherwise it is null
     * 
     * @return the exception type
     */
    public GeneratedExceptionType getExType() {
        return exType;
    }
    
    /**
     * If this node is created as an exit node in the caller, this is the exit
     * key
     * 
     * @return exit key for an exit node
     */
    public String getExitKey() {
        return exitKey;
    }
    
    /**
     * Name of the formal argument to a function if this node represents the
     * assignment to a formal parameter {@link PDGNodeType#FORMAL_ASSIGNMENT}
     * 
     * @return formal argument name this is an assignment for or null if this is
     *         not a formal assignment name
     */
    public String getParamName() {
        return paramName;
    }
    
    @Override
    public String getLongDescription() {
        return codeName + " context: " + getContext();
    }
    
    /**
     * True if this node represents a binary && or ||
     * 
     * @return True if this node represents a binary && or ||
     */
    public boolean isBinaryShortCircuit() {
        return isBinaryShortCircuit;
    }
}
