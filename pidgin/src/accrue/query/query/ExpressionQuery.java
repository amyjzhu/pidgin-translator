package accrue.query.query;

import accrue.query.expression.Expression;
import accrue.query.util.Environment;

/**
 * Expression as a query
 */
public class ExpressionQuery extends Query {

    /**
     * Expression to treat as a query
     */
    private final Expression e;

    /**
     * Create a query for the given expression
     * 
     * @param e
     *            expression
     */
    public ExpressionQuery(Expression e) {
        this.e = e;
    }
//
//    @Override
//    public ProgramDependenceGraph evaluate(Environment env) {
//        return e.evaluate(env);
//    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ExpressionQuery)) {
            return false;
        }
        ExpressionQuery other = (ExpressionQuery) obj;
        return this.e.equals(other.e);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return prime * e.hashCode();
    }
    
    @Override
    public String toString() {
        return e.toString(); 
    }
}
