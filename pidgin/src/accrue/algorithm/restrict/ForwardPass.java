package accrue.algorithm.restrict;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import accrue.algorithm.WorkQueue;
import accrue.pdg.PDGEdge;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.node.AbstractPDGNode;
import accrue.util.OrderedPair;

/**
 * Takes a set of nodes and computes a graph that depends on those nodes in some
 * way. Subclasses must implement
 * {@link ForwardPass#computeResult(AbstractPDGNode, Set)} Which takes a node
 * and all nodes that the node depends on, and returns whether that node is part
 * of the result graph based on its dependencies.
 * <p>
 * This class manages the work queue to ensure that results are recomputed only
 * if the results for dependencies have changed.
 * 
 * @param <T>
 *            Type of the results of the pass
 */
public abstract class ForwardPass<T> extends Restrictor {

    /**
     * Memoization table for results of the work queue algorithm
     */
    private Map<AbstractPDGNode, T> resultMemo = new LinkedHashMap<>();
    /**
     * Queue containing nodes that need to be processed
     */
    private WorkQueue<AbstractPDGNode> q = new WorkQueue<>();

    @Override
    public ProgramDependenceGraph restrict(ProgramDependenceGraph inputPDG, Set<AbstractPDGNode> guards, Set<AbstractPDGNode> notUsed) {

        if (notUsed.size() > 0) {
            throw new IllegalArgumentException("The output set is not used for a forward pass  so make it empty");
        }

        // initialize the Q
        q.addAll(guards);

        runWorkQueue(inputPDG, guards);

        // Remove anything not in the result set. We only want bad nodes
        return produceGraph(inputPDG, resultMemo);
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
    abstract ProgramDependenceGraph produceGraph(ProgramDependenceGraph originalPDG,
            Map<AbstractPDGNode, T> results);

    /**
     * Run a work-queue based algorithm. When a node in the queue is analyzed we
     * compute a set containing any nodes on paths to that node for which no
     * input condition is true.
     * 
     * @param pdg
     *            We only consider nodes in this program dependence graph
     * @param inputs
     *            Set of input nodes
     */
    private void runWorkQueue(ProgramDependenceGraph pdg, Set<AbstractPDGNode> inputs) {
        while (!q.isEmpty()) {
            AbstractPDGNode current = q.poll();
            T results;

            if (inputs.contains(current)) {
                // Base case where the node is an input
                results = getResultForInput();
            } else {
                OrderedPair<T, Set<AbstractPDGNode>> pair = computeResult(current, pdg.incomingEdgesOf(current));
                results = pair.fst();
                if (pair.snd() != null) {
                    q.addAll(pair.snd());
                }
            }

            if (!results.equals(getResults(current))) {
                // Something changed since last time we visited this node.
                // Revisit any other nodes that could change as a result
                for (PDGEdge e : pdg.outgoingEdgesOf(current)) {
                    q.add(e.getTarget());
                }
            }

            resultMemo.put(current, results);
        }
    }

    /**
     * Compute the results for a node after results have been computed for its
     * dependencies
     * 
     * @param current
     *            node to compute results for
     * @param incomingEdges
     *            incoming edges to the current node.
     * @return The results for the current node, and any nodes to add to the queue
     */
    protected abstract OrderedPair<T,Set<AbstractPDGNode>> computeResult(AbstractPDGNode current, Set<PDGEdge> incomingEdges);

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
        if (resultMemo.containsKey(node)) {
            return resultMemo.get(node);
        }
        return this.defaultResultForMissing();
    }

    /**
     * The default result for unprocessed nodes.
     * 
     * @return default result for unprocessed nodes.
     */
    protected abstract T defaultResultForMissing();

    /**
     * Get the element for input nodes
     * 
     * @return result element for the input nodes
     */
    abstract protected T getResultForInput();

    /**
     * Get results for a nodes with no input edges
     * 
     * @return results for a nodes with no input edges
     */
    abstract protected T getResultForNoInputEdges();
}
