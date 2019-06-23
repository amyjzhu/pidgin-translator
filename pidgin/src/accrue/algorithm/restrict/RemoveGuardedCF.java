package accrue.algorithm.restrict;

import java.util.HashMap;
import java.util.HashSet;
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
 * <p>
 * For procedure calls, if any of the entry nodes (sources of procedure entry edges)
 * are guarded then all exit nodes (targets of exit edges) are guarded).
 */
public class RemoveGuardedCF extends ForwardPass<Boolean> {
   
    /**
     * Graph we are removing nodes from
     */
    private ProgramDependenceGraph inputPDG;

    @Override
    public ProgramDependenceGraph restrict(ProgramDependenceGraph inputPDG, Set<AbstractPDGNode> guards, Set<AbstractPDGNode> notUsed) {

        for (AbstractPDGNode n : guards) {
            if (!n.getNodeType().isPathCondition()) {
                throw new IllegalStateException("Only PC nodes are allowed in RemoveGuarded restrictor, " +
                		"hint: use FindTruePCNodes, FindFalsePCNodes, or FindPCNodes");
            }
        }
        
        this.inputPDG = inputPDG;
        
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
         
         System.err.println("\tRemoving " + trueResults.size());
         return PDGFactory.removeNodes(originalPDG, trueResults);
    }
     
    /**
     * Count of number of visits to each node, used for infinite loop detection
     */
    private Map<AbstractPDGNode,Integer> counts = new HashMap<AbstractPDGNode, Integer>();
    
    /**
     * Descriptions of call sites that are guarded
     */
    private Set<Integer> guardedEntries = new HashSet<>();
    
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
                return new  OrderedPair<Boolean, Set<AbstractPDGNode>>(false, null);
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

            // TRUE is like a copy
            case TRUE : 

            // FALSE is OK since we already have the PC node (from the guard), so
            // this is like a copy
            case FALSE : 

            // IMPLICIT always guards the target so if the source is good then
            // we are good
            case IMPLICIT :

            // PC conjunction, this is a conjunction of booleans so if any are 
            // guarded then this one is
            case CONJUNCTION : 

            // POINTER edge is from the receiver to a field access. If the receiver is
            // guarded then so is the access itself.
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
            // which one
            // -If this is a binary short-circuit then we assume the result will only be computed if all the inputs are
            // computed so we can take the conjunction of all the incoming edges
            // -Otherwise this will only be computed if all arguments are computed
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
            default : throw new IllegalArgumentException("Did you add a new edge type and not tell me? Edge: " + e);
            }
        }
        boolean res = useConjunct ? conjunctResult : disjunctResult;
        if (!res) {
            if (inputPDG.getAllExitAssignmentNodes().contains(current)) {
                if (guardedEntries.contains(inputPDG.getExitAssignmentSite(current))) {
                    // If an entry node is guarded then the corresponding exit nodes
                    // are as well.
                    // This does not depend on the incoming edges
                    return new OrderedPair<Boolean, Set<AbstractPDGNode>>(true, null);
                }
            }
        }
        
        // Handle entry nodes
        if (res && inputPDG.getAllFormalAssignmentNodes().contains(current)) {
            // Add all call sites this is the entry node for to the set of 
            // guarded call sites
            Set<Integer> descs = inputPDG.getFormalAssignmentSites(current);
            guardedEntries.addAll(descs);
                        
            // if current is in guarded and it is an entry then all corresponding outputs are guarded
            Set<AbstractPDGNode> exits = new HashSet<>();
            for (Integer desc : descs) {
                Set<AbstractPDGNode> nodesForDesc = inputPDG.getExitAssignmentNodes(desc);
                if (nodesForDesc != null) {
                    exits.addAll(nodesForDesc);
                }
            }
            
            Set<AbstractPDGNode> addToQ = new HashSet<>();
            for (AbstractPDGNode n : exits) {
                if (!getResults(n)) {
                    // Add any exit nodes for processing
                    addToQ.add(n);
                }
            }
            return new OrderedPair<Boolean, Set<AbstractPDGNode>>(res, addToQ);
        }
        
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
