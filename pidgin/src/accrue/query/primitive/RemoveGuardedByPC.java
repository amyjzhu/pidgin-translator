package accrue.query.primitive;

import java.util.Collections;
import java.util.Set;

import accrue.query.expression.Expression;
import accrue.query.util.Environment;

/**
 * Remove nodes guarded by a given set of nodes, used for access control. A
 * node, N, is "guarded" by a set of nodes, S, if all paths into N are control
 * dependent on at least one node in S. Nodes in S can be expression nodes, in
 * which case the PC nodes representing the fact that nodes in S are TRUE and/or
 * FALSE (depending on the optional argument) are found, and nodes guarded by
 * these PC nodes are removed.
 */
public class RemoveGuardedByPC extends PrimitiveExpression {

    /**
     * First expression
     */
    private final Expression e;

    /**
     * Create a new remove guarded expression
     * 
     * @param e
     *            Expression evaluating to guards
     */
    public RemoveGuardedByPC(Expression e) {
        this.e = e;
    }

    public Expression getExpression() {
        return e;
    }

    //    @Override
//    public ProgramDependenceGraph evaluate(ProgramDependenceGraph g, Environment env) {
//
//        // For this we must have already computed the PC nodes
//        ProgramDependenceGraph pcNodes = e.evaluate(env);
//        RemoveGuardedCF rg = new RemoveGuardedCF();
//        return rg.restrict(g, pcNodes.vertexSet(), Collections.<AbstractPDGNode> emptySet());
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
//        return getGuardNodes(env);
//    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RemoveGuardedByPC)) {
            return false;
        }
        RemoveGuardedByPC other = (RemoveGuardedByPC) obj;
        return other.e.equals(this.e);
    }

    @Override
    public int hashCode() {
        return 31 * e.hashCode();
    }

    @Override
    public String toString() {
        return "removeGuardedBy(" + e + ")";
    }
}
