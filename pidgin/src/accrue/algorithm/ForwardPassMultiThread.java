package accrue.algorithm;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import accrue.pdg.PDGEdge;
import accrue.pdg.PDGEdgeType;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.node.AbstractPDGNode;

/**
 * Takes a set of nodes and computes a graph that depends on those nodes in some
 * way. Subclasses must implement
 * {@link ForwardPassMultiThread#computeResultFromDependencies(AbstractPDGNode, Map)} Which
 * takes a node and all nodes that the node depends on, and returns whether that
 * node is part of the result graph based on its dependencies.
 * <p>
 * This class manages the work queue to ensure that results are recomputed only
 * if the results for dependencies have changed.
 * 
 * @param <T>
 *            Type of the results of the pass
 */
public abstract class ForwardPassMultiThread<T> {

    /**
     * Memoization table for results of the work queue algorithm
     */
    private final Map<AbstractPDGNode, T> resultMemo = new ConcurrentHashMap<AbstractPDGNode, T>(16,
            0.75f,
            numThreads());

    /**
     * Set of nodes that are currently in the queue. Used to prevent the same
     * node from being added multiple times.
     */
    private final Set<AbstractPDGNode> enqueuedNodes =
            Collections.newSetFromMap(new ConcurrentHashMap<AbstractPDGNode, Boolean>(16,
                    0.75f,
                    numThreads()));

    /**
     * Set of starting nodes
     */
    private final Set<AbstractPDGNode> inputSet;

    /**
     * Graph to compute in
     */
    private final ProgramDependenceGraph graph;

    /**
     * number of available threads
     * 
     * @return threads available
     */
    private int numThreads() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * Create a new forward pass for the given initial graph and start nodes
     * 
     * @param inputPDG
     *            graph to compute in
     * @param inputSet
     *            set of start nodes
     */
    public ForwardPassMultiThread(ProgramDependenceGraph inputPDG, Set<AbstractPDGNode> inputSet) {
        this.inputSet = inputSet;
        this.graph = inputPDG;
    }
    
    /**
     * Start the initial threads
     */
    public void computeResult() {
        // initialize the Q
        enqueuedNodes.addAll(inputSet);
        
        // Kick off the initial tasks
        ForkJoinPool pool = new ForkJoinPool();
        for (AbstractPDGNode n : inputSet) {
            pool.invoke(new ForwardPassTask(n));
        }
    }

    /**
     * Get the graph resulting from running the pass
     * 
     * @return subgraph of input graph
     */
    public ProgramDependenceGraph getResult() {
        return produceGraph(graph, resultMemo);
    }

    /**
     * Produce the final graph from the results of the analysis pass
     * 
     * @param originalPDG
     *            Program dependence graph before running the analysis
     * @param results
     *            Results of the analysis for the visited nodes
     * @return new {@link ProgramDependenceGraph} based on the results
     */
    protected abstract ProgramDependenceGraph produceGraph(ProgramDependenceGraph originalPDG,
            Map<AbstractPDGNode, T> results);

    /**
     * Given a node in a particular PDG compute the sources and
     * {@link PDGEdgeType} for all incoming edges
     * 
     * @param current
     *            node to compute the dependencies for
     * @param pdg
     *            we want to compute the dependencies in
     * @return map from dependency to the type of edge the dependency occurs on
     */
    private Map<AbstractPDGNode, PDGEdgeType> getIncomingEdgeSources(AbstractPDGNode current, ProgramDependenceGraph pdg) {
        Map<AbstractPDGNode, PDGEdgeType> m = new LinkedHashMap<AbstractPDGNode, PDGEdgeType>();
        Set<PDGEdge> incomingEdges = pdg.incomingEdgesOf(current);
        for (PDGEdge e : incomingEdges) {
            m.put(e.getSource(), e.getType());
        }
        return m;
    }

    /**
     * Given a node in a particular PDG compute the targets and for outgoing
     * edges
     * 
     * @param current
     *            node to compute the targets for
     * @param pdg
     *            we want to compute the targets in
     * @return targets on edges leaving current
     */
    private Set<AbstractPDGNode> getOutgoingEdgeTargets(AbstractPDGNode current, ProgramDependenceGraph pdg) {
        Set<AbstractPDGNode> m = new LinkedHashSet<AbstractPDGNode>();
        Set<PDGEdge> outgoingEdges = pdg.outgoingEdgesOf(current);
        for (PDGEdge e : outgoingEdges) {
            m.add(e.getTarget());
        }
        return m;
    }

    /**
     * If no results have been computed for a node in the input set then add
     * that node to the work queue
     * 
     * @param nodesToCheck
     *            nodes to possibly add to the work queue
     * @return The set of nodes for which no results were found
     */
    private Set<AbstractPDGNode> addUnprocessedToQ(Set<AbstractPDGNode> nodesToCheck) {
        Set<AbstractPDGNode> missingDeps = new HashSet<AbstractPDGNode>();
        for (AbstractPDGNode d : nodesToCheck) {
            if (!resultMemo.containsKey(d)) {
                missingDeps.add(d);
                addToQ(d);
            }
        }
        return missingDeps;
    }

    /**
     * Add nodes that are not already in the queue to the queue and return the
     * added nodes
     * 
     * @param nodesToCheck
     *            nodes to possibly add to the work queue
     * @return The set of nodes added to the queue
     */
    private Set<AbstractPDGNode> addToQ(Set<AbstractPDGNode> nodesToCheck) {
        Set<AbstractPDGNode> added = new HashSet<AbstractPDGNode>();
        for (AbstractPDGNode d : nodesToCheck) {
            if (addToQ(d)) {
                added.add(d);
            }
        }
        return added;
    }

    /**
     * Add node to the queue, return true if node was not already in the queue
     * 
     * @param node
     *            node to possibly add
     * @return true if added, false otherwise
     */
    private boolean addToQ(AbstractPDGNode node) {
        return enqueuedNodes.add(node);
    }

    /**
     * Compute the results for a node after results have been computed for its
     * dependencies
     * 
     * @param current
     *            node to compute results for
     * @param dependencies
     *            dependencies for the current node whose results have already
     *            been computed
     * @return The results for the current node
     */
    protected abstract T computeResultFromDependencies(AbstractPDGNode current, Map<AbstractPDGNode, PDGEdgeType> dependencies);

    /**
     * Get the result for the given node
     * 
     * @param node
     *            PDG node to get the result for
     * 
     * @return Whether the node is in the result set or not (this may change if
     *         its dependencies are later added or removed from the result set)
     */
    protected T getResults(AbstractPDGNode node) {
        T b = resultMemo.get(node);
        if (b == null) {
            throw new NullPointerException("Results are null for " + node + " in " + this.getClass());
        }
        return b;
    }

    /**
     * Get the element for input nodes
     * 
     * @return result element for the input nodes
     */
    abstract protected T getResultForInput();

    /**
     * Get the element for nodes which have no dependencies
     * 
     * @return result element for the nodes with no input edges
     */
    abstract protected T getResultForNoInputEdge();

    /**
     * Unit of work for a forward pass algorithm
     */
    private class ForwardPassTask extends RecursiveAction {

        /**
         * Serialization ID
         */
        private static final long serialVersionUID = 6217237537815519610L;
        /**
         * Node this will compute results for
         */
        private AbstractPDGNode current;

        /**
         * New task to compute results for the given node
         * 
         * @param current
         *            node to process
         */
        public ForwardPassTask(AbstractPDGNode current) {
            this.current = current;
        }

        @Override
        public void compute() {
            enqueuedNodes.remove(current);

            T results;

            Set<ForwardPassTask> forks = new HashSet<ForwardPassTask>();
            if (inputSet.contains(current)) {
                // Base case where the node is an input
                results = getResultForInput();
            } else {
                Map<AbstractPDGNode, PDGEdgeType> dependencyMap = getIncomingEdgeSources(current, graph);
                Set<AbstractPDGNode> missingDeps = addUnprocessedToQ(dependencyMap.keySet());

                for (AbstractPDGNode n : missingDeps) {
                    forks.add(new ForwardPassTask(n));
                }

                // pretend that the dependencies aren't there
                // this node will be updated when they are initialized
                dependencyMap.keySet().removeAll(missingDeps);

                if (dependencyMap.isEmpty()) {
                    // Base case with no input edges
                    results = getResultForNoInputEdge();
                } else {
                    results = computeResultFromDependencies(current, dependencyMap);
                }
            }

            if (!results.equals(resultMemo.get(current))) {
                // Something changed since last time we visited this node.
                // Revisit any other nodes that could change as a result
                Set<AbstractPDGNode> added = addToQ(getOutgoingEdgeTargets(current, graph));
                for (AbstractPDGNode n : added) {
                    forks.add(new ForwardPassTask(n));
                }
            }

            resultMemo.put(current, results);
            invokeAll(forks);
        }
    }
}
