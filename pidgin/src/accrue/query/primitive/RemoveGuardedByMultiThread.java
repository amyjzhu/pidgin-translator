package accrue.query.primitive;

import java.util.Set;

import accrue.query.expression.Expression;
import accrue.query.util.Argument;
import accrue.query.util.Environment;
import accrue.util.OrderedPair;

/**
 * Remove nodes guarded by a given set of nodes, used for access control
 */
public class RemoveGuardedByMultiThread extends PrimitiveExpression {

    /**
     * First expression
     */
    private final Expression e;
    /**
     * Edge type to follow
     */
    private final Argument<?> et;

    /**
     * Create a new shortest path expression
     * 
     * @param e
     *            Expression evaluating to guards
     * @param et
     *            Edge type to follow
     */
    public RemoveGuardedByMultiThread(Expression e, Argument<?> et) {
        this.e = e;
        this.et = et;
    }

//    @Override
//    public ProgramDependenceGraph evaluate(ProgramDependenceGraph g, Environment env) {
//
//        FindPCNodesMultiThread findPC;
//
//        if (!et.isAbsent()) {
//            PDGEdgeType type = Argument.getEdgeTypeForArg(et, env);
//
//            if (type == PDGEdgeType.TRUE) {
//                findPC = new FindPCNodesMultiThread(g, getGuardNodes(env), true, false);
//            } else if (type == PDGEdgeType.FALSE) {
//                findPC = new FindPCNodesMultiThread(g, getGuardNodes(env), false, true);
//            } else {
//                throw new IllegalArgumentException("Invalid edge type in RemoveGuardedBy: " + type);
//            }
//        } else {
//            findPC = new FindPCNodesMultiThread(g, getGuardNodes(env), true, true);
//        }
//
//        findPC.computeResult();
//        ProgramDependenceGraph pcNodes = findPC.getResult();
//
//        RemoveGuardedMultiThread rg = new RemoveGuardedMultiThread(g, pcNodes.vertexSet());
//        rg.computeResult();
//        return rg.getResult();
//    }
//
//    /**
//     * Get the set of nodes representing guards
//     *
//     * @param env
//     *            current variable environment
//     * @return set of guard nodes
//     */
//    private Set<AbstractPDGNode> getGuardNodes(Environment env) {
//        return e.evaluate(env).vertexSet();
//    }
//
//    @Override
//    public Object getAdditionalCacheKey(Environment env) {
//        return new OrderedPair<PDGEdgeType, Set<AbstractPDGNode>>(Argument.getEdgeTypeForArg(et, env), getGuardNodes(env));
//    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RemoveGuardedByMultiThread)) {
            return false;
        }
        RemoveGuardedByMultiThread other = (RemoveGuardedByMultiThread) obj;
        return other.e.equals(this.e) && other.et.equals(this.et);
    }

    @Override
    public int hashCode() {
        return 31 * e.hashCode() + 23 * et.hashCode();
    }

    @Override
    public String toString() {
        return "removeGuardedByMultiThread(" + e + (et.isAbsent() ? "" : ", " + et.value()) + ")";
    }
}
