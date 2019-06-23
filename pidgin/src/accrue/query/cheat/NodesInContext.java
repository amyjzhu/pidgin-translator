package accrue.query.cheat;

import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.graph.PDGFactory;
import accrue.pdg.node.AbstractPDGNode;
import accrue.query.primitive.PrimitiveNodeMatchingExpression;
import accrue.query.util.Argument;
import accrue.query.util.Environment;

/**
 * Nodes in a context matching a regex
 */
public class NodesInContext extends PrimitiveNodeMatchingExpression {

    /**
     * name to match
     */
    private final Argument<?> name;

    /**
     * nodes in a context matching the argument
     * 
     * @param name
     *            name to match
     */
    public NodesInContext(Argument<?> name) {
        this.name = name;

    }

    @Override
    public ProgramDependenceGraph evaluate(ProgramDependenceGraph g, Environment env) {
        String pattern = Argument.getStringForArg(name, env);
        ProgramDependenceGraph newG = PDGFactory.retainNodes(g, getMatches(g.vertexSet(), pattern));
        if (newG.isEmpty()) {
            throw new RuntimeException(this 
                    + " evaluated to an empty graph.\nArgument was:\n\n\"" + pattern + "\"");
        }

        return newG;
    }

    @Override
    public String stringToMatch(AbstractPDGNode t) {
        return t.getContext();
    }

    @Override
    public Object getAdditionalCacheKey(Environment env) {
        return Argument.getStringForArg(name, env);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NodesInContext)) {
            return false;
        }
        NodesInContext other = (NodesInContext) obj;
        return other.name.equals(this.name);
    }

    @Override
    public int hashCode() {
        return 31 * name.hashCode();
    }

    @Override
    public String toString() {
        return "nodesInContext(\"" + name + "\")";
    }
}
