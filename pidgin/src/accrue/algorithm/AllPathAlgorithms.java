package accrue.algorithm;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

import accrue.pdg.PDGEdge;
import accrue.pdg.PDGPath;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.graph.PDGFactory;
import accrue.pdg.node.AbstractPDGNode;

/**
 * A bunch of algorithms that compute paths and properties about paths. Be
 * careful using any of these as they are usually heuristically based.
 */
public class AllPathAlgorithms {
    /**
     * Get all the paths (with no repeated edges) in the graph that start in at
     * the given source node.
     * 
     * @param pdg
     *            Graph we are looking for path in
     * @param source
     *            source node on the paths
     * @return A set of all paths starting at <code>source</code>
     */
    public static Set<PDGPath> getAllPaths(ProgramDependenceGraph pdg, AbstractPDGNode source) {
        return getAllPathsTarget(pdg, source, null);
    }

    /**
     * Get all the paths (with no repeated edges) in the graph that start with
     * any of a set of given source nodes.
     * 
     * @param pdg
     *            Graph we are looking for path in
     * @param sources
     *            source nodes on the paths
     * @return A set of all paths starting at <code>sources</code>
     */
    public static Set<PDGPath> getAllPaths(ProgramDependenceGraph pdg, Set<AbstractPDGNode> sources) {
        Set<PDGPath> paths = new LinkedHashSet<PDGPath>();
        for (AbstractPDGNode n : sources) {
            paths.addAll(getAllPaths(pdg, n));
        }
        return paths;
    }

    /**
     * Get all the paths (with no repeated edges) in the graph.
     * 
     * @param pdg
     *            Graph we are looking for path in
     *            
     * @return A set of all paths
     */
    public static Set<PDGPath> getAllPaths(ProgramDependenceGraph pdg) {
        return getAllPaths(pdg, pdg.vertexSet());
    }
    
    /**
     * Get all the paths (with no repeated edges) in the graph that start in at
     * source and end at target.
     * 
     * @param pdg
     *            Graph we are looking for path in
     * @param target
     *            target node for the paths
     * @return A set of all paths starting at <code>source</code> ending at
     *         <code>target</code>
     */
    public static Set<PDGPath> getAllBackPathsTarget(ProgramDependenceGraph pdg, AbstractPDGNode target) {
        Set<PDGPath> paths = new LinkedHashSet<PDGPath>();
        for (PDGEdge e : pdg.incomingEdgesOf(target)) {
            PDGPath path = PDGFactory.path(pdg, Collections.singletonList(e));
            getAllPathsDFS(PDGFactory.removeEdge(pdg, e), e.getSource(), null, path, paths, true);
        }
        return paths;
    }
    
    /**
     * Get all the backpaths (with no repeated edges) in the graph that start in at
     * source and end at target.
     * 
     * @param pdg
     *            Graph we are looking for path in
     * @param source
     *            source node on the paths
     * @param target
     *            target node for the paths
     * @return A set of all paths starting at <code>source</code> ending at
     *         <code>target</code>
     */
    public static Set<PDGPath> getAllPathsTarget(ProgramDependenceGraph pdg, AbstractPDGNode source, AbstractPDGNode target) {
        Set<PDGPath> paths = new LinkedHashSet<PDGPath>();
        for (PDGEdge e : pdg.outgoingEdgesOf(source)) {
            PDGPath path = PDGFactory.path(pdg, Collections.singletonList(e));
            getAllPathsDFS(PDGFactory.removeEdge(pdg, e), e.getTarget(), target, path, paths);// XXX inefficient
        }
        return paths;
    }
    
    /**
     * Get all the paths (with no repeated edges) in the graph that start at one
     * of the sources and end at one of the targets
     * 
     * @param pdg
     *            Graph we are looking for path in
     * @param sources
     *            source nodes on the paths
     * @param targets
     *            target nodes for the paths
     * @return A set of all paths starting at <code>sources</code> ending at
     *         <code>targets</code>
     */
    public static Set<PDGPath> getAllPathsTarget(ProgramDependenceGraph pdg, Set<AbstractPDGNode> sources, Set<AbstractPDGNode> targets) {
        Set<PDGPath> paths = new LinkedHashSet<PDGPath>();
        for (AbstractPDGNode source : sources) {
            for (AbstractPDGNode target : targets) {
                paths.addAll(getAllPathsTarget(pdg, source, target));
            }
        }
        return paths;
    }
    

    /**
     * Return a map from node to the number of times that node appears in a
     * path. In this way we can find the most commonly occurring nodes.
     * 
     * @param paths set of paths to count node occurances in
     * 
     * @return Map from node to the number of paths it occurs in
     */
    public static Map<AbstractPDGNode, Integer> getPathCounts(Set<PDGPath> paths) {

        Map<AbstractPDGNode, Integer> map = new LinkedHashMap<AbstractPDGNode, Integer>();
        for (PDGPath p : paths) {
            for (AbstractPDGNode n : new LinkedHashSet<AbstractPDGNode>(p.getNodeList())) {
                Integer count = map.get(n);
                int newCount = count == null ? 1 : count + 1;
                map.put(n, newCount);
            }
        }
        
        return map;
    }
    


    /**
     * helper method to print counts of occurances nodes in the given sets of
     * paths
     * 
     * @param paths
     *            paths to print a count of ocurrances in
     */
    public static void printSortedCounts(Set<PDGPath> paths) {
        printSortedCounts(getPathCounts(paths));
    }

    /**
     * helper method to print counts of occurances nodes in the given sets of
     * paths
     * 
     * @param counts
     *            map of nodes to count we want to print
     */
    public static void printSortedCounts(Map<AbstractPDGNode, Integer> counts) {
        Comparator<AbstractPDGNode> c = new ValueComparator(counts);
        Map<AbstractPDGNode, Integer> sortedMap = new TreeMap<AbstractPDGNode, Integer>(c);
        sortedMap.putAll(counts);
        
//        System.out.println(sortedMap);
        for (Entry<AbstractPDGNode, Integer> e : sortedMap.entrySet()){
            System.out.println(e.getValue() + "=" + e.getKey());
        }
    }

    /**
     * Semi-hack to sort the map from PDGNode to Integer on the value
     */
    private static class ValueComparator implements Comparator<AbstractPDGNode> {

        /**
         * map of node to an integer we want to sort on the latter
         */
        Map<AbstractPDGNode, Integer> m;

        /**
         * Create a comparator that uses the given map to sort based on the
         * value stored at a key
         * 
         * @param m map of node to an integer we want to sort on the latter
         */
        public ValueComparator(Map<AbstractPDGNode, Integer> m) {
            this.m = m;
        }

        // Note: this comparator imposes orderings that are inconsistent with
        // equals.
        @Override
        public int compare(AbstractPDGNode o1, AbstractPDGNode o2) {
            if (m.get(o1) >= m.get(o2)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }
    
    /**
     * Get all the paths from a source to a target (with no duplicate edges)
     * using a depth first search. And add them to the set <code>paths</code>
     * 
     * @param pdg
     *            Program dependence graph to get all the paths in
     * @param source
     *            Source node of all the paths
     * @param target
     *            target node for the paths, if this is null then all paths from
     *            the source are computed
     * 
     * @param incoming
     *            Path up to the source
     * @param paths
     *            the set of paths (with no duplicate edges) from the source
     *            node to the target. The results are added to this set.
     * @param backwards
     *            set to true to be a backwards analysis
     */
    private static void getAllPathsDFS(ProgramDependenceGraph pdg, AbstractPDGNode source, 
            AbstractPDGNode target, PDGPath incoming, Set<PDGPath> paths, boolean backwards) {

        Set<PDGEdge> next = backwards ? pdg.incomingEdgesOf(source) : pdg.outgoingEdgesOf(source);

        // Add the path if the end is the target
        if (target != null) {
            if((!backwards && incoming.getEndVertex().equals(target))
                    || (backwards && incoming.getStartVertex().equals(target))) {
                paths.add(incoming);
            }
        }

        // If target is null then we find all paths from source
        if (target == null && next.isEmpty()) {
            // if we are at the end of a path then add it to the set
            paths.add(incoming);
        }

        for (PDGEdge e : next) {
            // Get an outgoing edge and add that edge to the path
            List<PDGEdge> newEdgeList = new LinkedList<PDGEdge>();
            if (backwards) {
                newEdgeList.add(e);
                newEdgeList.addAll(incoming.getEdgeList());
            } else {
                newEdgeList.addAll(incoming.getEdgeList());
                newEdgeList.add(e);
                        
            }
            PDGPath path = PDGFactory.path(pdg, newEdgeList);

            // Recursive call with the destination node, pdg with the edge
            // removed
            getAllPathsDFS(PDGFactory.removeEdge(pdg, e), backwards ? e.getSource() : e.getTarget(), target, path, paths, backwards);
        }
    }
    
    /**
     * Get all the paths from a source to a target (with no duplicate edges)
     * using a depth first search. And add them to the set <code>paths</code>
     * 
     * @param pdg
     *            Program dependence graph to get all the paths in
     * @param source
     *            Source node of all the paths
     * @param target
     *            target node for the paths, if this is null then all paths from
     *            the source are computed
     * 
     * @param incoming
     *            Path up to the source
     * @param paths
     *            the set of paths (with no duplicate edges) from the source
     *            node to the target. The results are added to this set.
     */
    private static void getAllPathsDFS(ProgramDependenceGraph pdg, AbstractPDGNode source, 
            AbstractPDGNode target, PDGPath incoming, Set<PDGPath> paths) {
         getAllPathsDFS(pdg, source, target,  incoming,  paths, false);
        
    }
    
    /**
     * Expand the path to include any path conditions the nodes in the path are
     * control dependent on
     * 
     * @param pdg
     *            graph the path is contained in
     * @param path
     *            path to expand
     * @return a new PDG including the given path and any control dependencies
     *         for nodes on that path
     */
    public static ProgramDependenceGraph pathAndPathConditions(ProgramDependenceGraph pdg, PDGPath path) {
        List<AbstractPDGNode> targets = path.getNodeList();
        Set<PDGEdge> edges = new LinkedHashSet<PDGEdge>(path.getEdgeList());
        for (AbstractPDGNode target : targets) {
            // Use Breadth First Search back from the target
            Queue<AbstractPDGNode> queue = new ArrayDeque<AbstractPDGNode>();
            queue.add(target);

            Set<PDGEdge> bfsMarked = new HashSet<PDGEdge>();

            AbstractPDGNode current = null;
            while (!queue.isEmpty()) {
                current = queue.poll();
                Set<PDGEdge> dCurrentSet = pdg.incomingEdgesOf(current);
                for (PDGEdge dCurrent : dCurrentSet) {
                    // Only follow back from path conditions
                    if (dCurrent.getSource().getNodeType().isPathCondition()) {
                        if (!bfsMarked.contains(dCurrent)) {
                            bfsMarked.add(dCurrent);
                            queue.add(dCurrent.getSource());
                        }
                    }
                }
            }
            edges.addAll(bfsMarked);
        }
        return PDGFactory.graphFromEdges(edges);
    }
}
