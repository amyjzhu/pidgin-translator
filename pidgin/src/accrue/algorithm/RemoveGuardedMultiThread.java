package accrue.algorithm;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import accrue.pdg.PDGEdgeType;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.graph.PDGFactory;
import accrue.pdg.node.AbstractPDGNode;

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
public class RemoveGuardedMultiThread extends ForwardPassMultiThread<Boolean> {

    
    /**
     * Create a new forward pass for the given initial graph and start nodes
     * 
     * @param inputPDG
     *            graph to compute in
     * @param inputSet
     *            set of start nodes
     */
    public RemoveGuardedMultiThread(ProgramDependenceGraph inputPDG, Set<AbstractPDGNode> inputSet) {
        super(inputPDG, inputSet);
        for (AbstractPDGNode n : inputSet) {
            if (!n.getNodeType().isPathCondition()) {
                throw new IllegalStateException("Only PC nodes are allowed in RemoveGuarded restrictor, " +
                        "hint: use FindTruePCNodes, FindFalsePCNodes, or FindPCNodes");
            }
        }
    }

     @Override
    protected ProgramDependenceGraph produceGraph(ProgramDependenceGraph originalPDG, Map<AbstractPDGNode, Boolean> results) {
         // Remove anything in the result set. We only want bad nodes.
         Set<AbstractPDGNode> trueResults = new LinkedHashSet<AbstractPDGNode>();
         for (AbstractPDGNode n : results.keySet()) {
             if (results.get(n)) {
                 trueResults.add(n);
             }
         }
         
         System.err.println("Removing " + trueResults.size() + " nodes");
         return PDGFactory.removeNodes(originalPDG, trueResults);
    }
     
//    /**
//     * Count of number of visits to each node, used for infinite loop detection
//     */
//    private Map<AbstractPDGNode,Integer> counts = new HashMap<AbstractPDGNode, Integer>();
   
    @Override
    protected Boolean computeResultFromDependencies(AbstractPDGNode current, Map<AbstractPDGNode, PDGEdgeType> dependencies) {

//        System.out.println(current + ":");
//        Integer i = counts.get(current);
//        if (i == null) {
//            i = 1;
//        } else {
//            i++;
//            counts.put(current, i);
//            if (counts.get(current) > 100) {
//                System.err.println("WARNING: (setting to false) possible infinite loop in RemoveGuarded: " + current);
//                return false;
//            }
//        }
        
//        for (AbstractPDGNode n : dependencies.keySet()) {
//            System.out.println("\t" + dependencies.get(n) + "..." + n + ":" + getResults(n));
//        }
        
        boolean result = false;
        boolean shouldAndAll = false;
        loop: for (AbstractPDGNode n : dependencies.keySet()) {
            switch(dependencies.get(n)) {
            // Copies any one is OK
            case COPY : result = result || getResults(n); break;
            case INPUT : result = result || getResults(n); break;
            case OUTPUT : result = result || getResults(n); break;   
            // True is like a copy
            case TRUE : result = result || getResults(n); break;
            // False is OK since we already take the PC node from the guard, so
            // this is like a copy
            case FALSE : result = result || getResults(n); break;
            // Implicit always guards the target so if the source is good then
            // we are good
            case IMPLICIT : result = result || getResults(n); break;
            // PC conjunction, this is a conjunction of booleans so if any are 
            // guarded then this one is
            case CONJUNCTION : result = result || getResults(n); break;
            // For POINTER targets we want to keep it if it there is also a
            // CONJUNCTION source that is guarded, don't change the result
            case POINTER : result = result || getResults(n); break;
            // The target is some function of the input, but we don't know
            // which one, the result will only be computed if all the inputs are
            // computed so we can take the conjunction of all the incoming edges
            case EXP : shouldAndAll = true; break loop;
            // MISSING is the edge from arguments and "this" to the exit node 
            // for missing code, same as EXP, the function doesn't run and compute
            // the exit unless all the inputs are computed.
            case MISSING : result = result || getResults(n); break;
            // Disjunction, so all the inputs need to be true, handle this below
            case MERGE : shouldAndAll = true; break loop;
            default : throw new IllegalArgumentException("Did you add a new edge type and not tell me?");
            }
        }

        if (shouldAndAll) {
            result = true;
            for (AbstractPDGNode n : dependencies.keySet()) {
                result &= getResults(n);
            }
        }
//        System.out.println("\tRESULT:" + result);
        return result;
     }
    
    @Override
    protected Boolean getResultForInput() {
        return true;
    }
    
    @Override
    protected Boolean getResultForNoInputEdge() {
        return false;
    }
}
