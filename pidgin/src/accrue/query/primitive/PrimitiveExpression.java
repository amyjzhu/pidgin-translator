package accrue.query.primitive;

import accrue.pdg.ProgramDependenceGraph;
import accrue.query.util.Environment;

/**
 * Primitive QL expression, subgraph of the graph passed in
 */
public abstract class PrimitiveExpression {

    /**
     * Evaluate the primitive expression after a call to e.pe
     * 
     * @param g
     *            graph to evaluate in
     * @param env
     *            current variable environment
     * @return Subgraph of e resulting from evaluating this
     */
    public ProgramDependenceGraph evaluateCached(ProgramDependenceGraph g, Environment env) {

        Object cacheKey = getAdditionalCacheKey(env);

        long start = System.currentTimeMillis();
        ProgramDependenceGraph cacheResults;
        if ((cacheResults = g.getCachedResults(this.getClass(), cacheKey)) != null) {
            System.err.println(this.toString() + ": " + (System.currentTimeMillis() - start) + " (cached)");
            return cacheResults;
        }

        ProgramDependenceGraph res = evaluate(g, env);

        g.storeCachedResults(this.getClass(), res, cacheKey);
        System.err.println(this.toString() + ": " + (System.currentTimeMillis() - start) + " (no cached)");
        return res;
    }

    /**
     * Get cache key (in addition to this expression)
     * 
     * @param env
     *            current variable environment
     * @return Additional cache key
     */
    public abstract Object getAdditionalCacheKey(Environment env);

    /**
     * Evaluate the primitive expression after a call to e.pe
     * 
     * @param e
     *            graph to evaluate in
     * @param env
     *            current variable environment
     * @return Subgraph of e resulting from evaluating this
     */
    public abstract ProgramDependenceGraph evaluate(ProgramDependenceGraph e, Environment env);

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();
}
