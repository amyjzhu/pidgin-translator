package accrue.pdg.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import accrue.pdg.PDGEdge;
import accrue.pdg.PDGEdgeType;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.node.AbstractPDGNode;
import accrue.pdg.node.PDGNodeType;
import accrue.util.SetDisjointUnion;
import accrue.util.SetIntersect;
import accrue.util.SetMinus;
import accrue.util.SetWrapper;

/**
 * Graph with singleton merge edges into a target replaced with corresponding
 * COPY (for expressions) or CONJUNCTION (for PC nodes) edges
 */
public class GraphUnnecessaryMerges extends AbstractGraph implements ProgramDependenceGraph {

    /**
     * map from edges with MERGE to either CONJUGATION or COPY
     */
    private final Map<PDGEdge, PDGEdge> unnecessaryMerges;
    /**
     * Edges replaced with CONJUNCTION edges
     */
    private Set<PDGEdge> unnecessaryMergesConj;
    /**
     * Edges replaced with COPY edges
     */
    private Set<PDGEdge> unnecessaryMergesCopy;

    /**
     * Original graph with no edges replaced
     */
    private final ProgramDependenceGraph orig;
    
    /**
     * The semantics of MERGE edges is that the target represents a value that
     * is equal exactly one of the incoming values, but which one is, in
     * general, not known and not necessarily the same for different executions
     * or for iterations of a loop. If there is only one MERGE edge then it is
     * equivalent to a COPY edge. Replace all such MERGE edges with COPY edges.
     * 
     * @param pdg
     *            PDG in which to replace the singleton MERGE edges with COPY
     *            edges. This graph will be permantly modified
     */
    public static void constructImperative(ProgramDependenceGraph pdg) {
        int count = 0;
        for (PDGEdge e : pdg.getEdgesOfType(PDGEdgeType.MERGE)) {
            Set<PDGEdge> in = pdg.incomingEdgesOf(e.getTarget());
            if (in.size() == 1) {
                // this is an unnecessary merge edge! The target node
                // has only one incoming merge edge.
                // We can replace it with either a CONJUNCTION or COPY as
                // appropriate
                if (e.getTarget().getNodeType().isPathCondition()) {
                    count++;
                    e.changeEdgeType(PDGEdgeType.CONJUNCTION);
                }
                else {
                    count++;
                    e.changeEdgeType(PDGEdgeType.COPY);
                }
            } else if (in.size() == 2) {
                if (hasImplicit(in)) {
                    // this is an unnecessary merge edge! The target node
                    // has only one incoming merge edge and another implicit
                    // edge.
                    // We can replace it with either a CONJUNCTION or COPY as
                    // appropriate
                    if (e.getTarget().getNodeType().isPathCondition()) {
                        throw new RuntimeException("PC nodes should not have implicit edges");
                    }
                    else {
                        count++;
                        e.changeEdgeType(PDGEdgeType.COPY);
                    }
                }
            }
        }
        System.err.println("Replaced " + count + " MERGE edges with COPY or CONJUNCTION edges.");
    }

    /**
     * The semantics of MERGE edges is that the target represents a value that
     * is equal exactly one of the incoming values, but which one is, in
     * general, not known and not necessarily the same for different executions
     * or for iterations of a loop. If there is only one MERGE edge then it is
     * equivalent to a COPY edge. Replace all such MERGE edges with COPY edges.
     * 
     * @param pdg
     *            PDG in which to replace the singleton MERGE edges with COPY
     *            edges
     * @return new PDG with the singleton MERGE edges replaced with copy edges
     */
    public static ProgramDependenceGraph construct(ProgramDependenceGraph pdg) {
        Map<PDGEdge, PDGEdge> unnecessaryMerges = new HashMap<PDGEdge, PDGEdge>();
        Set<PDGEdge> unnecessaryMergesConj = new HashSet<PDGEdge>();
        Set<PDGEdge> unnecessaryMergesCopy = new HashSet<PDGEdge>();

        for (PDGEdge e : pdg.getEdgesOfType(PDGEdgeType.MERGE)) {
            Set<PDGEdge> in = pdg.incomingEdgesOf(e.getTarget());
            if (in.size() == 1) {
                // this is an unnecessary merge edge! The target node
                // has only one incoming merge edge.
                // We can replace it with either a CONJUNCTION or COPY as
                // appropriate
                if (e.getTarget().getNodeType().isPathCondition()) {
                    PDGEdge newEdge = PDGFactory.edge(e.getSource(), e.getTarget(), PDGEdgeType.CONJUNCTION, e.getEdgeLabel());
                    unnecessaryMergesConj.add(newEdge);
                    unnecessaryMerges.put(e, newEdge);
                }
                else {
                    PDGEdge newEdge = PDGFactory.edge(e.getSource(), e.getTarget(), PDGEdgeType.COPY, e.getEdgeLabel());
                    unnecessaryMergesCopy.add(newEdge);
                    unnecessaryMerges.put(e, newEdge);
                }
            } else if (in.size() == 2) {
                if (hasImplicit(in)) {
                    // this is an unnecessary merge edge! The target node
                    // has only one incoming merge edge and another implicit
                    // edge.
                    // We can replace it with either a CONJUNCTION or COPY as
                    // appropriate
                    if (e.getTarget().getNodeType().isPathCondition()) {
                        throw new RuntimeException("PC nodes should not have implicit edges");
                    }
                    else {
                        PDGEdge newEdge = PDGFactory.edge(e.getSource(), e.getTarget(), PDGEdgeType.COPY, e.getEdgeLabel());
                        unnecessaryMergesCopy.add(newEdge);
                        unnecessaryMerges.put(e, newEdge);
                    }
                }
            }
        }

        if (unnecessaryMerges.isEmpty()) {
            return pdg;
        }

        System.err.println("GraphUnnecssaryMerges has " + unnecessaryMerges.keySet().size() + " unnec merge edges");
        return new GraphUnnecessaryMerges(pdg, unnecessaryMerges, unnecessaryMergesConj, unnecessaryMergesCopy);
    }

    /**
     * True if there is an input edge labeled with {@link PDGEdgeType#IMPLICIT}
     * 
     * @param in
     *            input edges
     * @return true if one of the input edges is IMPLICIT
     */
    private static boolean hasImplicit(Set<PDGEdge> in) {
        for (PDGEdge e : in) {
            if (e.getType() == PDGEdgeType.IMPLICIT) {
                return true;
            }
        }
        return false;
    }

    /**
     * Constructor used in {@link #construct(ProgramDependenceGraph)}
     * 
     * @param pdg
     *            original graph
     * @param unnecessaryMerges2
     *            map of edges that were replaced to the replacing edges
     * @param unnecessaryMergesConj2
     *            set of edges replaced with CONJUNCTION
     * @param unnecessaryMergesCopy2
     *            set of edges replaced with COPY
     */
    private GraphUnnecessaryMerges(ProgramDependenceGraph pdg, Map<PDGEdge, PDGEdge> unnecessaryMerges2, Set<PDGEdge> unnecessaryMergesConj2, Set<PDGEdge> unnecessaryMergesCopy2) {
        this.orig = pdg;
        this.unnecessaryMerges = unnecessaryMerges2;
        this.unnecessaryMergesConj = unnecessaryMergesConj2;
        this.unnecessaryMergesCopy = unnecessaryMergesCopy2;
    }

    @Override
    public Set<PDGEdge> outgoingEdgesOf(AbstractPDGNode n) {
        return new SetWrapper<PDGEdge>(orig.outgoingEdgesOf(n), unnecessaryMerges, false);
    }

    @Override
    public Set<PDGEdge> incomingEdgesOf(AbstractPDGNode n) {
        return new SetWrapper<PDGEdge>(orig.incomingEdgesOf(n), unnecessaryMerges, false);
    }

    @Override
    public Set<PDGEdge> edgeSet() {
        return new SetWrapper<PDGEdge>(orig.edgeSet(), unnecessaryMerges, true);
    }

    @Override
    public Set<AbstractPDGNode> vertexSet() {
        return orig.vertexSet();
    }

    @Override
    public AbstractPDGNode getNodeById(Integer id) {
        return orig.getNodeById(id);
    }

    @Override
    public Set<PDGEdge> getEdgesOfType(PDGEdgeType t) {
        if (PDGEdgeType.MERGE == t) {
            return new SetMinus<PDGEdge>(orig.getEdgesOfType(t), unnecessaryMerges.keySet(), true);
        }
        else if (PDGEdgeType.CONJUNCTION == t && !unnecessaryMergesConj.isEmpty()) {
            return new SetDisjointUnion<PDGEdge>(orig.getEdgesOfType(t), unnecessaryMergesConj);
        }
        else if (PDGEdgeType.COPY == t && !unnecessaryMergesCopy.isEmpty()) {
            return new SetDisjointUnion<PDGEdge>(orig.getEdgesOfType(t), unnecessaryMergesCopy);
        }
        else {
            return orig.getEdgesOfType(t);
        }
    }

    @Override
    public Set<PDGEdge> getEdgesByNodeType(PDGNodeType t) {
        return new SetIntersect<PDGEdge>(this.orig.getEdgesByNodeType(t), this.edgeSet(), false);
    }

    @Override
    public Set<AbstractPDGNode> getNodeOfType(PDGNodeType t) {
        return orig.getNodeOfType(t);
    }

    @Override
    public boolean containsEdge(PDGEdge e) {
        if (unnecessaryMerges.values().contains(e))
            return true;
        if (unnecessaryMerges.keySet().contains(e))
            return false;
        return orig.containsEdge(e);
    }

    @Override
    public Set<String> getAllProcedureNames() {
        return orig.getAllProcedureNames();
    }

    @Override
    public Set<AbstractPDGNode> getNodesByProcedure(String procedureName) {
        return orig.getNodesByProcedure(procedureName);
    }

    @Override
    public Set<AbstractPDGNode> getExitSummaryNodes(Integer desc) {
        return orig.getExitSummaryNodes(desc);
    }

    @Override
    public Set<AbstractPDGNode> getExitAssignmentNodes(Integer desc) {
        return orig.getExitAssignmentNodes(desc);
    }

    @Override
    public Set<Integer> getFormalAssignmentSites(AbstractPDGNode node) {
        return orig.getFormalAssignmentSites(node);
    }

    @Override
    public Integer getExitAssignmentSite(AbstractPDGNode node) {
        return orig.getExitAssignmentSite(node);

    }

    @Override
    public Set<AbstractPDGNode> getAllFormalAssignmentNodes() {
        return orig.getAllFormalAssignmentNodes();
    }

    @Override
    public Set<AbstractPDGNode> getAllExitAssignmentNodes() {
        return orig.getAllExitAssignmentNodes();
    }
}
