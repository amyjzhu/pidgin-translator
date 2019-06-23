package accrue.query.cheat;

import java.util.LinkedHashSet;
import java.util.Set;

import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.graph.PDGFactory;
import accrue.pdg.node.AbstractPDGNode;
import accrue.query.expression.Variable;
import accrue.query.primitive.PrimitiveExpression;
import accrue.query.util.Argument;
import accrue.query.util.Environment;

/**
 * Get a node using its unique ID, this will be different if a new PDG is
 * generated
 */
public class NodeByID extends PrimitiveExpression {

    /**
     * ID to get
     */
    private final Argument<?> id;

    /**
     * Get a node using its unique ID
     * 
     * @param id
     *            ID to get
     */
    public NodeByID(Argument<?> id) {
        this.id = id;
    }

    @Override
    public ProgramDependenceGraph evaluate(ProgramDependenceGraph g, Environment env) {
        Integer idToFind = getNodeID(env);

        Set<AbstractPDGNode> matching = new LinkedHashSet<AbstractPDGNode>();
        for (AbstractPDGNode n : g.vertexSet()) {
            if (n.getNodeId() == idToFind.intValue()) {
                matching.add(n);
                break;
            }
        }

        ProgramDependenceGraph newG = PDGFactory.retainNodes(g, matching);
        if (newG.isEmpty()) {
            throw new RuntimeException(this 
                    + " evaluated to an empty graph.\nArgument was:\n\n\"" + idToFind + "\"");
        }

        return newG;
    }

    /**
     * Get the node ID
     * 
     * @param env
     *            Variable environment
     * @return node ID
     */
    private Integer getNodeID(Environment env) {
        Integer nodeID;
        if (id.isAbsent()) {
            nodeID = null;
        } else if (id.value() instanceof Integer) {
            nodeID = (Integer) id.value();
        } else if (id.value() instanceof Variable) {
            nodeID = ((Variable) id.value()).evaluateInteger(env);
        } else {
            throw new IllegalArgumentException("depth is the wrong type: " + id.value().getClass() + " value is " + id.value());
        }

        return nodeID;
    }

    @Override
    public Object getAdditionalCacheKey(Environment env) {
        return getNodeID(env);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NodeByID)) {
            return false;
        }
        NodeByID other = (NodeByID) obj;
        return other.id.equals(this.id);
    }

    @Override
    public int hashCode() {
        return 37 * id.hashCode();
    }

    @Override
    public String toString() {
        return "nodeByID(" + id + ")";
    }

}
