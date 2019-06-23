package accrue.query.primitive;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import accrue.algorithm.restrict.FindPCNodesRestrictor;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.graph.PDGFactory;
import accrue.pdg.node.AbstractPDGNode;
import accrue.query.bool.BooleanAnd;
import accrue.query.bool.BooleanExpression;
import accrue.query.bool.BooleanNot;
import accrue.query.bool.BooleanOr;
import accrue.query.bool.BooleanVariable;
import accrue.query.util.Environment;
import accrue.util.OrderedPair;

/**
 * The set of input nodes is a boolean formula combining conditional
 * expressions. This Restrictor finds the set of PC nodes for which the given
 * formula is definitely true.
 */
public class FindPCNodes extends PrimitiveExpression {

    /**
     * Nodes will be removed if control dependent on this expression
     */
    private final BooleanExpression b;
    /**
     * true if we are currently looking for TRUE edges from the expression false
     * if we are currently looking for FALSE edges from the expression
     */
    private boolean edgeType;

    /**
     * Create a new findPCNodes expression for the given boolean expression
     * 
     * @param b
     *            Boolean expression for guards
     */
    public FindPCNodes(BooleanExpression b) {
        this.b = b;
        this.edgeType = true;
    }

    /**
     * Create a new findPCNodes expression for the given boolean expression
     * 
     * @param b
     *            Boolean expression for guards
     * @param edgeType
     *            true if we are currently looking for TRUE edges from the
     *            expression false if we are currently looking for FALSE edges
     *            from the expression
     */
    private FindPCNodes(BooleanExpression b, boolean edgeType) {
        this.b = b;
        this.edgeType = edgeType;
    }

    @Override
    public ProgramDependenceGraph evaluate(ProgramDependenceGraph g, Environment env) {
        ProgramDependenceGraph ret = findPCNodes(b, g, env, edgeType);
        System.err.println("\tFound " + ret.vertexSet().size() + " PC nodes");
        return ret;
    }

    /**
     * Find the set of PC nodes that correspond to the boolean expression
     * 
     * @param bexp
     *            boolean expression to get the PC nodes for
     * @param g
     *            Graph we are working in
     * 
     * @param env
     *            current variable environment
     * @param edgeType
     *            true if we are currently looking for TRUE edges from the
     *            expression false if we are currently looking for FALSE edges
     *            from the expression
     * 
     * @return set of PC nodes
     */
    private ProgramDependenceGraph findPCNodes(BooleanExpression bexp, ProgramDependenceGraph g, Environment env, boolean edgeType) {
        switch (bexp.getType()) {
        case VAR:
            BooleanVariable bvar = (BooleanVariable) bexp;
            FindPCNodesRestrictor findPC = getNodeFinder(edgeType);
            return findPC.restrict(g, bvar.evaluate(env), Collections.<AbstractPDGNode> emptySet());
        case NOT:
            // (!b, et) === (b, !et)
            BooleanNot bnot = (BooleanNot) bexp;
            return new FindPCNodes(bnot.getNegated(), false).evaluate(g, env);
        case AND:
            // (b && c, TRUE) === (b, TRUE) intersection (c, TRUE)
            // (b && c, FALSE) === (b, FALSE) union (c, FALSE)
            BooleanAnd band = (BooleanAnd) bexp;
            BooleanExpression c1 = band.getB1();
            BooleanExpression c2 = band.getB2();
            if (edgeType) {
                ProgramDependenceGraph c1True = new FindPCNodes(c1, true).evaluateCached(g, env);
                ProgramDependenceGraph c2True = new FindPCNodes(c2, true).evaluateCached(g, env);
                return intersection(c1True, c2True);
            } else {
                ProgramDependenceGraph c1False = new FindPCNodes(c1, false).evaluateCached(g, env);
                ProgramDependenceGraph c2False = new FindPCNodes(c2, false).evaluateCached(g, env);
                return union(c1False, c2False);
            }
        case OR:
            // (b || c, TRUE) === (b, TRUE) union (c, TRUE)
            // (b || c, FALSE) === (b, FALSE) intersection (c, FALSE)
            BooleanOr bor = (BooleanOr) bexp;
            BooleanExpression d1 = bor.getB1();
            BooleanExpression d2 = bor.getB2();
            if (edgeType) {
                ProgramDependenceGraph d1True = new FindPCNodes(d1, true).evaluateCached(g, env);
                ProgramDependenceGraph d2True = new FindPCNodes(d2, true).evaluateCached(g, env);
                return union(d1True, d2True);
            } else {
                ProgramDependenceGraph d1False = new FindPCNodes(d1, false).evaluateCached(g, env);
                ProgramDependenceGraph d2False = new FindPCNodes(d2, false).evaluateCached(g, env);
                return intersection(d1False, d2False);
            }
        default:
            throw new RuntimeException("Missing case in switch.");
        }
    }

    /**
     * Destructively compute union of two graphs
     * 
     * @param s1
     *            first graph
     * @param s2
     *            second graph
     * @return union of s1 and s2
     */
    private ProgramDependenceGraph union(ProgramDependenceGraph s1, ProgramDependenceGraph s2) {
        return PDGFactory.union(s1, s2);
    }

    /**
     * Destructively compute intersection of two graphs
     * 
     * @param s1
     *            first graph
     * @param s2
     *            second graph
     * @return intersection of s1 and s2
     */
    private ProgramDependenceGraph intersection(ProgramDependenceGraph s1, ProgramDependenceGraph s2) {
        return PDGFactory.graphSubgraph(s1, new LinkedHashSet<>(s2.vertexSet()), new LinkedHashSet<>(s2.edgeSet()));
    }

    /**
     * Get a map from each variable or negation to the set of corresponding PDG
     * nodes
     * 
     * @param bexp
     *            expression to get the map for
     * @param env
     *            current variable environment
     * @return map containing mappings for all variables and negations in bexp
     */
    private Map<BooleanExpression, Set<AbstractPDGNode>> getNodesForVars(BooleanExpression bexp, Environment env) {
        Map<BooleanExpression, Set<AbstractPDGNode>> nodes = new LinkedHashMap<>();
        switch (bexp.getType()) {
        case VAR:
            BooleanVariable bvar = (BooleanVariable) bexp;
            nodes.put(bvar, bvar.evaluate(env));
            return nodes;
        case NOT:
            BooleanNot bnot = (BooleanNot) bexp;
            nodes.putAll(getNodesForVars(bnot.getNegated(), env));
            return nodes;
        case AND:
            BooleanAnd band = (BooleanAnd) bexp;
            nodes.putAll(getNodesForVars(band.getB1(), env));
            nodes.putAll(getNodesForVars(band.getB2(), env));
            return nodes;
        case OR:
            BooleanOr bor = (BooleanOr) bexp;
            nodes.putAll(getNodesForVars(bor.getB1(), env));
            nodes.putAll(getNodesForVars(bor.getB2(), env));
            return nodes;
        default:
            throw new RuntimeException("Missing case in switch.");
        }
    }

    @Override
    public Object getAdditionalCacheKey(Environment env) {
        // need boolean expression, edge type, and values of each boolean variable
        OrderedPair<BooleanExpression, Boolean> p = new OrderedPair<>(b, edgeType);       
        return new OrderedPair<OrderedPair<BooleanExpression,Boolean>, Map<BooleanExpression, Set<AbstractPDGNode>>>(p, getNodesForVars(b, env));        
    }

    /**
     * Get the algorithm used to find PC nodes based on the edge type, et
     * 
     * @param edgeType
     *            if true then the edge type, et, will be TRUE otherwise false
     * 
     * @return algorithm to find PC nodes
     */
    private FindPCNodesRestrictor getNodeFinder(boolean edgeType) {
        if (edgeType) {
            return new FindPCNodesRestrictor(true, false);
        }
        return new FindPCNodesRestrictor(false, true);

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((b == null) ? 0 : b.hashCode());
        result = prime * result + (edgeType ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FindPCNodes other = (FindPCNodes) obj;
        if (b == null) {
            if (other.b != null)
                return false;
        } else if (!b.equals(other.b))
            return false;
        if (edgeType != other.edgeType)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "findPCNodes(" + b + ")";
    }
}
