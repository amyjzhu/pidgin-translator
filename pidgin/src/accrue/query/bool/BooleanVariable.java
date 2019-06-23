package accrue.query.bool;

import java.util.Set;

import accrue.pdg.node.AbstractPDGNode;
import accrue.query.expression.Expression;
import accrue.query.util.Environment;

/**
 * Wrapper around a graph expression that, when evaluated, contains nodes
 * represent values of a variable in a boolean expression
 */
public class BooleanVariable implements BooleanExpression {

    /**
     * Expression that evaluates to nodes that represent values of this variable in
     * a boolean expression
     */
    private final Expression expr;
    
    /**
     * Create a variable in a boolean expression
     * 
     * @param expr
     *            Expression that evaluates to nodes that represent values of
     *            this variable in a boolean expression
     */
    public BooleanVariable(Expression expr) {
        this.expr = expr;
    }
    
    @Override
    public BoolType getType() {
        return BoolType.VAR;
    }
    
    @Override
    public String toString() {
        return expr.toString();
    }
    
    /**
     * Evaluate the underlying expression to a graph and take the nodes. Nodes
     * in this set represent values of this variable.
     * 
     * @param env
     *            current variable environment
     * @return nodes for the underlying expression
     */
    public Set<AbstractPDGNode> evaluate(Environment env) {
        return expr.evaluate(env).vertexSet();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((expr == null) ? 0 : expr.hashCode());
        return result;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BooleanVariable other = (BooleanVariable) obj;
        if (expr == null) {
            if (other.expr != null)
                return false;
        } else if (!expr.equals(other.expr))
            return false;
        return true;
    }
}
