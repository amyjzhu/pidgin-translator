package accrue.query;

import accrue.query.util.Environment;

/**
 * Top level of the query languages, either a query or a policy
 * 
 * @param <T>
 *            type this evaluates to
 */
public interface TopLevel<T> {

    /**
     * Evaluate the top level query or policy
     * 
     * @param env
     *            environment
     * @return evaluated query or policy
     */
    T evaluate(Environment env);
}
