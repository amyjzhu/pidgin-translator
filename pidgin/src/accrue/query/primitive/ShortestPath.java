package accrue.query.primitive;

import java.util.Set;

import accrue.query.expression.Expression;
import accrue.query.util.Environment;
import accrue.util.OrderedPair;

/**
 * Find the shortest path between two subgraphs
 */
public class ShortestPath extends PrimitiveExpression {

    /**
     * First expression
     */
    private final Expression e1;
    /**
     * Second expression
     */
    private final Expression e2;

    /**
     * Create a new shortest path expression
     * 
     * @param e1
     *            First expression
     * @param e2
     *            Second expression
     */
    public ShortestPath(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public Expression getE1() {
        return e1;
    }

    public Expression getE2() {
        return e2;
    }

    //    @Override
//    public ProgramDependenceGraph evaluate(ProgramDependenceGraph g, Environment env) {
//        accrue.algorithm.restrict.ShortestPath sp = new accrue.algorithm.restrict.ShortestPath();
//
//        return sp.restrict(g, getSources(env), getTargets(env));
//    }
//
//    /**
//     * Get the set of nodes for the sources
//     *
//     * @param env
//     *            current variable environment
//     * @return set of source nodes
//     */
//    private Set<AbstractPDGNode> getSources(Environment env) {
//        return e1.evaluate(env).vertexSet();
//    }
//
//    /**
//     * Get the set of nodes for the targets
//     *
//     * @param env
//     *            current variable environment
//     * @return set of target nodes
//     */
//    private Set<AbstractPDGNode> getTargets(Environment env) {
//        return e2.evaluate(env).vertexSet();
//    }
//
//    @Override
//    public Object getAdditionalCacheKey(Environment env) {
//        return new OrderedPair<Set<AbstractPDGNode>, Set<AbstractPDGNode>>(getSources(env), getTargets(env));
//    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ShortestPath)) {
            return false;
        }
        ShortestPath other = (ShortestPath) obj;
        return other.e1.equals(this.e1) && other.e2.equals(this.e2);
    }

    @Override
    public int hashCode() {
        return 31 * e1.hashCode() + 23 * e2.hashCode();
    }

    @Override
    public String toString() {
        return "shortestPath(" + e1 + ", " + e2 + ")";
    }
}
