package accrue.query.primitive;

import accrue.query.util.Argument;
import accrue.query.util.Environment;

/**
 * Get all edges of a particular type
 */
public class SelectEdges extends PrimitiveExpression {

    /**
     * edge type to get
     */
    private final Argument<?> et;

    /**
     * Expression to get edges of a particular type
     * 
     * @param et
     *            edge type to get
     */
    public SelectEdges(Argument<?> et) {
        this.et = et;
    }

//    @Override
//    public ProgramDependenceGraph evaluate(ProgramDependenceGraph g, Environment env) {
//        return PDGFactory.retainEdges(g, g.getEdgesOfType(Argument.getEdgeTypeForArg(et, env)));
//    }
//
//    @Override
//    public Object getAdditionalCacheKey(Environment env) {
//        return Argument.getEdgeTypeForArg(et, env);
//    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SelectEdges)) {
            return false;
        }
        SelectEdges other = (SelectEdges) obj;
        return other.et.equals(this.et);
    }

    @Override
    public int hashCode() {
        return 17 * et.hashCode();
    }

    @Override
    public String toString() {
        return "selectEdges(" + et + ")";
    }
}
