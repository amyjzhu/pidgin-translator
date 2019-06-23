package accrue.algorithm.restrict;

import java.util.Set;

import accrue.algorithm.ShortestPathAlgorithm;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.node.AbstractPDGNode;

/**
 * Restrict the PDG to the shortest path between any node in ins and any node in
 * outs
 */
public class ShortestPath extends Restrictor {

    @Override
    public ProgramDependenceGraph restrict(ProgramDependenceGraph inputPDG, Set<AbstractPDGNode> ins, Set<AbstractPDGNode> outs) {
        return ShortestPathAlgorithm.shortestPath(inputPDG, ins, outs);
    }

}
