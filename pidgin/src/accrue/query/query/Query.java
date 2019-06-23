package accrue.query.query;

import accrue.query.TopLevel;

/**
 * A query is a sequence of lets followed by an expression and evaluates to a
 * Graph
 */
// TODO used to implement TopLevel<ProgramDependenceGraph>
public abstract class Query implements TopLevel<Object> {

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();
    
    @Override
    public abstract String toString();
}
