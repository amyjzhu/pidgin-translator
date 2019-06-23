package accrue.query.policy;

import accrue.pdg.ProgramDependenceGraph;
import accrue.query.query.Query;
import accrue.query.util.Environment;

/**
 * Check whether a Query results in an empty graph
 */
public class IsEmpty extends Policy {

    /**
     * Create a new policy
     * 
     * @param q
     *            query to check
     */
    public IsEmpty(Query q) {
        super(q);
    }

    @Override
    protected boolean assertion(Query q, Environment c) {
        ProgramDependenceGraph pdg = q.evaluate(c);
        return pdg.isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IsEmpty)) {
            return false;
        }
        return ((IsEmpty) obj).q.equals(this.q);
    }

    @Override
    public int hashCode() {
        return 37 * q.hashCode();
    }

    @Override
    public String toString() {
        return q + " is empty";
    }
}
