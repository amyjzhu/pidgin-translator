package accrue.query.primitive;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import accrue.query.bool.BooleanAnd;
import accrue.query.bool.BooleanExpression;
import accrue.query.bool.BooleanNot;
import accrue.query.bool.BooleanOr;
import accrue.query.bool.BooleanVariable;
import accrue.query.util.Environment;
import accrue.util.OrderedPair;

/**
 * Remove nodes guarded by a given set of nodes, used for access control. A
 * node, N, is "guarded" by a set of PC nodes, S, if all paths into N are
 * control dependent on at least one node in S.
 * <p>
 * This class finds PC node representing the boolean condition passed in and
 * removes nodes guarded by those PC nodes
 */
public class RemoveGuardedByBool extends PrimitiveExpression {

    /**
     * Nodes will be removed if control dependent on this expression
     */
    private final BooleanExpression b;

    /**
     * Create a new remove guarded by taking a boolean expression
     * 
     * @param b
     *            Boolean expression for guards
     */
    public RemoveGuardedByBool(BooleanExpression b) {
        this.b = b;
    }

    public BooleanExpression getExpression() {
        return b;
    }

    //    @Override
//    public ProgramDependenceGraph evaluate(ProgramDependenceGraph g, Environment env) {
//
//        Set<AbstractPDGNode> pcNodes = findPCNodes(b, g, env, true);
//
//        RemoveGuardedCF rg = new RemoveGuardedCF();
//        return rg.restrict(g, pcNodes, Collections.<AbstractPDGNode> emptySet());
//    }
//
//    /**
//     * Find the set of PC nodes that correspond to the boolean expression
//     *
//     * @param bexp
//     *            boolean expression to get the PC nodes for
//     * @param g
//     *            Graph we are working in
//     *
//     * @param env
//     *            current variable environment
//     * @param edgeType
//     *            true if we are currently looking for TRUE edges from the
//     *            expression false if we are currently looking for FALSE edges
//     *            from the expression
//     *
//     * @return set of PC nodes
//     */
//    private Set<AbstractPDGNode> findPCNodes(BooleanExpression bexp, ProgramDependenceGraph g, Environment env, boolean edgeType) {
//        switch (bexp.getType()) {
//        case VAR:
//            BooleanVariable bvar = (BooleanVariable) bexp;
//            FindPCNodesRestrictor findPC = getNodeFinder(edgeType);
//            return findPC.restrict(g, bvar.evaluate(env), Collections.<AbstractPDGNode> emptySet()).vertexSet();
//        case NOT:
//            // (!b, et) === (b, !et)
//            BooleanNot bnot = (BooleanNot) bexp;
//            return findPCNodes(bnot.getNegated(), g, env, !edgeType);
//        case AND:
//            // (b && c, TRUE) === (b, TRUE) intersection (c, TRUE)
//            // (b && c, FALSE) === (b, FALSE) union (c, FALSE)
//            BooleanAnd band = (BooleanAnd) bexp;
//            BooleanExpression c1 = band.getB1();
//            BooleanExpression c2 = band.getB2();
//            if (edgeType) {
//                Set<AbstractPDGNode> s1 = findPCNodes(c1, g, env, true);
//                Set<AbstractPDGNode> s2 = findPCNodes(c2, g, env, true);
//                return intersection(s1, s2);
//            } else {
//                return union(findPCNodes(c1, g, env, false), findPCNodes(c2, g, env, false));
//            }
//        case OR:
//            // (b || c, TRUE) === (b, TRUE) union (c, TRUE)
//            // (b || c, FALSE) === (b, FALSE) intersection (c, FALSE)
//            BooleanOr bor = (BooleanOr) bexp;
//            BooleanExpression d1 = bor.getB1();
//            BooleanExpression d2 = bor.getB2();
//            if (edgeType) {
//                return union(findPCNodes(d1, g, env, true), findPCNodes(d2, g, env, true));
//            } else {
//                return intersection(findPCNodes(d1, g, env, false), findPCNodes(d2, g, env, false));
//            }
//        default:
//            throw new RuntimeException("Missing case in switch.");
//        }
//    }
//
//    /**
//     * Destructively compute union of two sets
//     *
//     * @param s1
//     *            first set
//     * @param s2
//     *            second set
//     * @return union of s1 and s2
//     */
//    private Set<AbstractPDGNode> union(Set<AbstractPDGNode> s1, Set<AbstractPDGNode> s2) {
//        s1.addAll(s2);
//        return s1;
//    }
//
//    /**
//     * Destructively compute intersection of two sets
//     *
//     * @param s1
//     *            first set
//     * @param s2
//     *            second set
//     * @return intersection of s1 and s2
//     */
//    private Set<AbstractPDGNode> intersection(Set<AbstractPDGNode> s1, Set<AbstractPDGNode> s2) {
//        s1.retainAll(s2);
//        return s1;
//    }
//
//    /**
//     * Get a map from each variable or negation to the set of corresponding PDG
//     * nodes
//     *
//     * @param bexp
//     *            expression to get the map for
//     * @param env
//     *            current variable environment
//     * @return map containing mappings for all variables and negations in bexp
//     */
//    private Map<BooleanExpression, Set<AbstractPDGNode>> getNodesForVars(BooleanExpression bexp, Environment env) {
//        Map<BooleanExpression, Set<AbstractPDGNode>> nodes = new LinkedHashMap<>();
//        switch (bexp.getType()) {
//        case VAR:
//            BooleanVariable bvar = (BooleanVariable) bexp;
//            nodes.put(bvar, bvar.evaluate(env));
//            return nodes;
//        case NOT:
//            BooleanNot bnot = (BooleanNot) bexp;
//            nodes.putAll(getNodesForVars(bnot.getNegated(), env));
//            return nodes;
//        case AND:
//            BooleanAnd band = (BooleanAnd) bexp;
//            nodes.putAll(getNodesForVars(band.getB1(), env));
//            nodes.putAll(getNodesForVars(band.getB2(), env));
//            return nodes;
//        case OR:
//            BooleanOr bor = (BooleanOr) bexp;
//            nodes.putAll(getNodesForVars(bor.getB1(), env));
//            nodes.putAll(getNodesForVars(bor.getB2(), env));
//            return nodes;
//        default:
//            throw new RuntimeException("Missing case in switch.");
//        }
//    }
//
//    @Override
//    public Object getAdditionalCacheKey(Environment env) {
//        return new OrderedPair<BooleanExpression, Map<BooleanExpression, Set<AbstractPDGNode>>>(this.b, getNodesForVars(b, env));
//    }
//
//    /**
//     * Get the algorithm used to find PC nodes based on the edge type, et
//     *
//     * @param edgeType
//     *            if true then the edge type, et, will be TRUE otherwise false
//     *
//     * @return algorithm to find PC nodes
//     */
//    private FindPCNodesRestrictor getNodeFinder(boolean edgeType) {
//        if (edgeType) {
//            return new FindPCNodesRestrictor(true, false);
//        }
//        return new FindPCNodesRestrictor(false, true);
//
//    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RemoveGuardedByBool)) {
            return false;
        }
        RemoveGuardedByBool other = (RemoveGuardedByBool) obj;
        return other.b.equals(this.b);
    }

    @Override
    public int hashCode() {
        return 31 * b.hashCode();
    }

    @Override
    public String toString() {
        return "removeGuardedByBool(" + b + ")";
    }
}
