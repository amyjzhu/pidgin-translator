package accrue.algorithm.restrict;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import accrue.algorithm.SlicingAlgorithms;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.graph.PDGFactory;
import accrue.pdg.node.AbstractPDGNode;

/**
 * Find nodes in the input PDG that all paths from the inputs to the outputs
 * must pass through.  
 */
// TODO restrict to requested packages, need to make this a first class language construct to do this 
public class FindChokePoints extends Restrictor {

    @Override
    public ProgramDependenceGraph restrict(ProgramDependenceGraph inputPDG, Set<AbstractPDGNode> ins, Set<AbstractPDGNode> outs) {

        // If all paths go through a node then the shortest must
        ShortestPath sp = new ShortestPath();
        ProgramDependenceGraph path = sp.restrict(inputPDG, ins, outs);
        
        Set<AbstractPDGNode> chokes = new HashSet<>();
        
        // Now test all the nodes in the path, could be slow if the path is long
        for (AbstractPDGNode n : path.vertexSet()) {
            ProgramDependenceGraph removeN = PDGFactory.removeNodes(inputPDG, Collections.singleton(n));
            ProgramDependenceGraph fwdSlice = SlicingAlgorithms.forwardSlice(removeN, ins, null);
            ProgramDependenceGraph between = SlicingAlgorithms.backwardSlice(fwdSlice, outs, null);
            if (between.isEmpty()) {
                chokes.add(n);
            }
        }
        
        return PDGFactory.retainNodes(inputPDG, chokes);
    }

}
