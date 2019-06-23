package accrue.query.expression;

import accrue.pdg.ProgramDependenceGraph;
import accrue.query.util.Environment;

/**
 * Expression that always evaluates to the original PDG
 */
public class OriginalGraph extends Expression {

    /**
     * original PDG
     */
    private final ProgramDependenceGraph pdg;
    /**
     * Get the singleton instance of an OriginalGraph expression
     */
    private static OriginalGraph singleton;

    /**
     * Create a new constant expression
     * 
     * @param pdg
     *            original PDG
     */
    private OriginalGraph(ProgramDependenceGraph pdg) {
        this.pdg = pdg;
    }

    /**
     * Get the singleton instance of this class
     * 
     * @param pdg
     *            original PDG
     * @return Singleton instance of this class
     */
    public static OriginalGraph newInstance(ProgramDependenceGraph pdg) {
        if (singleton == null) {
            singleton = new OriginalGraph(pdg);
        }
        return singleton;
    }

    @Override
    public ProgramDependenceGraph evaluate(Environment env) {
        return pdg;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public int hashCode() {
        return 31 * pdg.hashCode();
    }

    @Override
    public String toString() {
        return "pdg";
    }
}
