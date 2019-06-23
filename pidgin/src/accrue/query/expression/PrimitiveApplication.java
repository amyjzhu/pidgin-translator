package accrue.query.expression;

import accrue.pdg.ProgramDependenceGraph;
import accrue.query.primitive.PrimitiveExpression;
import accrue.query.util.Environment;

/**
 * Evaluate a primitive using the DOT notation as in E.PE
 * <p>
 * E is evaluated to G and PE is evaluated in G
 */
public class PrimitiveApplication extends Expression {

    /**
     * Graph this will evaluate to a subgraph of
     */
    private final Expression baseGraph;
    /**
     * Primitive expression to evaluate
     */
    private final PrimitiveExpression primitive;

    /**
     * Create a new Expression for e.pe
     * 
     * @param e
     *            expression to evaulate pe in
     * @param pe
     *            primitive expression to evaluate
     */
    public PrimitiveApplication(Expression e, PrimitiveExpression pe) {
        this.baseGraph = e;
        this.primitive = pe;
    }

    @Override
    public ProgramDependenceGraph evaluate(Environment env) {
        ProgramDependenceGraph g = baseGraph.evaluate(env);
        return primitive.evaluateCached(g, env);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PrimitiveApplication)) {
            return false;
        }
        PrimitiveApplication other = (PrimitiveApplication) obj;

        return other.baseGraph.equals(other.baseGraph) && other.primitive.equals(this.primitive);
    }

    @Override
    public int hashCode() {
        return 31 * baseGraph.hashCode() + 29 * primitive.hashCode();
    }

    @Override
    public String toString() {
        return baseGraph.toString() + "." + primitive.toString();
    }
}