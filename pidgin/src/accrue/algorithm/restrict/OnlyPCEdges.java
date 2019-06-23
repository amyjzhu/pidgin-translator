package accrue.algorithm.restrict;

import java.util.LinkedHashSet;
import java.util.Set;

import accrue.pdg.PDGEdge;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.graph.PDGFactory;
import accrue.pdg.node.AbstractPDGNode;

/**
 * Restricts an input graph to only edges with a PC node at one end or the
 * other.
 */
public class OnlyPCEdges extends Restrictor {

    @Override
    public ProgramDependenceGraph restrict(ProgramDependenceGraph inputPDG, Set<AbstractPDGNode> ins, Set<AbstractPDGNode> outs) {
        Set<PDGEdge> edges = new LinkedHashSet<PDGEdge>();
        for (PDGEdge e : inputPDG.edgeSet()) {
            if (e.getSource().getNodeType().isPathCondition() || e.getTarget().getNodeType().isPathCondition()) {
                edges.add(e);
            }
        }
        return PDGFactory.graphFromEdges(edges);
    }
}
