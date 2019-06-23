package accrue.query.primitive;

import java.util.Set;

import accrue.pdg.node.AbstractPDGNode;
import accrue.query.util.StringMatcher;

/**
 * Primitive that matches strings in nodes
 */
public abstract class PrimitiveNodeMatchingExpression extends PrimitiveExpression {

    /**
     * Default
     */
    private final StringMatcherForNodes m = new StringMatcherForNodes();   
    
    /**
     * Get Nodess matching a given string. What it matches against is defined
     * by the return value of {@link PrimitiveNodeMatchingExpression#getStringToMatch(Object)}.
     * 
     * @param candidates
     *            Candidates to pull matches from
     * @param pattern
     *            String to match against, regular expression are formatted as
     *            "r[regex]"
     * 
     * @return Nodes matching the pattern
     */
    public Set<AbstractPDGNode> getMatches(Set<AbstractPDGNode> candidates, String pattern) {
        return m.getMatches(candidates, pattern);
    }
    
    
    /**
     * Get a string to match against for a node. This is an extension point for
     * primitives that match against Strings in nodes
     * 
     * @param t
     *            Node to get the String for
     * @return String to match against
     */
    public abstract String stringToMatch(AbstractPDGNode t);
    
    /**
     * String matcher for AbstractPDGNode
     */
    private class StringMatcherForNodes extends StringMatcher<AbstractPDGNode> {
        @Override
        public String getStringToMatch(AbstractPDGNode t) {
            return stringToMatch(t);
        }     
    }
    
}
