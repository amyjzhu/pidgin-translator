package accrue.server;

/**
 * Type for JSON response to client
 */
public enum ResponseType {
    /**
     * Set of nodes
     */
	NODES, 
	/**
	 * Set of edges
	 */
	EDGES,
	/**
	 * Graph view
	 */
	GRAPH,
	/**
	 * List of strings
	 */
	SUMMARY;
}