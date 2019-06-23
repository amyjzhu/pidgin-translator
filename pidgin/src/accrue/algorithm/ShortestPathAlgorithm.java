package accrue.algorithm;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import accrue.pdg.PDGEdge;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.graph.PDGFactory;
import accrue.pdg.node.AbstractPDGNode;

/**
 * Utility methods for certain graph common algorithms
 */
public class ShortestPathAlgorithm {

    /**
     * Get the shortest path between two nodes in the PDG or an empty graph if no path
     * exists
     * 
     * @param pdg
     *            Graph to find a path in
     * @param sources
     *            possible first nodes in the path
     * @param dests
     *            possible last nodes in the path
     * @return A graph consisting of a shortest path between the two nodes or empty if none exists
     */
    public static ProgramDependenceGraph shortestPath(ProgramDependenceGraph pdg, Set<AbstractPDGNode> sources, Set<AbstractPDGNode> dests) {
        LinkedList<FPath> q = new LinkedList<FPath>();
        for (AbstractPDGNode s : sources) {
            FPath p = new FPath.EmptyFPath(s);
            if (dests.contains(s)) {
                return fpathToPDG(p);
            }
            else {
                q.addLast(p);
            }
        }
        Set<AbstractPDGNode> visited = new HashSet<AbstractPDGNode>(sources);
        while (!q.isEmpty()) {
            FPath p = q.removeFirst();
            for (PDGEdge e : pdg.outgoingEdgesOf(p.lastNode())) {
                FPath newP = new FPath.ConsFPath(p, e);
                AbstractPDGNode target = e.getTarget();
                if (dests.contains(target)) {
                    // found a path!
                    return fpathToPDG(newP);
                }
                else if (visited.add(target)) {
                    // we haven't already added a path with last node target, so do so now.
                    q.addLast(newP);
                }
            }
        }
        return PDGFactory.emptyGraph();
    }
    
    /**
     * Convert a path into a PDG
     * 
     * @param path
     *            path to be converted
     * @return PDG containing only nodes and edges on the path
     */
    private static ProgramDependenceGraph fpathToPDG(FPath path) {
        LinkedList<PDGEdge> pathList = new LinkedList<PDGEdge>();
        PDGEdge e;
        while ((e = path.lastEdge()) != null) {
            pathList.addFirst(e);
            path = path.prefix();
        }
        Set<PDGEdge> edges = new LinkedHashSet<PDGEdge>(pathList);        
        return PDGFactory.graphFromEdges(edges);
    }
    
    /**
     * Functional representation of a pathq
     */
    public interface FPath {
        /**
         * last node on the path
         * 
         * @return last node on the path
         */
        AbstractPDGNode lastNode();

        /**
         * Path containing every node and edge in this path except the last
         * 
         * @return Path containing every node and edge in this path except the
         *         last
         */
        FPath prefix();

        /**
         * last edge on the path
         * 
         * @return last edge on the path
         */
        PDGEdge lastEdge();
        
        /**
         * Path containing one node and no edges
         */
        public static class EmptyFPath implements FPath{
            /**
             * Singleton node on this path
             */
            final AbstractPDGNode n;

            /**
             * Create a new path containing just <code>n</code>
             * 
             * @param n
             *            only node on this path
             */
            public EmptyFPath(AbstractPDGNode n) {
                this.n = n;
            }

            @Override
            public AbstractPDGNode lastNode() {
                return n;
            }

            @Override
            public PDGEdge lastEdge() {
                return null;
            }
            @Override
            public FPath prefix() {
                return null;
            }
        }
        /**
         * New path that is the same as a previous but with one more edge
         */
        public static class ConsFPath implements FPath{
            /**
             * Path containing the same nodes and edges as this path except the last
             * node and edge
             */
            private final FPath prefix;
            /**
             * last edge on this path
             */
            private final PDGEdge edge;
            
            /**
             * New path that is the same as a previous but with one more edge
             * 
             * @param p
             *            previous path
             * @param e
             *            new edge to add
             */
            public ConsFPath(FPath p, PDGEdge e) {
                this.prefix = p;
                this.edge = e;
            }

            @Override
            public AbstractPDGNode lastNode() {
                return edge.getTarget();
            }
            @Override
            public PDGEdge lastEdge() {
                return edge;
            }
            @Override
            public FPath prefix() {
                return prefix;
            }
        }
    }
}
