package accrue.algorithm.restrict;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import accrue.algorithm.CFLShortestPath;
import accrue.pdg.PDGEdge;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.graph.PDGFactory;
import accrue.pdg.node.AbstractPDGNode;

public class CFLPath extends Restrictor {

    @Override
    public ProgramDependenceGraph restrict(ProgramDependenceGraph inputPDG, Set<AbstractPDGNode> ins,
                                           Set<AbstractPDGNode> outs) {
        List<PDGEdge> path = new CFLShortestPath().shortestPath(inputPDG, ins, outs);
        if (path == null) {
            // No path found
            return PDGFactory.emptyGraph();
        }
        return PDGFactory.graphFromEdges(new LinkedHashSet<>(path));
    }

}
