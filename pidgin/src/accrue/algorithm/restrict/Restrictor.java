package accrue.algorithm.restrict;

import java.util.Set;

import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.node.AbstractPDGNode;

/**
 * Interface for all graph "restrictors" these are general graph transformers
 * that take in one graph and produce another. Implementing classes should only
 * remove nodes and edges from the input graph never add them.
 */
public abstract class Restrictor {

    /**
     * Restrict the input PDG and return a new PDG with possibly fewer edges
     * and/or nodes
     * 
     * @param inputPDG
     *            PDG to be restricted
     * @param ins
     *            Set of input nodes, what these are used for depends on the
     *            implementing class
     * @param outs
     *            Set of output nodes, what these are used for depends on the
     *            implementing class
     * @return a restricted PDG
     */
    public abstract ProgramDependenceGraph restrict(ProgramDependenceGraph inputPDG, Set<AbstractPDGNode> ins, Set<AbstractPDGNode> outs);
}
