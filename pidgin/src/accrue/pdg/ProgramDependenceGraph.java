package accrue.pdg;

import java.util.List;
import java.util.Set;

import accrue.pdg.node.AbstractPDGNode;
import accrue.pdg.node.PDGNodeType;

/**
 * Graph with nodes representing values of variables and expressions and
 * directed edges representing dependencies between those values.
 * 
 * @see accrue.infoflow.analysis.dependency.graph.ProgramDependenceGraph
 */
public interface ProgramDependenceGraph {

    /**
     * Get the set of all edges outgoing from the given node
     * 
     * @param n
     *            node to get the outgoing edges for
     * @return the set of outgoing edges for the given node
     */
    Set<PDGEdge> outgoingEdgesOf(AbstractPDGNode n);

    /**
     * Get the set of all edges incoming into the given node
     * 
     * @param n
     *            node to get the incoming edges for
     * @return the set of incoming edges for the given node
     */
    Set<PDGEdge> incomingEdgesOf(AbstractPDGNode n);

    /**
     * True if this PDG contains the given node
     * 
     * @param n
     *            node to check containment of
     * @return true if the node is in this PDG
     */
    boolean containsVertex(AbstractPDGNode n);

    /**
     * Set of all edges in this PDG
     * 
     * @return all edges in this PDG
     */
    Set<PDGEdge> edgeSet();

    /**
     * Set of all nodes in this PDG
     * 
     * @return all nodes in this PDG
     */
    Set<AbstractPDGNode> vertexSet();

    /**
     * Get the node corresponding to the given ID or null if it is not found
     * 
     * @param id
     *            unique ID to find a node for
     * @return the node corresponding to the ID or null if none is found in this
     *         PDG
     */
    AbstractPDGNode getNodeById(Integer id);

    /**
     * Get the set of edges in this PDG of the given {@link PDGEdgeType}
     * 
     * @param t
     *            type to retrieve edges for
     * @return all edges in this PDG of the given type
     */
    Set<PDGEdge> getEdgesOfType(PDGEdgeType t);

    /**
     * Get the set of nodes in this PDG of the given {@link PDGNodeType}
     * 
     * @param t
     *            type to retrieve nodes for
     * @return all nodes in this PDG of the given type
     */
    Set<AbstractPDGNode> getNodeOfType(PDGNodeType t);
    
    /**
     * Get the set of edges in this PDG that have both nodes of the given
     * {@link PDGNodeType}
     * 
     * @param t
     *            type to retrieve nodes for
     * @return all edges in this PDG where both nodes have the given type
     */
    Set<PDGEdge> getEdgesByNodeType(PDGNodeType t);
    
    /**
     * Get all nodes for a given procedure
     * 
     * @param procedureName
     *            name to get nodes for
     * @return Set of nodes for the given procedure
     */
    Set<AbstractPDGNode> getNodesByProcedure(String procedureName);
    
    /**
     * Get all exit assignment nodes (in the caller) for a given return site
     * 
     * @param id
     *            id for return site
     * @return Set of exit assignment nodes (in the caller) for the given ID
     */
    Set<AbstractPDGNode> getExitAssignmentNodes(Integer id);
    
    /**
     * Get all exit summary nodes (in the callee) for a given return site
     * 
     * @param id
     *            id for return site
     * @return Set of exit summary nodes (in the callee) for the given ID
     */
    Set<AbstractPDGNode> getExitSummaryNodes(Integer id);
       
    /**
     * Get return site for a given exit assignment node
     * 
     * @param node
     *            exit assignment node
     * @return return site ID
     */
    Integer getExitAssignmentSite(AbstractPDGNode node);
    
    /**
     * Get all call site IDs for a given formal assignment node
     * 
     * @param node
     *            formal assignment node
     * @return Set of call site IDs
     */
    Set<Integer> getFormalAssignmentSites(AbstractPDGNode node);
    
    /**
     * Get all caller exit assignment nodes
     * 
     * @return Set of caller exit assignment nodes
     */
    Set<AbstractPDGNode> getAllExitAssignmentNodes();
    
    /**
     * Get all caller formal assignment nodes
     * 
     * @return Set of caller formal assignment nodes
     */
    Set<AbstractPDGNode> getAllFormalAssignmentNodes();
    
    /**
     * Get a set of the names of procedures contained in this graph
     * 
     * @return Set of procedures contained in this graph
     */
    Set<String> getAllProcedureNames();

    /**
     * True if this PDG contains the given edge
     * 
     * @param e
     *            edge to check containment of
     * @return True if this PDG contains the given edge
     */
    boolean containsEdge(PDGEdge e);
    
    /**
     * Get cached results for the given expression and keys. Or null if none is
     * found.
     * 
     * @param expr
     *            Class for expression to get the cached results for
     * @param args
     *            any arguments to the primitive expression
     * @return cached result or null if there is no result in the cache
     */
    ProgramDependenceGraph getCachedResults(Class<?> expr, Object args);

    /**
     * Store result in the cache for the given expression and keys.
     * 
     * @param expr
     *            Class for expression to put results into the cache for
     * @param args
     *            any arguments to the primitive expression
     * @param result
     *            what to store into the cache
     */
    void storeCachedResults(Class<?> expr, ProgramDependenceGraph result, Object args);
    
    /**
     * Clear the cache. This will effect performance of future queries.
     */
    void clearAllCachedResults();

    /**
     * Statistics for the PDG
     * 
     * @return list of info about the PDG
     */
    public List<String> getStats();
    
    /**
     * True if the graph has no nodes or edges
     * 
     * @return True if the graph has no nodes or edges
     */
    public boolean isEmpty();
}
