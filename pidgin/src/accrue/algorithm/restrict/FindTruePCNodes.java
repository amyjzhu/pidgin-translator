package accrue.algorithm.restrict;

/**
 * The set of input nodes are nodes representing conditional expressions. This 
 * Restrictor finds the set of path conditions for which the given conditional expressions
 * are true.
 */
public class FindTruePCNodes extends FindPCNodesRestrictor  {

    /**
     * Default constructor
     */
    public FindTruePCNodes() {
        super(true, false);
    }

}
