package accrue.query.query;

import accrue.pdg.ProgramDependenceGraph;
import accrue.query.TopLevel;

/**
 * A query is a sequence of lets followed by an expression and evaluates to a
 * Graph
 */
public abstract class Query implements TopLevel<ProgramDependenceGraph> {

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();
    
    @Override
    public abstract String toString();
}
