package accrue.query.primitive;

import accrue.query.expression.Expression;
import accrue.query.util.Environment;

/**
 * Remove nodes in a given graph from another graph
 */
public class RemoveNodes extends PrimitiveExpression {

    /**
     * Evaluates to a graph with nodes to remove
     */
    private final Expression e;

    /**
     * New RemoveNodes expression
     * 
     * @param e
     *            Evaluates to a graph with nodes to remove
     */
    public RemoveNodes(Expression e) {
        this.e = e;
    }
//
//    @Override
//    public ProgramDependenceGraph evaluate(ProgramDependenceGraph g, Environment env) {
////        long start = System.currentTimeMillis();
//        ProgramDependenceGraph rem = nodesToRemove(env);
////        System.err.println("computed nodes: " + (System.currentTimeMillis() - start));
////        System.err.println("\tRemoving: " + rem.vertexSet().size() + " Class: " + rem.getClass().getSimpleName());
////        start = System.currentTimeMillis();
//        ProgramDependenceGraph res = PDGFactory.removeNodes(g, rem.vertexSet());
////        System.err.println("\tRemoved nodes: " + (System.currentTimeMillis() - start));
//
//        return res;
//    }
//
//    /**
//     * Get the set of nodes to remove
//     *
//     * @param env
//     *            current variable environment
//     * @return set of nodes to remove
//     */
//    private ProgramDependenceGraph nodesToRemove(Environment env) {
//        return e.evaluate(env);
//    }
//
//    @Override
//    public Object getAdditionalCacheKey(Environment env) {
//        return nodesToRemove(env);
//    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RemoveNodes)) {
            return false;
        }
        RemoveNodes other = (RemoveNodes) obj;
        return other.e.equals(this.e);
    }

    @Override
    public int hashCode() {
        return 17 * e.hashCode();
    }

    @Override
    public String toString() {
        return "removeNodes(" + e + ")";
    }
}
