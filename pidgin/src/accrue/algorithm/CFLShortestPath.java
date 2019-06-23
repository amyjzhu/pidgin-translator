package accrue.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import accrue.pdg.PDGEdge;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.node.AbstractPDGNode;
import accrue.pdg.node.PDGNodeType;

/**
 * Compute the shortest path with appropriately nested and matching call-sites and return-sites
 */
public class CFLShortestPath {

    /**
     * Create a new algorithm
     */
    public CFLShortestPath() {
    }

    /**
     * Map from entry nodes of methods to exit nodes and heap nodes, along with the shortest path from the entry node to
     * the heap/exit node.
     */
    private Map<AbstractPDGNode, Map<AbstractPDGNode, Path>> methodResults = new HashMap<>();
    /**
     * Map from entry nodes to methods to the call sites that depend on them (i.e., elements that were in the global
     * queue.
     */
    private Map<AbstractPDGNode, Set<WorkItem>> globalDependencies;
    /**
     * Map from entry nodes of callees to entry nodes of callers.
     */
    private Map<AbstractPDGNode, Set<AbstractPDGNode>> methodDependencies = new HashMap<>();

    private void addGlobalDependency(WorkItem globalWorkItem,
                                     AbstractPDGNode calleeEntryNode) {
        Set<WorkItem> s = globalDependencies.get(calleeEntryNode);
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


    private Map<AbstractPDGNode, Path> reachableDests;

    private ProgramDependenceGraph pdg;

    private Set<AbstractPDGNode> sinks;

    private WorkQueue<WorkItem> globalQueue;
    private Map<WorkItemKey, Integer> globalVisited;

    private void addToGlobalQueue(AbstractPDGNode n, Integer callSiteId, Path newPath, int maxPath) {
        addToGlobalQueue(new WorkItem(n, callSiteId, newPath), maxPath);
    }

    private void addToGlobalQueue(WorkItem wi, int maxPath) {
        if (wi.shortestPathFromSource.size() > maxPath) {
            // ignore this.
            return;
        }
        WorkItemKey workItemKey = new WorkItemKey(wi.n, wi.callSiteId);
        Integer existingPath = globalVisited.get(workItemKey);
        if (existingPath == null || existingPath > wi.shortestPathFromSource.size()) {
            globalVisited.put(workItemKey, wi.shortestPathFromSource.size());
            globalQueue.add(wi);
        }
    }

    private static void addToQ(AbstractPDGNode n, Integer callSiteId, Path newPath, WorkQueue<WorkItem> q,
                               Map<WorkItemKey, Integer> visitedPathLength) {
        WorkItemKey workItemKey = new WorkItemKey(n, callSiteId);
        Integer existingPath = visitedPathLength.get(workItemKey);
        if (existingPath == null || existingPath > newPath.size()) {
            visitedPathLength.put(workItemKey, newPath.size());
            q.add(new WorkItem(n, callSiteId, newPath));
        }
    }

    /**
     * Get the memoized results (i.e., the exit nodes reachable from the entryPDGNode), computing them if necessary.
     * 
     * @param entryPDGNode
     * @return
     */
    private Map<AbstractPDGNode, Path> getMethodResults(AbstractPDGNode entryPDGNode) {
        return getMethodResults(entryPDGNode, false);
    }

    /**
     * Get the memoized results (i.e., the exit nodes reachable from the entryPDGNode), computing them if necessary, or
     * if the forceRecompute flag is true.
     * 
     * @param entryPDGNode
     * @return
     */
    private Map<AbstractPDGNode, Path> getMethodResults(AbstractPDGNode entryPDGNode, boolean forceRecompute) {
        assert entryPDGNode.getNodeType().isEntrySummary();
        Map<AbstractPDGNode, Path> s = methodResults.get(entryPDGNode);
        if (s == null) {
            // don't have any results yet.
            s = new HashMap<>();
            methodResults.put(entryPDGNode, s);
            forceRecompute = true;
        }
        if (forceRecompute) {
            Map<AbstractPDGNode, Path> newResults = computeMethodResults(entryPDGNode);
            boolean changed = false;
            for (AbstractPDGNode n : newResults.keySet()) {
                Path old = s.put(n, newResults.get(n));
                if (old == null || old.size() > newResults.get(n).size()) {
                    changed = true;
                }
            }
            if (changed) {
                // we added some exit nodes or shortened some paths
                // Check the dependencies, and redo them.
                Set<AbstractPDGNode> deps = this.methodDependencies.get(entryPDGNode);
                if (deps != null) {
                    for (AbstractPDGNode dep : deps) {
                        // dep used the result for entryPDGNode, and since these results have changed, for dep to be recomputed.
                        getMethodResults(dep, true);
                    }
                }

                // Add the global dependencies directly to the queue (they have already been visited, we want to revisit them)
                Set<WorkItem> globalDeps = this.globalDependencies.get(entryPDGNode);
                if (globalDeps != null) {
                    for (WorkItem wi : globalDeps) {
                        addToGlobalQueue(wi, Integer.MAX_VALUE);
                    }
                }
            }
        }
        return s;
    }

    static class WorkItemKey {
        WorkItemKey(AbstractPDGNode n, Integer callSiteId) {
            this.n = n;
            this.callSiteId = callSiteId;
        }

        final AbstractPDGNode n;
        final Integer callSiteId;

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((callSiteId == null) ? 0 : callSiteId.hashCode());
            result = prime * result + ((n == null) ? 0 : n.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof WorkItemKey)) {
                return false;
            }
            WorkItemKey other = (WorkItemKey) obj;
            if (callSiteId == null) {
                if (other.callSiteId != null) {
                    return false;
                }
            }
            else if (!callSiteId.equals(other.callSiteId)) {
                return false;
            }
            if (n == null) {
                if (other.n != null) {
                    return false;
                }
            }
            else if (!n.equals(other.n)) {
                return false;
            }
            return true;
        }

    }

    static class WorkItem {
        WorkItem(AbstractPDGNode n, Integer callSiteId, Path shortestPathFromSource) {
            this.n = n;
            this.callSiteId = callSiteId;
            this.shortestPathFromSource = shortestPathFromSource;
        }

        final AbstractPDGNode n;
        final Integer callSiteId;
        final Path shortestPathFromSource;
    }
    /**
     * Determine which exit nodes are reachable from the entryPDGNode
     */
    private Map<AbstractPDGNode, Path> computeMethodResults(AbstractPDGNode entryPDGNode) {
        Map<AbstractPDGNode, Path> exits = new HashMap<>();

        Map<WorkItemKey, Integer> visitedPathLength = new HashMap<>();
        WorkQueue<WorkItem> q = new WorkQueue<>();
        q.add(new WorkItem(entryPDGNode, null, EmptyPath.INSTANCE));
        visitedPathLength.put(new WorkItemKey(entryPDGNode, null), 0);
        while (!q.isEmpty()) {
            WorkItem p = q.poll();
            AbstractPDGNode n = p.n;

            if (this.sinks.contains(n)) {
                // this is a reachable destination. Add it to the
                // exits, so we can check for the shortest path there.
                Path existing = exits.get(n);
                if (existing == null || existing.size() > p.shortestPathFromSource.size()) {
                    exits.put(n, p.shortestPathFromSource);
                }                
            }
            
            if (n.getNodeType().isExitSummary()) {
                assert p.callSiteId == null && entryPDGNode.getProcedureName().equals(n.getProcedureName()); // this should be an exit for the same method as the entry
                Path existing = exits.get(n);
                if (existing == null || existing.size() > p.shortestPathFromSource.size()) {
                    exits.put(n, p.shortestPathFromSource);
                }
                continue;
            }
            if (n.getNodeType().isEntrySummary()) {
                Integer callSiteID = p.callSiteId;
                if (callSiteID == null) {
                    // this is the entry node for this method we are analyzing!
                    assert (n == entryPDGNode);
                    // we'll handle this below.
                }
                else {
                    // this is a callee.
                    // get the result.
                    Map<AbstractPDGNode, Path> calleeExits = getMethodResults(n);
                    addMethodDependency(entryPDGNode, n);

                    for (AbstractPDGNode calleeExit : calleeExits.keySet()) {
                        Path calleePath = calleeExits.get(calleeExit);
                        Path pathToCalleeExit = concat(p.shortestPathFromSource, calleePath);

                        assert (this.sinks.contains(calleeExit)
                                || calleeExit.getNodeType() == PDGNodeType.ABSTRACT_LOCATION || calleeExit.getNodeType()
                                                                                                          .isExitSummary());
                        if (this.sinks.contains(calleeExit)
                                || calleeExit.getNodeType() == PDGNodeType.ABSTRACT_LOCATION) {
                            // this is a heap node or a destination node.
                            Path existing = exits.get(calleeExit);
                            if (existing == null || existing.size() > pathToCalleeExit.size()) {
                                exits.put(calleeExit, pathToCalleeExit);
                            }
                        }
                        if (calleeExit.getNodeType().isExitSummary()) {
                            for (PDGEdge e : this.pdg.outgoingEdgesOf(calleeExit)) {
                                assert e.getEdgeLabel() != null;
                                if (e.getEdgeLabel().getCallSiteID() == callSiteID) {
                                    // this is a return to our method!

                                    Path newPath = concat(pathToCalleeExit, e);
                                    assert e.getTarget().getProcedureName().equals(entryPDGNode.getProcedureName());
                                    addToQ(e.getTarget(), null, newPath, q, visitedPathLength);
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
                Path existing = exits.get(n);
                if (existing == null || existing.size() > p.shortestPathFromSource.size()) {
                    exits.put(n, p.shortestPathFromSource);
                }
                continue;
            }

            // handle normal nodes.
            assert n.getProcedureName().equals(entryPDGNode.getProcedureName());
            for (PDGEdge e : this.pdg.outgoingEdgesOf(n)) {
                Path newPath = concat(p.shortestPathFromSource, e);
                if (e.getEdgeLabel() != null) {
                    // we are calling out to another method. Record the call site id.
                    Integer callSiteId = e.getEdgeLabel().getCallSiteID();
                    assert e.getTarget().getNodeType().isEntrySummary();

                    addToQ(e.getTarget(), callSiteId, newPath, q, visitedPathLength);
                }
                else {
                    // no edge label, just a normal node.
                    assert e.getTarget().getNodeType() == PDGNodeType.ABSTRACT_LOCATION
                            || e.getTarget().getProcedureName().equals(entryPDGNode.getProcedureName()) : "The targets of this edge should be either a heap location or a node in the same method as this.";
                    addToQ(e.getTarget(), null, newPath, q, visitedPathLength);

                }
            }

        }

        return exits;
    }



    private Path concat(Path a, PDGEdge e) {
        assert a == null || a.size() == 0 || a.last().getTarget().equals(e.getSource());
        if (a == null || a.size() == 0) {
            return new SingletonPath(e);
        }
        return new ExtendPath(a, e);


    }

    private Path concat(Path a, Path b) {
        if (a.size() == 0) {
            return b;
        }
        if (b.size() == 0) {
            return a;
        }
        if (b.size() == 1) {
            return new ExtendPath(a, b.first());
        }
        assert (a.last().getTarget().equals(b.first().getSource()));

        return new ConcatPath(a, b);
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
    public Map<AbstractPDGNode, Path> computeReachable(ProgramDependenceGraph pdg,
                                                                Set<AbstractPDGNode> sources,
                                                                Set<AbstractPDGNode> sinks, int maxPath) {

        this.pdg = pdg;

        this.globalQueue = new WorkQueue<>();
        this.globalVisited = new HashMap<>();

        this.globalDependencies = new HashMap<>();

        // Initialize the reachable set with any sources that are also sinks
        this.reachableDests = new HashMap<>();
        this.sinks = sinks;

        // Add the sources to the work queue with no call site
        for (AbstractPDGNode n : sources) {
            addToGlobalQueue(n, null, EmptyPath.INSTANCE, maxPath);
        }

        while (!globalQueue.isEmpty()) {

            WorkItem p = globalQueue.poll();
            AbstractPDGNode n = p.n;
            checkReachableDest(n, p.shortestPathFromSource);

            assert p.callSiteId != null ? n.getNodeType().isEntrySummary() : true;

            if (n.getNodeType().isEntrySummary()) {
                Integer callSiteID = p.callSiteId;
                if (callSiteID != null) {
                    // this is a callee.
                    // get the result.
                    addGlobalDependency(p, n);
                    Map<AbstractPDGNode, Path> calleeExits = getMethodResults(n);
                    for (AbstractPDGNode calleeExit : calleeExits.keySet()) {
                        assert this.sinks.contains(calleeExit)
                                || calleeExit.getNodeType() == PDGNodeType.ABSTRACT_LOCATION
                                || calleeExit.getNodeType().isExitSummary();
                        Path pathToCalleeExit = concat(p.shortestPathFromSource, calleeExits.get(calleeExit));
                        if (this.sinks.contains(calleeExit)) {
                            this.checkReachableDest(calleeExit, pathToCalleeExit);
                        }
                        if (calleeExit.getNodeType() == PDGNodeType.ABSTRACT_LOCATION) {
                            // this is a heap node
                            addToGlobalQueue(calleeExit, null, pathToCalleeExit, maxPath);
                        }
                        if (calleeExit.getNodeType().isExitSummary()) {
                            for (PDGEdge e : this.pdg.outgoingEdgesOf(calleeExit)) {
                                assert e.getEdgeLabel() != null;
                                if (e.getEdgeLabel().getCallSiteID() == callSiteID) {
                                    // this is a return to our method!
                                    addToGlobalQueue(e.getTarget(), null, concat(pathToCalleeExit, e), maxPath);
                                }
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
                    addToGlobalQueue(e.getTarget(), callSiteId, concat(p.shortestPathFromSource, e), maxPath);
                }
                else {
                    // no edge label, just a normal node, or it's an exit node
                    addToGlobalQueue(e.getTarget(), null, concat(p.shortestPathFromSource, e), maxPath);
                }
            }

        }

        return reachableDests;
    }

    private void checkReachableDest(AbstractPDGNode n, Path path) {
        if (this.sinks.contains(n)) {
            Path p = this.reachableDests.get(n);
            if (p == null || p.size() > path.size()) {
                this.reachableDests.put(n, path);
            }
        }
    }

    public List<PDGEdge> shortestPathIterativeDeepening(ProgramDependenceGraph pdg, Set<AbstractPDGNode> sources,
                                                        Set<AbstractPDGNode> sinks) {
        for (int i = 0; i < pdg.edgeSet().size(); i++) {
            if (i % 10000 == 0) {
                System.err.println("Path length: " + i);
            }
            Path p = shortestPathBoundedSearch(pdg, sources, sinks, i);
            if (p != null) {
                List<PDGEdge> l = new ArrayList<>(p.size());
                p.addSelfToList(l);
                return l;
            }
        }
        return null;
    }
    public List<PDGEdge> shortestPath(ProgramDependenceGraph pdg, Set<AbstractPDGNode> sources,
                                      Set<AbstractPDGNode> sinks) {
        return shortestPathIterativeDeepening(pdg, sources, sinks);
    }

    private Path shortestPathBoundedSearch(ProgramDependenceGraph pdg, Set<AbstractPDGNode> sources,
                                                   Set<AbstractPDGNode> sinks, int maxDepth) {
        Map<AbstractPDGNode, Path> res = computeReachable(pdg, sources, sinks, maxDepth);
        Path shortest = null;
        for (Path path : res.values()) {
            if (shortest == null || path.size() < shortest.size()) {
                shortest = path;
            }
        }
        return shortest;
    }

    interface Path {
        PDGEdge last();

        PDGEdge first();

        int size();

        void addSelfToList(List<PDGEdge> l);
    }

    static class EmptyPath implements Path {

        public static final Path INSTANCE = new EmptyPath();;

        @Override
        public int size() {
            return 0;
        }

        @Override
        public PDGEdge last() {
            return null;
        }

        @Override
        public PDGEdge first() {
            return null;
        }

        @Override
        public void addSelfToList(List<PDGEdge> l) {

        }

        @Override
        public boolean equals(Object o) {
            throw new UnsupportedOperationException();
        }

    }

    static class SingletonPath implements Path {
        final PDGEdge e;

        public SingletonPath(PDGEdge e) {
            this.e = e;
        }

        @Override
        public PDGEdge last() {
            return e;
        }

        @Override
        public PDGEdge first() {
            return e;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public void addSelfToList(List<PDGEdge> l) {
            l.add(e);
        }

        @Override
        public boolean equals(Object o) {
            throw new UnsupportedOperationException();
        }

    }

    static class ExtendPath implements Path {
        final Path prefix;
        final PDGEdge e;
        final int size;

        public ExtendPath(Path prefix, PDGEdge e) {
            this.prefix = prefix;
            this.e = e;
            this.size = prefix.size() + 1;
        }

        @Override
        public PDGEdge last() {
            return e;
        }

        @Override
        public PDGEdge first() {
            return prefix.first();
        }

        @Override
        public int size() {
            return this.size;
        }

        @Override
        public void addSelfToList(List<PDGEdge> l) {
            prefix.addSelfToList(l);
            l.add(e);
        }

        @Override
        public boolean equals(Object o) {
            throw new UnsupportedOperationException();
        }

    }

    static class ConcatPath implements Path {
        final Path a;
        final Path b;
        final int size;

        public ConcatPath(Path a, Path b) {
            this.a = a;
            this.b = b;
            this.size = a.size() + b.size();
            assert a.size() > 0 && b.size() > 0;
        }

        @Override
        public PDGEdge last() {
            return b.last();
        }

        @Override
        public PDGEdge first() {
            return a.first();
        }

        @Override
        public int size() {
            return this.size;
        }

        @Override
        public void addSelfToList(List<PDGEdge> l) {
            a.addSelfToList(l);
            b.addSelfToList(l);
        }

        @Override
        public boolean equals(Object o) {
            throw new UnsupportedOperationException();
        }

    }
}
