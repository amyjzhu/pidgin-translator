package accrue.algorithm.restrict;

/**
 * The set of input nodes are nodes representing conditional expressions. This 
 * Restrictor finds the set of path conditions for which the given conditional expressions
 * are false.
 */
public class FindFalsePCNodes extends FindPCNodesRestrictor {

    /**
     * Default constructor
     */
    public FindFalsePCNodes() {
        super(false, true);
    }   
}
