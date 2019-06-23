package accrue.algorithm.restrict;

import java.util.Set;

import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.node.AbstractPDGNode;

/**
 * Cheat to clear the cache on the input PDG
 */
public class ClearCache extends Restrictor {

    @Override
    public ProgramDependenceGraph restrict(ProgramDependenceGraph inputPDG, Set<AbstractPDGNode> ins,
                                           Set<AbstractPDGNode> outs) {
        inputPDG.clearAllCachedResults();
        return inputPDG;
    }

}
