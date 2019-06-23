package accrue.pdg.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import accrue.pdg.PDGEdge;
import accrue.pdg.PDGEdgeType;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.node.AbstractPDGNode;
import accrue.pdg.node.PDGNodeType;
import accrue.pdg.util.CallSiteEdgeLabel;
import accrue.pdg.util.CallSiteEdgeLabel.SiteType;

/**
 * Graph with nodes representing values of variables and expressions and
 * directed edges representing dependencies between those values.
 * <p>
 * This is a basic immutable implementation of all the PDG functionality, meant
 * to be extended by classes implementing functional modification of this PDG
 * 
 * @see accrue.infoflow.analysis.dependency.graph.ProgramDependenceGraph
 */
public class GraphOriginal extends AbstractGraph implements ProgramDependenceGraph {

    /**
     * Set of nodes in the graph
     */
    private final Set<AbstractPDGNode> nodes;

    /**
     * set of edges in the graph
     */
    private final Set<PDGEdge> edges;
    /**
     * Index of edges stored by type. Lazily craeted
     */
    private final Map<PDGEdgeType, Set<PDGEdge>> edgesByType;

    /**
     * Index of edges where both nodes have a particular node type
     */
    private final Map<PDGNodeType, Set<PDGEdge>> edgesByNodeType;

    /**
     * True if we have initialized the index for {@link PDGEdgeType}
     */
    private boolean edgeTypeIndexValid = false;

    /**
     * Index of nodes stored by type.
     */
    private final Map<PDGNodeType, Set<AbstractPDGNode>> nodesByType;

    /**
     * Index of nodes stored by Procedure name.
     */
    private final Map<String, Set<AbstractPDGNode>> nodesByProcedure;

    /**
     * Return sites for exit assignment nodes
     */
    private final Map<AbstractPDGNode, Integer> idForExitAssignmentNode;

    /**
     * Call site(s) for formal assignment nodes
     */
    private final Map<AbstractPDGNode, Set<Integer>> idsForFormalAssignmentNode;

    /**
     * Exit asssignment nodes (in the caller) for return site
     */
    private final Map<Integer, Set<AbstractPDGNode>> exitAssignmentNodesForID;
    
    /**
     * Exit summary nodes (in the callee) for return site
     */
    private final Map<Integer, Set<AbstractPDGNode>> exitSummaryNodesForID;

    /**
     * All Caller exit assignment nodes
     */
    private final Set<AbstractPDGNode> allExitAssignmentNodes;

    /**
     * All Caller formal assignment nodes
     */
    private final Set<AbstractPDGNode> allFormalAssignmentNodes;

    /**
     * Index of nodes stored by node ID
     */
    private final Map<Integer, AbstractPDGNode> nodesByID;

    /**
     * Index of incoming edges
     */
    private Map<AbstractPDGNode, Set<PDGEdge>> incomingEdgeIndex = null;

    /**
     * Index of outgoing edges
     */
    private Map<AbstractPDGNode, Set<PDGEdge>> outgoingEdgeIndex = null;

    /**
     * Create the {@link PDGEdgeType} to set of edges index
     */
    private void indexEdgeTypes() {
        if (!edgeTypeIndexValid) {
            long start = System.currentTimeMillis();
            for (PDGEdge e : this.edges) {
                edgesByType.get(e.getType()).add(e);
            }
            edgeTypeIndexValid = true;
            System.err.println("\tIndex  types: " + (System.currentTimeMillis() - start));
        }

    }

    /**
     * Create the indices for incoming and outgoing edges of nodes
     */
    private void indexEdges() {
        if (incomingEdgeIndex == null && outgoingEdgeIndex == null) {
            incomingEdgeIndex = new HashMap<AbstractPDGNode, Set<PDGEdge>>();
            outgoingEdgeIndex = new HashMap<AbstractPDGNode, Set<PDGEdge>>();

            long start = System.currentTimeMillis();
            for (PDGEdge e : this.edges) {
                {
                    if (e.getEdgeLabel() != null) {
                        CallSiteEdgeLabel el = e.getEdgeLabel();
                        if (el.getType() == SiteType.ENTRY) {
                            AbstractPDGNode source = e.getSource();
                            Set<Integer> descs = idsForFormalAssignmentNode.get(source);
                            if (descs == null) {
                                descs = new HashSet<>();
                                idsForFormalAssignmentNode.put(source, descs);
                            }
                            descs.add(el.getCallSiteID());
                            allFormalAssignmentNodes.add(source);
                        }
                        if (el.getType() == SiteType.EXIT) {
                            AbstractPDGNode target = e.getTarget();

                            Set<AbstractPDGNode> nodesForDesc = exitAssignmentNodesForID.get(el.getCallSiteID());
                            if (nodesForDesc == null) {
                                nodesForDesc = new HashSet<>();
                                exitAssignmentNodesForID.put(el.getCallSiteID(), nodesForDesc);
                            }

                            if (idForExitAssignmentNode.put(target, el.getCallSiteID()) != null) {
                                throw new RuntimeException("Multiple edges for a single target." +
                                        " Target: " + target + ", edge label: " + el);
                            }
                            nodesForDesc.add(target);
                            allExitAssignmentNodes.add(target);
                            
                            // Set up formal summary nodes
                            Set<AbstractPDGNode> summarynodesForID = exitSummaryNodesForID.get(el.getCallSiteID());
                            if (summarynodesForID == null) {
                                summarynodesForID = new HashSet<>();
                                exitSummaryNodesForID.put(el.getCallSiteID(), summarynodesForID);
                            }
                            summarynodesForID.add(e.getSource());
                        }
                    }

                    AbstractPDGNode s = e.getSource();
                    if (!this.nodes.contains(s)) {
                        throw new RuntimeException("All edges should have their sources and targets in the node set");
                    }
                    Set<PDGEdge> out = outgoingEdgeIndex.get(s);
                    if (out == null) {
                        out = new HashSet<PDGEdge>();
                        outgoingEdgeIndex.put(s, out);
                    }
                    out.add(e);

                    AbstractPDGNode t = e.getTarget();
                    if (!this.nodes.contains(t)) {
                        throw new RuntimeException("All edges should have their sources and targets in the node set");
                    }
                    Set<PDGEdge> in = incomingEdgeIndex.get(t);
                    if (in == null) {
                        in = new HashSet<PDGEdge>();
                        incomingEdgeIndex.put(t, in);
                    }
                    in.add(e);

                    if (t.getNodeType() == s.getNodeType()) {
                        Set<PDGEdge> edgesForType = edgesByNodeType.get(t.getNodeType());
                        edgesForType.add(e);
                    }
                }

            }
            System.err.println("\tIndexed " + edges.size() + " edges: " + (System.currentTimeMillis() - start));
            System.err.println("\t" + allFormalAssignmentNodes.size() + " entry nodes");
            System.err.println("\t" + allExitAssignmentNodes.size() + " exit nodes");
        }
    }

    /**
     * Create a PDG from a collection of nodes and edges. Nodes in the
     * <code>nodes</code> set and nodes on edges in <code>edges</code> will be
     * added. If the <code>nodes</code> is null then only nodes on the edges
     * will be added to the PDG. If the set of <code>edges</code> is null then
     * no edges will be added to the PDG
     * 
     * @param nodes_
     *            nodes in the new PDG. Should null or contain all of the nodes
     *            of the edges.
     * @param edges_
     *            edges in the new PDG, if null there will be no edges in the
     *            PDG
     */
    public GraphOriginal(Set<AbstractPDGNode> nodes_, Set<PDGEdge> edges_) {
        nodesByID = new HashMap<Integer, AbstractPDGNode>();
        nodesByProcedure = new LinkedHashMap<String, Set<AbstractPDGNode>>();
        idsForFormalAssignmentNode = new HashMap<AbstractPDGNode, Set<Integer>>();
        idForExitAssignmentNode = new HashMap<AbstractPDGNode, Integer>();
        exitAssignmentNodesForID = new HashMap<Integer, Set<AbstractPDGNode>>();
        exitSummaryNodesForID = new HashMap<Integer, Set<AbstractPDGNode>>();
        allFormalAssignmentNodes = new HashSet<>();
        allExitAssignmentNodes = new HashSet<>();

        this.nodesByType = new LinkedHashMap<PDGNodeType, Set<AbstractPDGNode>>();
        this.edgesByNodeType = new LinkedHashMap<PDGNodeType, Set<PDGEdge>>();
        for (PDGNodeType t : PDGNodeType.values()) {
            Set<AbstractPDGNode> s = new HashSet<AbstractPDGNode>();
            nodesByType.put(t, s);

            Set<PDGEdge> e = new HashSet<PDGEdge>();
            edgesByNodeType.put(t, e);
        }

        this.edgesByType = new LinkedHashMap<PDGEdgeType, Set<PDGEdge>>();
        for (PDGEdgeType t : PDGEdgeType.values()) {
            Set<PDGEdge> s = new HashSet<PDGEdge>();
            edgesByType.put(t, s);
        }

        this.edges = edges_ == null ? Collections.<PDGEdge> emptySet() : edges_;
        if (nodes_ != null) {
            this.nodes = nodes_;
            for (AbstractPDGNode n : nodes_) {
                indexNode(n);
            }
        }
        else {
            // no nodes, we need to construct it from edges
            this.nodes = new LinkedHashSet<AbstractPDGNode>();
            for (PDGEdge e : this.edges) {
                if (this.nodes.add(e.getSource())) {
                    indexNode(e.getSource());
                }
                if (this.nodes.add(e.getTarget())) {
                    indexNode(e.getTarget());
                }
            }
        }
    }

    /**
     * Add the given node to the node indices
     * 
     * @param v
     *            node to add to the indices
     */
    private void indexNode(AbstractPDGNode v) {
        Set<AbstractPDGNode> nodesOfType = nodesByType.get(v.getNodeType());
        nodesOfType.add(v);
        Set<AbstractPDGNode> nodesInProcedure = nodesByProcedure.get(v.getProcedureName());
        if (nodesInProcedure == null) {
            nodesInProcedure = new LinkedHashSet<AbstractPDGNode>();
            nodesByProcedure.put(v.getProcedureName(), nodesInProcedure);
        }
        nodesInProcedure.add(v);
        nodesByID.put(v.getNodeId(), v);
    }

    @Override
    public AbstractPDGNode getNodeById(Integer nodeId) {
        return nodesByID.get(nodeId);
    }

    @Override
    public GraphOriginal clone() {
        Set<PDGEdge> newEdges = new LinkedHashSet<PDGEdge>(edgeSet());
        Set<AbstractPDGNode> newNodes = new LinkedHashSet<AbstractPDGNode>(vertexSet());
        return new GraphOriginal(newNodes, newEdges);
    }

    @Override
    public Set<PDGEdge> getEdgesOfType(PDGEdgeType type) {
        indexEdgeTypes();
        return Collections.unmodifiableSet(edgesByType.get(type));
    }

    @Override
    public Set<AbstractPDGNode> getNodeOfType(PDGNodeType type) {
        return Collections.unmodifiableSet(nodesByType.get(type));
    }

    @Override
    public Set<PDGEdge> getEdgesByNodeType(PDGNodeType type) {
        return Collections.unmodifiableSet(edgesByNodeType.get(type));
    }


    @Override
    public Set<AbstractPDGNode> getNodesByProcedure(String procedureName) {
        return Collections.unmodifiableSet(nodesByProcedure.get(procedureName));
    }

    @Override
    public Integer getExitAssignmentSite(AbstractPDGNode n) {
        return idForExitAssignmentNode.get(n);
    }

    @Override
    public Set<Integer> getFormalAssignmentSites(AbstractPDGNode n) {
        Set<Integer> descs = idsForFormalAssignmentNode.get(n);
        if (descs == null) {
            return null;
        }
        return Collections.unmodifiableSet(descs);
    }

    @Override
    public Set<AbstractPDGNode> getExitAssignmentNodes(Integer desc) {
        Set<AbstractPDGNode> nodes = exitAssignmentNodesForID.get(desc);
        if (nodes == null) {
            return null;
        }
        return Collections.unmodifiableSet(nodes);
    }

    @Override
    public Set<AbstractPDGNode> getExitSummaryNodes(Integer desc) {
        Set<AbstractPDGNode> nodes = exitSummaryNodesForID.get(desc);
        if (nodes == null) {
            return null;
        }
        return Collections.unmodifiableSet(nodes);
    }

    @Override
    public Set<AbstractPDGNode> getAllFormalAssignmentNodes() {
        return Collections.unmodifiableSet(allFormalAssignmentNodes);
    }

    @Override
    public Set<AbstractPDGNode> getAllExitAssignmentNodes() {
        return Collections.unmodifiableSet(allExitAssignmentNodes);
    }

    @Override
    public Set<String> getAllProcedureNames() {
        return Collections.unmodifiableSet(nodesByProcedure.keySet());
    }

    @Override
    public Set<PDGEdge> outgoingEdgesOf(AbstractPDGNode n) {
        indexEdges();
        Set<PDGEdge> s = outgoingEdgeIndex.get(n);
        return s == null ? Collections.<PDGEdge> emptySet() : s;
    }

    @Override
    public Set<PDGEdge> incomingEdgesOf(AbstractPDGNode n) {
        indexEdges();
        Set<PDGEdge> s = incomingEdgeIndex.get(n);
        return s == null ? Collections.<PDGEdge> emptySet() : s;
    }

    @Override
    public boolean containsVertex(AbstractPDGNode n) {
        return this.nodes.contains(n);
    }

    @Override
    public Set<PDGEdge> edgeSet() {
        return this.edges;
    }

    @Override
    public Set<AbstractPDGNode> vertexSet() {
        return this.nodes;
    }
}
