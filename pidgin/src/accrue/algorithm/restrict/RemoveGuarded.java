package accrue.algorithm.restrict;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import accrue.pdg.PDGEdge;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.graph.PDGFactory;
import accrue.pdg.node.AbstractPDGNode;
import accrue.pdg.node.ExprNode;
import accrue.util.OrderedPair;

/**
 * The input set must contain path condition nodes only. A path condition
 * represents a boolean condition. We want to remove any nodes from the input
 * PDG that is definitely guarded by some path condition in the input set. If
 * all paths into a node are guarded by some node in the input set then that
 * node will be removed.
 * <p>
 * To invert this a node in the input PDG is also in the output of this
 * Restrictor iff there exists a path in the input PDG into that node that may
 * not be guarded by one of the nodes in the input set.
 * <p>
 * If the input set are nodes representing access control checks then this
 * Restrictor will return all nodes for which it is possible that none of the
 * access control checks is successful.
 */
public class RemoveGuarded extends ForwardPass<Boolean> {

    @Override
    public ProgramDependenceGraph restrict(ProgramDependenceGraph inputPDG, Set<AbstractPDGNode> guards, Set<AbstractPDGNode> notUsed) {

        for (AbstractPDGNode n : guards) {
            if (!n.getNodeType().isPathCondition()) {
                throw new IllegalStateException("Only PC nodes are allowed in RemoveGuarded restrictor, " +
                		"hint: use FindTruePCNodes, FindFalsePCNodes, or FindPCNodes");
            }
        }
        
        return super.restrict(inputPDG, guards, notUsed);
    }

     @Override
    ProgramDependenceGraph produceGraph(ProgramDependenceGraph originalPDG, Map<AbstractPDGNode, Boolean> results) {
         // Remove anything in the result set. We only want bad nodes.
         Set<AbstractPDGNode> trueResults = new LinkedHashSet<AbstractPDGNode>();
         for (AbstractPDGNode n : results.keySet()) {
             if (results.get(n)) {
                 trueResults.add(n);
             }
         }
         
         System.out.println("\tRemoving " + trueResults.size() + " nodes");
         return PDGFactory.removeNodes(originalPDG, trueResults);
    }
     
    /**
     * Count of number of visits to each node, used for infinite loop detection
     */
    private Map<AbstractPDGNode,Integer> counts = new HashMap<AbstractPDGNode, Integer>();
    
    @Override
    protected OrderedPair<Boolean, Set<AbstractPDGNode>> computeResult(AbstractPDGNode current, Set<PDGEdge> incomingEdges) {

        Integer i = counts.get(current);
        if (i == null) {
            i = 1;
        } else {
            i++;
            counts.put(current, i);
            if (counts.get(current) > 100) {
                System.err.println("WARNING: (setting to false) possible infinite loop in RemoveGuarded: " + current);
                return new OrderedPair<Boolean, Set<AbstractPDGNode>>(false, null);
            }
        }
        
        boolean disjunctResult = false;
        boolean conjunctResult = true;
        boolean useConjunct = false;
        for (PDGEdge e : incomingEdges) {
            AbstractPDGNode n = e.getSource();
            
            Boolean b = getResults(n);
            disjunctResult |= b;
            conjunctResult &= b;
            
            switch(e.getType()) {
            // Copies any one is OK
            case COPY : 
            case INPUT : 
            case OUTPUT :    

            // True is like a copy
            case TRUE : 

            // False is OK since we already take the PC node from the guard, so
            // this is like a copy
            case FALSE : 

            // Implicit always guards the target so if the source is good then
            // we are good
            case IMPLICIT :

            // PC conjunction, this is a conjunction of booleans so if any are 
            // guarded then this one is
            case CONJUNCTION : 

            // For POINTER targets we want to keep it if it there is also a
            // CONJUNCTION source that is guarded, don't change the result
            case POINTER :

            // MISSING is the edge from arguments and "this" to the exit node 
            // for missing code, same as EXP, the function doesn't run and compute
            // the exit unless all the inputs are computed.
            case MISSING : 
                
            // SWITCH is the edge from the guard of a switch to a case-block. 
            // The case-block can only be reached if the guard is reached.
            case SWITCH :             
                break;

                
            // The target is some function of the input, but we don't know
            // which one, the result will only be computed if all the inputs are
            // computed so we can take the conjunction of all the incoming edges
            // TODO treat boolean short circuit specially
            case EXP : 
                if (current instanceof ExprNode) {
                    ExprNode expr = (ExprNode) current;
                    if (expr.isBinaryShortCircuit()) {
                        useConjunct = true;
                    }
                }
                break;
            
            // Disjunction, so all the inputs need to be true, handle this below
            case MERGE : useConjunct = true; break;
            default : throw new IllegalArgumentException("Did you add a new edge type and not tell me?");
            }
        }
        boolean res = useConjunct ? conjunctResult : disjunctResult;
        return new OrderedPair<Boolean, Set<AbstractPDGNode>>(res, null);
     }
    
    @Override
    protected Boolean getResultForInput() {
        return true;
    }

    @Override
    protected Boolean defaultResultForMissing() {
        return false;
    }
    
    @Override
    protected Boolean getResultForNoInputEdges() {
        return false;
    }
    
}
