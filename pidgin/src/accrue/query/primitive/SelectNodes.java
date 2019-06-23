package accrue.query.primitive;

import accrue.query.expression.Variable;
import accrue.query.util.Argument;
import accrue.query.util.Environment;

/**
 * Expression to get nodes of a particular type
 */
public class SelectNodes extends PrimitiveExpression {

    /**
     * node type to get
     */
    private Argument<?> nt;

    /**
     * Expression to get nodes of a particular type
     * 
     * @param nt
     *            node type to get
     */
    public SelectNodes(Argument<?> nt) {
        this.nt = nt;
    }

    public Argument<?> getNodeType() {
        return nt;
    }

    //    @Override
//    public ProgramDependenceGraph evaluate(ProgramDependenceGraph g, Environment env) {
//        PDGNodeType t = getNodeType(env);
//        return PDGFactory.graphSubgraph(g, g.getNodeOfType(t), g.getEdgesByNodeType(t));
//    }
//
//    /**
//     * Get the node type
//     *
//     * @param env
//     *            current variable environment
//     * @return node type
//     */
//    private PDGNodeType getNodeType(Environment env) {
//        PDGNodeType type;
//        if (nt.value() instanceof PDGNodeType) {
//            type = (PDGNodeType) nt.value();
//        } else if (nt.value() instanceof Variable) {
//            type = ((Variable) nt.value()).evaluateNodeType(env);
//        } else {
//            throw new IllegalArgumentException("Argument is the wrong type in " + this + " Expected: PDGNodeType, Got: " + nt.value().getClass() + ", Value: " + nt.value());
//        }
//
//        return type;
//    }
//
//    @Override
//    public Object getAdditionalCacheKey(Environment env) {
//        return getNodeType(env);
//    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SelectNodes)) {
            return false;
        }
        SelectNodes other = (SelectNodes) obj;
        return other.nt.equals(this.nt);
    }

    @Override
    public int hashCode() {
        return 17 * nt.hashCode();
    }

    @Override
    public String toString() {
        return "selectNodes(" + nt + ")";
    }
}
