package accrue.query.primitive;

import java.util.Collections;
import java.util.Set;

import accrue.algorithm.restrict.FindPCNodesRestrictor;
import accrue.algorithm.restrict.RemoveGuardedCF;
import accrue.pdg.PDGEdgeType;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.node.AbstractPDGNode;
import accrue.query.expression.Expression;
import accrue.query.util.Argument;
import accrue.query.util.Environment;
import accrue.util.OrderedPair;

/**
 * Remove nodes guarded by a given set of nodes, used for access control. A
 * node, N, is "guarded" by a set of nodes, S, if all paths into N are control
 * dependent on at least one node in S. Nodes in S can be expression nodes, in
 * which case the PC nodes representing the fact that nodes in S are TRUE and/or
 * FALSE (depending on the optional argument) are found, and nodes guarded by
 * these PC nodes are removed.
 */
public class RemoveGuardedBy extends PrimitiveExpression {

    /**
     * First expression
     */
    private final Expression e;
    /**
     * Edge type to follow
     */
    private final Argument<?> et;

    /**
     * Create a new remove guarded expression
     * 
     * @param e
     *            Expression evaluating to guards
     * @param et
     *            Edge type to follow
     */
    public RemoveGuardedBy(Expression e, Argument<?> et) {
        this.e = e;
        this.et = et;
    }

    @Override
    public ProgramDependenceGraph evaluate(ProgramDependenceGraph g, Environment env) {

        FindPCNodesRestrictor findPC;

        if (!et.isAbsent()) {
            PDGEdgeType type = Argument.getEdgeTypeForArg(et, env);

            if (type == PDGEdgeType.TRUE) {
                findPC = new FindPCNodesRestrictor(true, false);
            } else if (type == PDGEdgeType.FALSE) {
                findPC = new FindPCNodesRestrictor(false, true);
            } else {
                throw new IllegalArgumentException("Invalid edge type in RemoveGuardedBy: " + type);
            }
        } else {
            findPC = new FindPCNodesRestrictor(true, true);
        }

        ProgramDependenceGraph pcNodes = findPC.restrict(g, getGuardNodes(env), Collections.<AbstractPDGNode> emptySet());

        RemoveGuardedCF rg = new RemoveGuardedCF();
        return rg.restrict(g, pcNodes.vertexSet(), Collections.<AbstractPDGNode> emptySet());
    }

    /**
     * Get the set of nodes representing guards
     * 
     * @param env
     *            current variable environment
     * @return set of guard nodes
     */
    private Set<AbstractPDGNode> getGuardNodes(Environment env) {
        return e.evaluate(env).vertexSet();
    }

    @Override
    public Object getAdditionalCacheKey(Environment env) {
        return new OrderedPair<PDGEdgeType, Set<AbstractPDGNode>>(Argument.getEdgeTypeForArg(et, env), getGuardNodes(env));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RemoveGuardedBy)) {
            return false;
        }
        RemoveGuardedBy other = (RemoveGuardedBy) obj;
        return other.e.equals(this.e) && other.et.equals(this.et);
    }

    @Override
    public int hashCode() {
        return 31 * e.hashCode() + 23 * et.hashCode();
    }

    @Override
    public String toString() {
        return "removeGuardedBy(" + e + (et.isAbsent() ? "" : ", " + et.value()) + ")";
    }
}
