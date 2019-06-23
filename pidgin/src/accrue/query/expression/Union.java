package accrue.query.expression;

import java.util.LinkedHashSet;
import java.util.Set;

import accrue.query.util.Environment;
import accrue.util.OrderedPair;

/**
 * Evaluate two expressions and take the union
 */
public class Union extends Expression {

    /**
     * First expression
     */
    private Expression e1;
    /**
     * Second expression
     */
    private Expression e2;

    /**
     * Create a new union expression
     * 
     * @param e1
     *            First expression
     * @param e2
     *            Second expression
     */
    public Union(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }
//
//    @Override
//    public ProgramDependenceGraph evaluate(Environment env) {
//        ProgramDependenceGraph p1 = e1.evaluate(env);
//        ProgramDependenceGraph p2 = e2.evaluate(env);
//
//        long start = System.currentTimeMillis();
//        // TODO should be unordered
//        OrderedPair<ProgramDependenceGraph, ProgramDependenceGraph> cacheKey =
//                new OrderedPair<ProgramDependenceGraph, ProgramDependenceGraph>(p1, p2);
//        ProgramDependenceGraph cacheResults;
//        if ((cacheResults = getCachedResults(this.getClass(), cacheKey)) != null) {
//            System.err.println(this.toString() + ": " + (System.currentTimeMillis() - start) + " (cached)");
//            return cacheResults;
//        }
//
//        Set<PDGEdge> s1 = new LinkedHashSet<PDGEdge>(p1.edgeSet());
//        s1.retainAll(p2.edgeSet());
//
//        Set<AbstractPDGNode> n1 = new LinkedHashSet<AbstractPDGNode>(p1.vertexSet());
//        n1.retainAll(p2.vertexSet());
//
//        ProgramDependenceGraph res = PDGFactory.union(p1, p2);
//
//        storeCachedResults(this.getClass(), res, cacheKey);
//        System.err.println(this.toString() + ": " + (System.currentTimeMillis() - start) + " (no cached)");
//        return res;
//    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Union)) {
            return false;
        }
        Union other = (Union) obj;
        return other.e1.equals(this.e1) && other.e2.equals(this.e2);
    }

    @Override
    public int hashCode() {
        return 31 * e1.hashCode() + 23 * e2.hashCode();
    }

    @Override
    public String toString() {
        return e1 + " OR " + e2;
    }
}
