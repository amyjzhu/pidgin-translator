package accrue.query.expression;

import java.util.Map;

import accrue.query.util.Environment;
import accrue.util.SoftHashMap;

/**
 * Expressions evaluate to a graph
 */
public abstract class Expression {

    public String label = "";
    
    @Override
    public abstract boolean equals(Object obj);
    
    @Override
    public abstract int hashCode();
    
    @Override
    public abstract String toString();

    public String getLabel() {
        return label;
    }


//    /**
//     * Map storing cached newquery results
//     */
//    private static Map<Class<?>, Map<Object, ProgramDependenceGraph>> cacheResults =
//            new SoftHashMap<Class<?>, Map<Object, ProgramDependenceGraph>>();
   
//    /**
//     * Evaluate the expression in the current environment
//     *
//     * @param env
//     *            current variable environment
//     * @return Graph resulting from evaluating this
//     */
//    public abstract ProgramDependenceGraph evaluate(Environment env);
//
//    /**
//     * Get cached results for the given expression and keys. Or null if none is
//     * found.
//     *
//     * @param expr
//     *            Class for expression to get the cached results for
//     * @param key
//     *            additional cache key
//     * @return cached result or null if there is no result in the cache
//     */
//    public final ProgramDependenceGraph getCachedResults(Class<?> expr, Object key) {
//        if (key == null) {
//            return null;
//        }
//
//        Map<Object, ProgramDependenceGraph> m = cacheResults.get(expr);
//        if (m == null) {
//            return null;
//        }
//        // System.err.println("Found in Cache: " + expr);
//        ProgramDependenceGraph r = m.get(key);
//        // System.err.println("Found env Cache Too");
//        return r;
//    }

    /**
     * Store result in the cache for the given expression and keys.
     * 
     * @param expr
     *            Class for expression to put results into the cache for
     * @param key
     *            additional cache key
     * @param result
     *            what to store into the cache
     */
//    public final void storeCachedResults(Class<?> expr, ProgramDependenceGraph result, Object key) {
//        Map<Object, ProgramDependenceGraph> m = cacheResults.get(expr);
//        if (m == null) {
//            m = new SoftHashMap<Object, ProgramDependenceGraph>();
//            cacheResults.put(expr, m);
//        }
//        m.put(key, result);
//
//    }
}
