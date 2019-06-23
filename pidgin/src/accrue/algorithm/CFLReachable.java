package accrue.algorithm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import accrue.pdg.PDGEdge;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.node.AbstractPDGNode;
import accrue.pdg.node.PDGNodeType;
import accrue.util.OrderedPair;

/**
 * Compute the set of nodes rechable on paths with appropriately nested and matching call-sites and return-sites
 */
public class CFLReachable {

    /**
     * Create a new algorithm
     */
    public CFLReachable() {
    }

    private Map<AbstractPDGNode, Set<AbstractPDGNode>> methodResults = new HashMap<>();
    /**
     * Map from entry nodes to methods to the call sites that depend on them (i.e., elements that were in the global
     * queue.
     */
    private Map<AbstractPDGNode, Set<OrderedPair<AbstractPDGNode, Integer>>> globalDependencies = new HashMap<>();
    /**
     * Map from entry nodes of callees to entry nodes of callers.
     */
    private Map<AbstractPDGNode, Set<AbstractPDGNode>> methodDependencies = new HashMap<>();

    private void addGlobalDependency(OrderedPair<AbstractPDGNode, Integer> globalWorkItem,
                                     AbstractPDGNode calleeEntryNode) {
        Set<OrderedPair<AbstractPDGNode, Integer>> s = globalDependencies.get(calleeEntryNode);
        if (s == null) {
            s = new HashSet<>();
            globalDependencies.put(calleeEntryNode, s);
        }
        s.add(globalWorkItem);
    }

    private void addMethodDependency(AbstractPDGNode callerEntryNode, AbstractPDGNode calleeEntryNode) {
        Set<AbstractPDGNode> s = methodDependencies.get(calleeEntryNode);
        if (s == null) {
            s = new HashSet<>();
            methodDependencies.put(calleeEntryNode, s);
        }
        s.add(callerEntryNode);
    }

    private WorkQueue<OrderedPair<AbstractPDGNode, Integer>> globalQueue;
    private Set<OrderedPair<AbstractPDGNode, Integer>> globalVisited;

    private Set<AbstractPDGNode> reachableDests;

    private ProgramDependenceGraph pdg;

    private Set<AbstractPDGNode> sinks;


    private void addToGlobalQueue(AbstractPDGNode n, Integer callSiteId) {
        OrderedPair<AbstractPDGNode, Integer> p = new OrderedPair<AbstractPDGNode, Integer>(n, callSiteId);
        if (globalVisited.add(p)) {
            globalQueue.add(p);
        }


    }
    /**
     * Get the memoized results (i.e., the exit nodes reachable from the entryPDGNode), computing them if necessary.
     * 
     * @param entryPDGNode
     * @return
     */
    private Set<AbstractPDGNode> getMethodResults(AbstractPDGNode entryPDGNode) {
        return getMethodResults(entryPDGNode, false);
    }

    /**
     * Get the memoized results (i.e., the exit nodes reachable from the entryPDGNode), computing them if necessary, or
     * if the forceRecompute flag is true.
     * 
     * @param entryPDGNode
     * @return
     */
    private Set<AbstractPDGNode> getMethodResults(AbstractPDGNode entryPDGNode, boolean forceRecompute) {
        assert entryPDGNode.getNodeType().isEntrySummary();
        Set<AbstractPDGNode> s = methodResults.get(entryPDGNode);
        if (s == null) {
            // don't have any results yet.
            s = new HashSet<AbstractPDGNode>();
            methodResults.put(entryPDGNode, s);
            forceRecompute = true;
        }
        if (forceRecompute) {
            if (s.addAll(computeMethodResults(entryPDGNode))) {
                // we added some exit nodes.
                // Check the dependencies, and redo them.

                Set<AbstractPDGNode> methodDeps = this.methodDependencies.get(entryPDGNode);
                if (methodDeps != null) {
                    for (AbstractPDGNode dep : methodDeps) {
                        // dep used the result for entryPDGNode, and since these results have changed, for dep to be recomputed.
                        getMethodResults(dep, true);
                    }
                }

                // Add the global dependencies directly to the queue (they have already been visited, we want to revisit them)
                Set<OrderedPair<AbstractPDGNode, Integer>> globalDeps = this.globalDependencies.get(entryPDGNode);
                if (globalDeps != null) {
                    globalQueue.addAll(globalDeps);
                }
            }
        }
        return s;
    }

    /**
     * Determine which exit nodes are reachable from the entryPDGNode
     */
    private Set<AbstractPDGNode> computeMethodResults(AbstractPDGNode entryPDGNode) {
        Set<AbstractPDGNode> exits = new HashSet<>();

        Set<OrderedPair<AbstractPDGNode, Integer>> visited = new HashSet<>();
        WorkQueue<OrderedPair<AbstractPDGNode, Integer>> q = new WorkQueue<>();
        q.add(new OrderedPair<AbstractPDGNode, Integer>(entryPDGNode, null));
        visited.add(new OrderedPair<AbstractPDGNode, Integer>(entryPDGNode, null));
        while (!q.isEmpty()) {
            OrderedPair<AbstractPDGNode, Integer> p = q.poll();
            AbstractPDGNode n = p.fst();            
            if (checkReachableDest(n)) {
                // Found everything already break out of the loop
                break;
            }
            if (n.getNodeType().isExitSummary()) {
                assert p.snd() == null && entryPDGNode.getProcedureName().equals(n.getProcedureName()); // this should be an exit for the same method as the entry
                exits.add(n);
                continue;
            }
            if (n.getNodeType().isEntrySummary()) {
                Integer callSiteID = p.snd();
                if (callSiteID == null) {
                    // this is the entry node for this method we are analyzing!
                    assert (n == entryPDGNode);
                    // we'll handle this below.
                }
                else {
                    // this is a callee.
                    // get the result.
                    Set<AbstractPDGNode> calleeExits = getMethodResults(n);
                    addMethodDependency(entryPDGNode, n);
                    
                    // Add exit nodes for the matching return sites to the queue
                    for (AbstractPDGNode calleeExit : calleeExits) {
                        for (PDGEdge e : this.pdg.outgoingEdgesOf(calleeExit)) {
                            assert e.getEdgeLabel() != null;
                            if (e.getEdgeLabel().getCallSiteID() == callSiteID) {
                                // this is a return to our method!
                                OrderedPair<AbstractPDGNode, Integer> targetPair = new OrderedPair<AbstractPDGNode, Integer>(e.getTarget(),
                                                                                                                             null);
                                assert targetPair.fst().getProcedureName().equals(entryPDGNode.getProcedureName());
                                if (visited.add(targetPair)) {
                                    q.add(targetPair);
                                }
                            }
                        }
                    }
                    // we are done with this.
                    continue;
                }
            }
            // handle heap nodes
            if (n.getNodeType() == PDGNodeType.ABSTRACT_LOCATION) {
                addToGlobalQueue(n, null);
                continue;
            }

            // handle normal nodes.
            assert n.getProcedureName().equals(entryPDGNode.getProcedureName());
            for (PDGEdge e : this.pdg.outgoingEdgesOf(n)) {
                if (e.getEdgeLabel() != null) {
                    // we are calling out to another method. Record the call site id.
                    Integer callSiteId = e.getEdgeLabel().getCallSiteID();
                    OrderedPair<AbstractPDGNode, Integer> targetPair = new OrderedPair<AbstractPDGNode, Integer>(e.getTarget(),
                                                                                                                 callSiteId);
                    assert targetPair.fst().getNodeType().isEntrySummary();
                    if (visited.add(targetPair)) {
                        q.add(targetPair);
                    }
                }
                else {
                    // no edge label, just a normal node.
                    OrderedPair<AbstractPDGNode, Integer> targetPair = new OrderedPair<AbstractPDGNode, Integer>(e.getTarget(),
                                                                                                                 null);
                    assert targetPair.fst().getNodeType() == PDGNodeType.ABSTRACT_LOCATION
                            || targetPair.fst().getProcedureName().equals(entryPDGNode.getProcedureName()) : "The targets of this edge should be either a heap location or a node in the same method as this.";
                    if (visited.add(targetPair)) {
                        q.add(targetPair);
                    }

                }
            }

        }

        return exits;
    }


    /**
     * Compute all nodes in <code>sinks</code> that are reachable from a node in <code>source</code> via a valid
     * sequence of calls and returns.
     * 
     * @param pdg Program dependence graph to check for reachability in
     * @param sources set of sources
     * @param sinks set of (possibly reachable) destination nodes
     * @return the subset of <code>sinks</code> that is reachable via a vaild sequence of edges
     */
    public Set<AbstractPDGNode> computeReachable(ProgramDependenceGraph pdg, Set<AbstractPDGNode> sources,
                                                 Set<AbstractPDGNode> sinks) {

        this.pdg = pdg;

        this.globalQueue = new WorkQueue<>();
        this.globalVisited = new HashSet<>();

        // Initialize the reachable set with any sources that are also sinks
        this.reachableDests = new LinkedHashSet<>(sources);
        this.reachableDests.retainAll(sinks);
        this.sinks = sinks;

        // Add the sources to the work queue with no call site
        for (AbstractPDGNode n : sources) {
            addToGlobalQueue(n, null);
        }

        while (!globalQueue.isEmpty()) {

            OrderedPair<AbstractPDGNode, Integer> p = globalQueue.poll();
            AbstractPDGNode n = p.fst();
            if (checkReachableDest(n)) {
                // Already found all the sinks
                break;
            }

            assert p.snd() != null ? n.getNodeType().isEntrySummary() : true;

            if (n.getNodeType().isEntrySummary()) {
                Integer callSiteID = p.snd();
                if (callSiteID != null) {
                    // this is a callee.
                    // get the result.
                    Set<AbstractPDGNode> calleeExits = getMethodResults(n);
                    addGlobalDependency(p, n); // XXX AAJ Its better to record the dependency after computing the results, right?
                    for (AbstractPDGNode calleeExit : calleeExits) {
                        for (PDGEdge e : this.pdg.outgoingEdgesOf(calleeExit)) {
                            assert e.getEdgeLabel() != null : e;
                            if (e.getEdgeLabel().getCallSiteID() == callSiteID) {
                                // this is a return to our method!
                                addToGlobalQueue(e.getTarget(), null);
                            }
                        }
                    }
                    // we are done with this.
                    continue;
                }
            }

            // handle normal nodes (including heap nodes, exits, etc.)
            for (PDGEdge e : this.pdg.outgoingEdgesOf(n)) {
                if (e.getEdgeLabel() != null && e.getTarget().getNodeType().isEntrySummary()) {
                    // we are calling out to another method. Record the call site id.
                    Integer callSiteId = e.getEdgeLabel().getCallSiteID();
                    addToGlobalQueue(e.getTarget(), callSiteId);
                }
                else {
                    // no edge label (just a normal node), or an edge label to an exit.
                    addToGlobalQueue(e.getTarget(), null);
                }
            }

        }

        return reachableDests;
    }

    /**
     * Check whether the given node is a sink. If it is and all sinks have been found then return true, else return false.
     * 
     * @param n node to check
     * @return true if all sinks have been found
     */
    private boolean checkReachableDest(AbstractPDGNode n) {
        if (this.sinks.contains(n)) {
            this.reachableDests.add(n);
        }
        return (sinks.size() == reachableDests.size());
    }

}
