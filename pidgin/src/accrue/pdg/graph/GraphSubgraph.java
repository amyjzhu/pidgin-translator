package accrue.pdg.graph;

import java.util.Collections;
import java.util.Set;

import accrue.pdg.PDGEdge;
import accrue.pdg.PDGEdgeType;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.node.AbstractPDGNode;
import accrue.pdg.node.PDGNodeType;
import accrue.util.SetIntersect;

/**
 * A functional representation of a graph restricted to the specified nodes and
 * edges
 * 
 * @author snchong
 * 
 */
public class GraphSubgraph extends AbstractGraph implements ProgramDependenceGraph {

    /**
     * nodes in the subgraph
     */
    private final Set<AbstractPDGNode> nodes;
    /**
     * edges in the subgraph
     */
    private final Set<PDGEdge> edges;
    /**
     * original PDG this is a subgraph allowing reuse of data structures from
     * this graph
     */
    private final ProgramDependenceGraph orig;

    /**
     * Create a subgraph of the given graph containing only the given nodes and
     * edges
     * 
     * @param pdg
     *            original graph this is a subgraph of
     * @param nodes
     *            nodes in the subgraph
     * @param edges
     *            edges in the subgraph
     */
    public GraphSubgraph(ProgramDependenceGraph pdg, Set<AbstractPDGNode> nodes, Set<PDGEdge> edges) {
        this.orig = pdg;
        this.nodes = nodes;
        this.edges = edges;
    }

    @Override
    public Set<PDGEdge> outgoingEdgesOf(AbstractPDGNode n) {
        if (this.nodes.contains(n)) {
            return new SetIntersect<PDGEdge>(this.orig.outgoingEdgesOf(n), this.edges, false);
        }
        return Collections.emptySet();
    }

    @Override
    public Set<PDGEdge> incomingEdgesOf(AbstractPDGNode n) {
        if (this.nodes.contains(n)) {
            return new SetIntersect<PDGEdge>(this.orig.incomingEdgesOf(n), this.edges, false);
        }
        return Collections.emptySet();
    }

    @Override
    public Set<AbstractPDGNode> getExitSummaryNodes(Integer desc) {
        return new SetIntersect<AbstractPDGNode>(this.orig.getExitSummaryNodes(desc), this.nodes, false);
    }

    @Override
    public Set<AbstractPDGNode> getExitAssignmentNodes(Integer desc) {
        return new SetIntersect<AbstractPDGNode>(this.orig.getExitAssignmentNodes(desc), this.nodes, false);
    }

    @Override
    public Set<Integer> getFormalAssignmentSites(AbstractPDGNode node) {
        if (nodes.contains(node)) {
            return orig.getFormalAssignmentSites(node);
        }
        return Collections.emptySet();
    }

    @Override
    public Integer getExitAssignmentSite(AbstractPDGNode node) {
        if (nodes.contains(node)) {
            return orig.getExitAssignmentSite(node);
        }
        return null;
    }

    @Override
    public Set<PDGEdge> edgeSet() {
        return this.edges;
    }

    @Override
    public Set<AbstractPDGNode> vertexSet() {
        return nodes;
    }

    @Override
    public AbstractPDGNode getNodeById(Integer id) {
        AbstractPDGNode n = orig.getNodeById(id);
        if (this.nodes.contains(n)) {
            return n;
        }
        return null;
    }

    @Override
    public Set<PDGEdge> getEdgesOfType(PDGEdgeType t) {
        return new SetIntersect<PDGEdge>(this.orig.getEdgesOfType(t), this.edges, false);
    }

    @Override
    public Set<PDGEdge> getEdgesByNodeType(PDGNodeType t) {
        return new SetIntersect<PDGEdge>(this.orig.getEdgesByNodeType(t), this.edges, false);
    }

    @Override
    public Set<AbstractPDGNode> getNodeOfType(PDGNodeType t) {
        return new SetIntersect<AbstractPDGNode>(this.orig.getNodeOfType(t), this.nodes, false);
    }

    @Override
    public Set<String> getAllProcedureNames() {
        return this.orig.getAllProcedureNames();
    }

    @Override
    public Set<AbstractPDGNode> getNodesByProcedure(String procedureName) {
        return new SetIntersect<AbstractPDGNode>(this.orig.getNodesByProcedure(procedureName), this.nodes, false);
    }

    @Override
    public Set<AbstractPDGNode> getAllFormalAssignmentNodes() {
        return new SetIntersect<AbstractPDGNode>(this.orig.getAllFormalAssignmentNodes(), this.nodes, false);
    }

    @Override
    public Set<AbstractPDGNode> getAllExitAssignmentNodes() {
        return new SetIntersect<AbstractPDGNode>(this.orig.getAllExitAssignmentNodes(), this.nodes, false);
    }
}
