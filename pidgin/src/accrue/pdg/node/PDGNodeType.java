package accrue.pdg.node;

/**
 * Descriptive type of a PDG node each giving semantic meaning to the node
 */
public enum PDGNodeType {
    // Expressions
    /**
     * Local variable
     */
    LOCAL(false, false, false, false),
    /**
     * Output expression (language construct added to Java for convenience of
     * analysis)
     */
    OUTPUT(false, false, false, false),
    /**
     * Any other kind of expression
     */
    OTHER_EXPRESSION(false, false, false, false),
    /**
     * Node representing the assignment of an argument to a formal parameter
     */
    FORMAL_ASSIGNMENT(false, false, false, false),
    /**
     * Expression that cannot be broken into smaller sub-expressions, these
     * include constants, newly created objects, type nodes...
     */
    BASE_VALUE(false, false, false, false),
    /**
     * Special merge node for nodes representing the value of an exception. See
     * {@link PDGNode#isMergeNode()}.
     */
    EXCEPTION_MERGE(false, false, false, false),
    /**
     * Representation of a procedure exit in the caller. If there is an assignment
     * from the return value then the node for the callee exit value is copied into 
     * this type of node which is then copied into the assignee.
     */
    EXIT_ASSIGNMENT(false, false, false, false),
    // Path Conditions
    /**
     * Summary node for the path condition at procedure or initializer exit
     */
    EXIT_PC_SUMMARY(true, false, true, true),
    /**
     * Summary node for the path condition at procedure or initializer entry
     */
    ENTRY_PC_SUMMARY(true, true, false, true),
    /**
     * Node representing the adding of a particular boolean expression to the
     * path condition, there should be a {@link PDGEdgeType#TRUE} edge from the
     * node representing the value of this new condition
     */
    BOOLEAN_TRUE_PC(true, false, false, false),
    /**
     * Node representing the adding of the negation of a particular boolean
     * expression to the path condition, there should be a
     * {@link PDGEdgeType#FALSE} edge from the node representing the value of
     * this new condition
     */
    BOOLEAN_FALSE_PC(true, false, false, false),
    /**
     * Special merge node for nodes each representing a path condition. See
     * {@link PDGNode#isMergeNode()}.
     */
    PC_MERGE(true, false, false, false),
    /**
     * PC node in the caller joining the caller PC at the call site and the 
     * callee PC at the return
     */
    EXIT_PC_JOIN(true, false, false, false),
    /**
     * Catch all for any other type of path condition. An example is the choice
     * of receiver for a method call.
     */
    PC_OTHER(true, false, false, false),
    // Procedure summary nodes
    /**
     * Summary node for a formal argument to a procedure
     */
    FORMAL_SUMMARY(false, true, false, true),
    /**
     * Node representing the value of "this"
     */
    THIS(false, false, false, true),
    /**
     * Summary node for the value upon procedure exit (exceptional return or
     * normal termination)
     */
    EXIT_SUMMARY(false, false, true, true),
    /**
     * Abstract location which is represents or more concrete locations
     */
    ABSTRACT_LOCATION(false, false, false, false),
    /**
     * Abstract location summary node representing the value of the location at 
     * the start of a procedure call.
     */
    LOCATION_SUMMARY(false, false, false, true),
    /**
     * Abstract location summary node representing the value of the location at 
     * the end of a procedure call.
     */
    LOCATION_EXIT(false, false, true, true);

    /**
     * True if the type is a path conditions
     */
    private final boolean isPathCondition;
    /**
     * True if the type is an entry summary
     */
    private final boolean isEntrySummary;
    /**
     * True if the type is an exit summary
     */
    private final boolean isExitSummary;
    /**
     * is the type the summary for the exit of a procedure
     */
    private final boolean isProcedureSummary;

    /**
     * Constructor a PDG node type
     * 
     * @param isPathCondition
     *            is the type a path condition type
     * @param isEntrySummary 
     *            is the type the summary for the entry to a procedure
     * @param isExitSummary
     *            is the type the summary for the exit of a procedure
     * @param isProcedureSummary
     *            is the type a summary for procedure input or output
     */
    private PDGNodeType(boolean isPathCondition, boolean isEntrySummary,
            boolean isExitSummary, boolean isProcedureSummary) {
        this.isPathCondition = isPathCondition;
        this.isExitSummary = isExitSummary;
        this.isProcedureSummary = isProcedureSummary;
        this.isEntrySummary = isEntrySummary;
    }

    /**
     * Is the type a path condition
     * 
     * @return true for path condition types, false otherwise
     */
    public boolean isPathCondition() {
        return isPathCondition;
    }

    /**
     * Is the type a summary for the exit of a procedure
     * 
     * @return true for exit summary types, false otherwise
     */
    public boolean isExitSummary() {
        return isExitSummary;
    }
    
    /**
     * Is the type a summary for the entry to a procedure
     * 
     * @return true for entry summary types, false otherwise
     */
    public boolean isEntrySummary() {
        return isEntrySummary;
    }

    /**
     * Is the type a summary node for a procedure
     * 
     * @return true for summary types false otherwise
     */
    public boolean isProcedureSummary() {
        return isProcedureSummary;
    }

    /**
     * Is this a summary node for an Abstract Location
     * 
     * @return true if this is a summary node for an abstract location
     */
    public boolean isLocationSummary() {
        if (this == LOCATION_SUMMARY || this == LOCATION_EXIT) {
            return true;
        }
        return false;
    }
}
