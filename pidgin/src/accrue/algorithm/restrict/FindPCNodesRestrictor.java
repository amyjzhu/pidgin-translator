package accrue.algorithm.restrict;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import accrue.pdg.PDGEdge;
import accrue.pdg.PDGEdgeType;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.graph.PDGFactory;
import accrue.pdg.node.AbstractPDGNode;
import accrue.util.OrderedPair;

/**
 * The set of input nodes are nodes representing conditional expressions. This 
 * Restrictor finds the set of path conditions for which the given conditional expressions
 * are definitely true and/or false.
 */
public class FindPCNodesRestrictor extends ForwardPass<FindPCNodesRestrictor.Results> {

    /**
     * If true this will get a graph including PC nodes for which the input
     * conditions are true
     */
    final protected boolean findTrue;
    /**
     * If true this will get a graph including PC nodes for which the input
     * conditions are false
     */
    final protected boolean findFalse;
    
    /**
     * Create new instance of the algorithm for true and/or false PC nodes
     * 
     * @param findTrue
     *            If true this will get a graph including PC nodes for which the
     *            input conditions are true
     * @param findFalse
     *            If true this will get a graph including PC nodes for which the
     *            input conditions are false
     */
    public FindPCNodesRestrictor(boolean findTrue, boolean findFalse) {
        this.findTrue = findTrue;
        this.findFalse = findFalse;
    }

    /**
     * Create new instance of the algorithm for true AND false PC nodes
     */
    public FindPCNodesRestrictor() {
        this(true,true);
    }
    
    @Override
    ProgramDependenceGraph produceGraph(ProgramDependenceGraph originalPDG, Map<AbstractPDGNode, Results> results) {
        Set<AbstractPDGNode> pcNodes = new HashSet<AbstractPDGNode>();
        for (AbstractPDGNode n : results.keySet()) {
            // Keep all matching nodes that are PC nodes
            if (results.get(n).inSet && n.getNodeType().isPathCondition()) {
                pcNodes.add(n);
            }
        }    
        System.err.println("\tFound " + pcNodes.size() + " PC nodes");
        return PDGFactory.retainNodes(originalPDG, pcNodes);
    }
    
    @Override
    protected OrderedPair<Results, Set<AbstractPDGNode>> computeResult(AbstractPDGNode current, Set<PDGEdge> incoming) {

//        if (current.toString().contains("&&")) {
//            System.out.println(current);
//        }
        boolean inSet = false;
        boolean isMerge = false;
        // Have TRUE or FALSE edges have been seen on all (non-TRUE/FALSE)
        // incoming edges
        
        // If this is a merge node then all incoming edges must be seen 
        boolean anyMergeEdges = false; 
        for (PDGEdge e : incoming) {
            if (e.getType() == PDGEdgeType.MERGE) {
                anyMergeEdges = true;
                break;
            }
        }
        // Otherwise one conjunction edge is enough
        boolean TRUEorFALSEseen;
        if (anyMergeEdges) {
            TRUEorFALSEseen = true;
            for (PDGEdge e : incoming) {
                PDGEdgeType t = e.getType();
                if (t != PDGEdgeType.MERGE) {
                    throw new RuntimeException("Should be all merge or all not merge " + current + " edge was " + e);
                }
                TRUEorFALSEseen &= getResults(e.getSource()).TRUEorFALSEseen;
            }
        } else {
            TRUEorFALSEseen = false;
            for (PDGEdge e : incoming) {
                PDGEdgeType t = e.getType();
                if (t != PDGEdgeType.TRUE && t != PDGEdgeType.FALSE) {
                    TRUEorFALSEseen |= getResults(e.getSource()).TRUEorFALSEseen;
                }
            }
        }
        
        // This was an optimization that would keep the number of PC nodes returned small
        // This does not work well with the semantics expected by the boolean expression stuff        
//        // We only keep the first true or false node on each path
//        if (TRUEorFALSEseen) {
//            Results res =  new Results(false, TRUEorFALSEseen);
//            return new OrderedPair<FindPCNodes.Results, Set<AbstractPDGNode>>(res, null);
//        }
        
        loop: for (PDGEdge e : incoming) {
            AbstractPDGNode n = e.getSource();
            switch(e.getType()) {
            
            // Copies (if we have a true one of these then the target is true) 
            case COPY : inSet = inSet || getResults(n).inSet; break;
            case INPUT : inSet = inSet || getResults(n).inSet; break;
            case OUTPUT : inSet = inSet || getResults(n).inSet; break; 
            // TRUE and FALSE are tricky
            // 1. If we have already seen an edge on all paths then its fine if 
            //    we see another one
            // 2. The node is in the set if we are looking for that type and
            //    the source is in the set
            // 3. If this we have now seen an edge means that don't need to consider this edge.
            //    This happens if we have a condition joining in with the one we care about 
            //    where the new one has a TRUE or FALSE and the one we care about has a CONJUNCTION
            case TRUE:
                // 1.
                if (TRUEorFALSEseen) {
                    break;
                }
                // 2.
                inSet = findTrue && getResults(n).inSet;
                // 3.
                TRUEorFALSEseen = inSet;
                break loop;
            case FALSE:
                // 1.
                if (TRUEorFALSEseen) {
                    break;
                }
                // 2.
                inSet = findFalse && getResults(n).inSet;
                // 3.
                TRUEorFALSEseen = inSet;
                break loop;
            // IMPLICIT takes us out of the PC graph and says nothing about
            // the original condition so ignore it
            case IMPLICIT : break;
            // PC CONJUNCTION copies the condition on the incoming edge, so if
            // any of the sources are in the set then the target is as well
            case CONJUNCTION : inSet = inSet || getResults(n).inSet; break;
            // We want to accept a POINTER target if there is also CONJUNCTION
            // with an accepted node, so the source has no effect
            // TODO can we also take it if the pointer is in the set?
            case POINTER : break;
            // SWITCH is the edge from the guard of a switch to a case-block. 
            // This is similar to a POINTER edge. We want to take it if there
            // also a CONJUNCTION edge with an accepted source.
            case SWITCH :  break;
            // TODO we could include && and ! here for more precision
            case EXP : inSet = false; break loop;
            // MISSING is the edge from arguments and "this" to the exit node 
            // for missing code so it is assumed to be some unknown function
            case MISSING : inSet = false; break loop;
            // Disjunction see below
            case MERGE : isMerge = true; break loop;
            default : throw new IllegalArgumentException("Did you add a new edge type and not tell me?");
            }
        }

        if (isMerge) {
            inSet = true;
            for (PDGEdge e : incoming) {
                inSet &= getResults(e.getSource()).inSet;
            }
        }

        Results r = new Results(inSet, TRUEorFALSEseen);
        return new OrderedPair<FindPCNodesRestrictor.Results, Set<AbstractPDGNode>>(r, null);
    }
    
    /**
     * Analysis results
     * <p>
     * 1. Is the node in the result set?
     * <p>
     * 2. Has a TRUE or FALSE edge been seen yet?
     */
    static class Results {
        /**
         * True if the node is in the set
         */
        boolean inSet;
        /**
         * True if a TRUE or FALSE edge has been see on all paths to this node
         */
        boolean TRUEorFALSEseen;
        
        /**
         * Create an object containing whether the node is in the result set and 
         * whether a true or false edge has been seen yet on every path
         * 
         * @param inSet
         *            True if the node is in the set
         * @param TRUEorFALSEseen
         *            True if a TRUE or FALSE edge has been see on all paths to
         *            this node
         */
        public Results(boolean inSet, boolean TRUEorFALSEseen) {
            this.inSet = inSet;
            this.TRUEorFALSEseen = TRUEorFALSEseen;
        }

        @Override
        public String toString() {
            return (inSet ? "(in set " : "(not in set") + ", " +
                    (TRUEorFALSEseen ? "seen)" : "not seen)");
        }

        @Override
        public int hashCode() {
            return (inSet ? 1 : 0) * 37 + (TRUEorFALSEseen ? 1 : 0) * 41;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Results)) {
                return false;
            }
            Results other = (Results) obj;
            return (this.inSet == other.inSet) &&
                    (this.TRUEorFALSEseen == other.TRUEorFALSEseen);
        }
    }
    
    @Override
    protected Results getResultForInput() {
        return new Results(true, false);
    }
    
    @Override
    protected Results getResultForNoInputEdges() {
        return new Results(false, false);
    }
    
    @Override
    protected Results defaultResultForMissing() {
        return new Results(false, false);
    }    
}
