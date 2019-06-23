package accrue.query.primitive;

import java.util.Set;

import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.graph.PDGFactory;
import accrue.pdg.node.AbstractPDGNode;
import accrue.query.util.Argument;
import accrue.query.util.Environment;

/**
 * Get nodes matching a particular expression
 */
public class ForExpression extends PrimitiveNodeMatchingExpression {

    /**
     * name of the expression
     */
    private final Argument<?> name;

    /**
     * Get a new ForExpression for nodes matching a
     * 
     * @param name
     *            name of the expression
     */
    public ForExpression(Argument<?> name) {
        this.name = name;
    }

    @Override
    public ProgramDependenceGraph evaluate(ProgramDependenceGraph g, Environment env) {

        String toMatch = Argument.getStringForArg(name, env);
        
        Set<AbstractPDGNode> matching = getMatches(g.vertexSet(), toMatch);

        ProgramDependenceGraph newG = PDGFactory.retainNodes(g, matching);
        if (newG.isEmpty()) {
            throw new RuntimeException(this + " evaluated to an empty graph.\nArgument was:\n\n\"" + toMatch + "\"");
        }
        return newG;
    }

    @Override
    public String stringToMatch(AbstractPDGNode n) {
        return n.getName();
    }

    @Override
    public Object getAdditionalCacheKey(Environment env) {
        return Argument.getStringForArg(name, env);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ForExpression)) {
            return false;
        }
        ForExpression other = (ForExpression) obj;
        return other.name.equals(this.name);
    }

    @Override
    public int hashCode() {
        return 29 * name.hashCode();
    }

    @Override
    public String toString() {
        return "forExpression(" + name + ")";
    }
}
