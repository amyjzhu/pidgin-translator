package accrue.query.bool;

/**
 * Boolean expression to be used by RemoveGuardedBy,
 */
public interface BooleanExpression {
    
    @Override
    public boolean equals(Object obj);
    
    @Override
    public int hashCode();
    
    @Override
    public String toString();
    
    /**
     * Get the type of this expression
     * 
     * @return type of this expression
     */
    BoolType getType();
}
