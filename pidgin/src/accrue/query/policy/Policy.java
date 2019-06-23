package accrue.query.policy;

import accrue.query.TopLevel;
import accrue.query.query.Query;
import accrue.query.util.Environment;

/**
 * A policy is an assertion on a Query
 */
public abstract class Policy implements TopLevel<Boolean> {

    /**
     * Query to make an assertion on
     */
    protected final Query q;

    /**
     * New policy for q
     * 
     * @param q
     *            Query to make an assertion on
     */
    public Policy(Query q) {
        this.q = q;
    }

    public Query getQuery() {
        return q;
    }

    /**
     * Evaluate the policy, if it holds then return true otherwise throw an
     * Error.
     * 
     * @param env
     *            environment
     * @return true if policy holds throws Error otherwise
     */
    public Boolean evaluate(Environment env) {
        if (!assertion(q, env)) {
            throw new AssertionError("Assertion Failed:" + this.getClass());
        }
        return true;
    }

    /**
     * Evaluate assertion for the policy
     * 
     * @param q
     *            query
     * @param env
     *            environment
     * @return true if assertion holds, false otherwise
     */
    protected abstract boolean assertion(Query q, Environment env);

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();
}
