package accrue.query.bool;

/**
 * Type of boolean expression used for switching
 */
public enum BoolType {
    /**
     * Boolean variable
     */
    VAR, 
    /**
     * Conjunction
     */
    AND, 
    /**
     * disjunction
     */
    OR, 
    /**
     * negation
     */
    NOT;
}
