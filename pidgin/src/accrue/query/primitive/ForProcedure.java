package accrue.query.primitive;

import java.util.LinkedHashSet;
import java.util.Set;

import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.graph.PDGFactory;
import accrue.pdg.node.AbstractPDGNode;
import accrue.query.util.Argument;
import accrue.query.util.Environment;
import accrue.query.util.StringMatcher;

/**
 * Nodes matching a particular procedure
 */
public class ForProcedure extends PrimitiveExpression {

    /**
     * name to match
     */
    private final Argument<?> name;

    /**
     * nodes in a procedure matching the argument
     * 
     * @param name
     *            name to match
     */
    public ForProcedure(Argument<?> name) {
        this.name = name;

    }

    @Override
    public ProgramDependenceGraph evaluate(ProgramDependenceGraph g, Environment env) {

        String str = Argument.getStringForArg(name, env);

        Set<AbstractPDGNode> matchingNodes;
        if (g.getAllProcedureNames().size() > g.vertexSet().size()) {
            // mode procedures than nodes so use nodes
            matchingNodes = new StringMatcherForNodes().getMatches(g.vertexSet(), str);
        } else {
            Set<String> matches = new StringMatcherForStrings().getMatches(g.getAllProcedureNames(), str);
            matchingNodes = new LinkedHashSet<AbstractPDGNode>();
            for (String s : matches) {
                matchingNodes.addAll(g.getNodesByProcedure(s));
            }
        }

        ProgramDependenceGraph newG = PDGFactory.retainNodes(g, matchingNodes);
        if (newG.isEmpty()) {
            throw new RuntimeException(this 
                    + " evaluated to an empty graph.\nArgument was:\n\n\"" + str + "\"");
        }

        return newG;
    }   
    
    @Override
    public Object getAdditionalCacheKey(Environment env) {
        return Argument.getStringForArg(name, env);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ForProcedure)) {
            return false;
        }
        ForProcedure other = (ForProcedure) obj;
        return other.name.equals(this.name);
    }

    @Override
    public int hashCode() {
        return 13 * name.hashCode();
    }

    @Override
    public String toString() {
        return "forProcedure(" + name + ")";
    }
    
    /**
     * String matcher for AbstractPDGNode
     */
    private static class StringMatcherForStrings extends StringMatcher<String> {
        @Override
        public String getStringToMatch(String s) {
            return s;
        }     
    }
    
    /**
     * String matcher for AbstractPDGNode
     */
    private static class StringMatcherForNodes extends StringMatcher<AbstractPDGNode> {
        @Override
        public String getStringToMatch(AbstractPDGNode t) {
            return t.groupingName();
        }     
    }
}
