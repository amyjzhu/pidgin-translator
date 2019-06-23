package accrue.query.primitive;

import java.util.Set;

import accrue.algorithm.SlicingAlgorithms;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.node.AbstractPDGNode;
import accrue.query.expression.Expression;
import accrue.query.expression.Variable;
import accrue.query.util.Argument;
import accrue.query.util.Environment;
import accrue.util.OrderedPair;

/**
 * Compute a graph that contains the edges up to a particular depth backward
 * from nodes in a graph
 */
public class BackwardSlice extends PrimitiveExpression {

    /**
     * Expression to slice from
     */
    private final Expression e;
    /**
     * Depth to slice to
     */
    private final Argument<?> depth;

    /**
     * Create a new slice primitive
     * 
     * @param e
     *            Expression to slice from
     * @param d
     *            Depth to slice to
     */
    public BackwardSlice(Expression e, Argument<?> d) {
        this.e = e;
        this.depth = d;
    }

    @Override
    public ProgramDependenceGraph evaluate(ProgramDependenceGraph g, Environment env) {
        return SlicingAlgorithms.backwardSlice(g, getStartNodes(env), getDepth(env));
    }

    /**
     * Get the depth to slice to
     * 
     * @param env
     *            Variable environment
     * @return depth to slice to
     */
    private Integer getDepth(Environment env) {
        Integer d;
        if (depth.isAbsent()) {
            d = null;
        } else if (depth.value() instanceof Integer) {
            d = (Integer) depth.value();
        } else if (depth.value() instanceof Variable) {
            d = ((Variable) depth.value()).evaluateInteger(env);
        } else {
            throw new IllegalArgumentException("depth is the wrong type: " + depth.value().getClass() + " value is " + depth.value());
        }

        return d;
    }

    /**
     * Get the set of nodes to slice from
     * 
     * @param env
     *            current variable environment
     * @return set of nodes to slice from
     */
    private Set<AbstractPDGNode> getStartNodes(Environment env) {
        return e.evaluate(env).vertexSet();
    }

    @Override
    public Object getAdditionalCacheKey(Environment env) {
        return new OrderedPair<Integer, Set<AbstractPDGNode>>(getDepth(env), getStartNodes(env));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BackwardSlice)) {
            return false;
        }
        BackwardSlice other = (BackwardSlice) obj;
        return other.e.equals(this.e) && other.depth.equals(this.depth);
    }

    @Override
    public int hashCode() {
        return 37 * e.hashCode() + 29 * depth.hashCode();
    }

    @Override
    public String toString() {
        return "backwardSlice(" + e + (depth.isAbsent() ? "" : ", " + depth.value()) + ")";
    }
}
