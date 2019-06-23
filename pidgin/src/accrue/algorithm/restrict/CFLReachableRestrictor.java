package accrue.algorithm.restrict;

import java.util.Set;

import accrue.algorithm.CFLReachable;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.graph.PDGFactory;
import accrue.pdg.node.AbstractPDGNode;

public class CFLReachableRestrictor extends Restrictor {

    @Override
    public ProgramDependenceGraph restrict(ProgramDependenceGraph inputPDG, Set<AbstractPDGNode> ins,
                                           Set<AbstractPDGNode> outs) {
        return PDGFactory.retainNodes(inputPDG, new CFLReachable().computeReachable(inputPDG, ins, outs));
    }

}
