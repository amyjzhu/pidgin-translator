package accrue.pdg.graph;

import java.lang.management.ManagementFactory;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import accrue.pdg.PDGEdge;
import accrue.pdg.PDGEdgeType;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.node.AbstractPDGNode;
import accrue.pdg.node.PDGNodeType;
import accrue.util.SoftHashMap;

/**
 * The most general implementation of a {@link ProgramDependenceGraph}
 */
public abstract class AbstractGraph implements ProgramDependenceGraph {

    @Override
    public boolean containsEdge(PDGEdge e) {
        return this.edgeSet().contains(e);
    }

    @Override
    public boolean containsVertex(AbstractPDGNode e) {
        return this.vertexSet().contains(e);
    }

    /**
     * Map storing cached newquery results
     */
    private Map<Class<?>, Map<Object, ProgramDependenceGraph>> newCacheResults = new SoftHashMap<>();

    @Override
    public ProgramDependenceGraph getCachedResults(Class<?> expr, Object args) {
        Map<Object, ProgramDependenceGraph> m = newCacheResults.get(expr);
        if (m == null)
            return null;
        // System.err.println("Found in Cache: " + expr);
        ProgramDependenceGraph r = m.get(args);
        // System.err.println("Found env Cache Too");
        return r;
    }

    @Override
    public void storeCachedResults(Class<?> expr, ProgramDependenceGraph result, Object args) {
        Map<Object, ProgramDependenceGraph> m = newCacheResults.get(expr);
        if (m == null) {
            m = new SoftHashMap<Object, ProgramDependenceGraph>();
            newCacheResults.put(expr, m);
        }
        m.put(args, result);

    }
    
    @Override
    public void clearAllCachedResults() {
        System.err.println("Clearing cache. " + (ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1000000) + "MB");
        newCacheResults = new SoftHashMap<>();
        System.gc();
        System.err.println("Finished clearing cache. " + (ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1000000) + "MB");
    }


    /**
     * Statistics for the PDG
     * 
     * @return list of info about the PDG
     */
    public List<String> getStats() {
        List<String> result = new LinkedList<String>();

        result.add("Edges: " + edgeSet().size());
        result.add("Nodes: " + vertexSet().size());

        // Count edges
        Map<PDGEdgeType, Integer> edgeCounts = new LinkedHashMap<PDGEdgeType, Integer>();

        // initialize to 0
        for (PDGEdgeType t : PDGEdgeType.values()) {
            edgeCounts.put(t, 0);
        }
        for (PDGEdge e : edgeSet()) {
            Integer count = edgeCounts.get(e.getType());
            edgeCounts.put(e.getType(), ++count);
        }

        for (PDGEdgeType t : PDGEdgeType.values()) {
            result.add(t + ": " + edgeCounts.get(t));
        }

        // Count Nodes
        Map<PDGNodeType, Integer> nodeCounts =
                new LinkedHashMap<PDGNodeType, Integer>();

        // initialize to 0
        for (PDGNodeType t : PDGNodeType.values()) {
            nodeCounts.put(t, 0);
        }
        for (AbstractPDGNode n : vertexSet()) {
            Integer count = nodeCounts.get(n.getNodeType());
            nodeCounts.put(n.getNodeType(), ++count);
        }
        for (PDGNodeType t : nodeCounts.keySet()) {
            result.add(t + ": " + nodeCounts.get(t));
        }

        return result;
    }

    @Override
    public boolean isEmpty() {
        return vertexSet().isEmpty() && edgeSet().isEmpty();
    }
}
