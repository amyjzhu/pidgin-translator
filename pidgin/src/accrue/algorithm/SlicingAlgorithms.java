package accrue.algorithm;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import accrue.pdg.PDGEdge;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.graph.PDGFactory;
import accrue.pdg.node.AbstractPDGNode;

/**
 * Utility methods for certain graph common algorithms
 */
public class SlicingAlgorithms {	
    /**
     * Get a graph for the incoming edges at most distance, <code>depth</code>,
     * from a set of nodes. If depth is null then it is considered to be the
     * maximum depth in the graph.
     * 
     * @param pdg
     *            program dependence graph we are investigating
     * @param nodes
     *            nodes to get the incoming edges for
     * @param depth
     *            max distance from the set of nodes we want edges at, if null
     *            then it is considered to be the maximum depth of the graph
     * @return Graph containing all nodes and edges at most <code>depth</code>
     *         away from a node in the set nodes. If depth is null then it is
     *         the maximum depth of the graph
     */
	public static ProgramDependenceGraph backwardSlice(ProgramDependenceGraph pdg, Set<AbstractPDGNode> nodes, Integer depth) {
	    return slice(pdg, nodes, depth, false);
	}

    /**
     * Get the outgoing edges at most distance, <code>depth</code>, from a set
     * of nodes.
     * 
     * @param pdg
     *            program dependence graph we are investigating
     * @param nodes
     *            nodes to get the incoming edges for
     * @param depth
     *            max distance from the set of nodes we want edges at
     * @return Subgraph containing edges at most <code>depth</code> away from a
     *         node in the set nodes
     */
    public static ProgramDependenceGraph forwardSlice(ProgramDependenceGraph pdg, Set<AbstractPDGNode> nodes, Integer depth) {  
        return slice(pdg, nodes, depth, true);        
    }

    /**
     * Compute a reachability slice from the nodes in <code>nodes</code>
     * 
     * @param pdg
     *            Graph to slice in
     * @param nodes
     *            nodes to slice from
     * @param depth
     *            depth to slice to or <code>null</code> if unbounded
     * @param forwards
     *            true if this is a forward slice false if it is a backward
     *            slice
     * @return a new PDG containing any nodes or edges in the slice
     */
    private static ProgramDependenceGraph slice(ProgramDependenceGraph pdg, Set<AbstractPDGNode> nodes, Integer depth, boolean forwards) {  
        if (depth == null) {
            return slice(pdg, nodes, forwards);
        }
        
        Set<PDGEdge> newEdges = new LinkedHashSet<PDGEdge>();
        Map<AbstractPDGNode, Integer> visitedMaxDepth = new LinkedHashMap<AbstractPDGNode, Integer>();
        for (AbstractPDGNode n : nodes) {
            if (pdg.containsVertex(n)) {
                slice(pdg, n, depth, visitedMaxDepth, newEdges, forwards);
            }
        }
        return PDGFactory.graphSubgraph(pdg, visitedMaxDepth.keySet(), newEdges);     
    }
    
    /**
     * Recursively compute a slice from <code>base</code> up to a depth of
     * <code>depth</code>. All nodes that are at most depth from the base node
     * will get added to <code>visitedMaxDepth</code> and any edges followed to
     * get to those nodes will be added to <code>newEdges</code>
     * 
     * @param pdg
     *            Graph the slicing occurs in
     * @param base
     *            slicing base node
     * @param depth
     *            max distance from the base
     * @param visitedMaxDepth
     *            Any node within <code>depth</code> from the base will be added
     *            to the keyset of this map. This is a map from nodes we have
     *            already visited to the max depth they were visited at. (If a
     *            node is visited with depth 2 and again with depth 4 we need to
     *            re-process, but if it is visited again with depth 2 we do not)
     * @param newEdges
     *            edges at most distance <code>depth</code> from the base
     * @param forwards
     *            true if this is a forward slice false if it is a backward
     *            slice
     */
    private static void slice(ProgramDependenceGraph pdg, AbstractPDGNode base, int depth, Map<AbstractPDGNode, Integer> visitedMaxDepth, Set<PDGEdge> newEdges, boolean forwards) {

        Integer oldMaxDepth;
        if ((oldMaxDepth = visitedMaxDepth.get(base)) == null) {
            // not yet visited
            visitedMaxDepth.put(base, depth);
        } else if (depth > oldMaxDepth) {
            // visiting with a bigger depth
            visitedMaxDepth.put(base, depth);
        } else {
            // already visited this node at this depth or a bigger depth.
            return;
        }
        
        if (depth == 0) {
            // no need to recurse.
            return;
        }

        for (PDGEdge e : forwards ? pdg.outgoingEdgesOf(base) : pdg.incomingEdgesOf(base)) {
            newEdges.add(e);
            slice(pdg, forwards ? e.getTarget() : e.getSource(), depth - 1, visitedMaxDepth, newEdges, forwards);
        }
    }
    
    /**
     * Get a subgraph containing any node that is reachable from a node in the
     * given set
     * 
     * @param pdg
     *            program dependence graph we are investigating
     * @param nodes
     *            nodes to get the incoming edges for * @param forwards true if
     *            this is a forward slice false if it is a backward slice
     * @return Set of edges at most <code>depth</code> away from a node in the
     *         set nodes
     */
    private static ProgramDependenceGraph slice(ProgramDependenceGraph pdg, Set<AbstractPDGNode> nodes, boolean forwards) {
        Set<AbstractPDGNode> visited = new LinkedHashSet<AbstractPDGNode>();
        Queue<AbstractPDGNode> q = new LinkedBlockingQueue<AbstractPDGNode>();
        for (AbstractPDGNode n : nodes) {
            if (pdg.containsVertex(n)) {
                q.add(n);
                visited.add(n);
            }
        }

        Set<PDGEdge> edges = new LinkedHashSet<PDGEdge>();

        long time1 = System.currentTimeMillis();
        while (!q.isEmpty()) {
            AbstractPDGNode n = q.poll();
            for (PDGEdge e : forwards ? pdg.outgoingEdgesOf(n) : pdg.incomingEdgesOf(n)) {
                AbstractPDGNode s = forwards ? e.getTarget() : e.getSource();
                edges.add(e);
                if (visited.add(s)) {
                    q.add(s);
                }
            }
        }
        long time2 = System.currentTimeMillis();
        ProgramDependenceGraph ret = PDGFactory.graphSubgraph(pdg, visited, edges);
        long time3 = System.currentTimeMillis();
        System.err.println("Slice: " + (time2 - time1));
        System.err.println("    new PDG      : " + (time3 - time2));

        return ret;
    }    
}
