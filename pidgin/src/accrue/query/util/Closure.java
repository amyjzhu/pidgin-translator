package accrue.query.util;

import accrue.query.expression.Expression;

/**
 * Closure for function and let declarations
 */
public class Closure {
    /**
     * Expression bound to the function name
     */
    private final Expression functionBody;
    /**
     * Environment at the time of function declaration
     */
    private final Environment env;

    /**
     * Create a new closure
     * 
     * @param functionBody
     *            expression that the function is bound to
     * @param env
     *            environment at function decl time
     */
    public Closure(Expression functionBody, Environment env) {
        this.functionBody = functionBody;
        this.env = env;
    }
    
    /**
     * Get the free variable environment
     * 
     * @return closure environment
     */
    public Environment getEnvironment() {
        return env;
    }

    /**
     * Get the function body
     * 
     * @return function body
     */
    public Expression getFunctionBody() {
        return functionBody;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((env == null) ? 0 : env.hashCode());
        result = prime * result + ((functionBody == null) ? 0 : functionBody.hashCode());
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
        Closure other = (Closure) obj;
        if (env == null) {
            if (other.env != null)
                return false;
        }
        else if (!env.equals(other.env))
            return false;
        if (functionBody == null) {
            if (other.functionBody != null)
                return false;
        }
        else if (!functionBody.equals(other.functionBody))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return functionBody + " Env: " + env;
    }
}
