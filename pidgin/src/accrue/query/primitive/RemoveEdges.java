package accrue.query.primitive;

import java.util.Set;

import accrue.pdg.PDGEdge;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.graph.PDGFactory;
import accrue.query.expression.Expression;
import accrue.query.util.Environment;

/**
 * Remove edges in a given graph from another graph
 */
public class RemoveEdges extends PrimitiveExpression {

    /**
     * Evaluates to a graph with edges to remove
     */
    private Expression e;

    /**
     * New RemoveEdges expression
     * 
     * @param e
     *            Evaluates to a graph with edges to remove
     */
    public RemoveEdges(Expression e) {
        this.e = e;
    }

    @Override
    public ProgramDependenceGraph evaluate(ProgramDependenceGraph g, Environment env) {
        return PDGFactory.removeEdges(g, edgesToRemove(env));
    }

    /**
     * Get the set of edges to remove
     * 
     * @param env
     *            current variable environment
     * @return set of edges to remove
     */
    private Set<PDGEdge> edgesToRemove(Environment env) {
        return e.evaluate(env).edgeSet();
    }

    @Override
    public Object getAdditionalCacheKey(Environment env) {
        return edgesToRemove(env);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RemoveEdges)) {
            return false;
        }
        RemoveEdges other = (RemoveEdges) obj;
        return other.e.equals(this.e);
    }

    @Override
    public int hashCode() {
        return 13 * e.hashCode();
    }

    @Override
    public String toString() {
        return "removeEdges(" + e + ")";
    }
}
