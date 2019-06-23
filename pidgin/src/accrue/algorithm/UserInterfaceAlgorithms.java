package accrue.algorithm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import accrue.pdg.PDGEdge;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.node.AbstractPDGNode;

/**
 * Utility methods for certain graph common algorithms
 */
public class UserInterfaceAlgorithms {    
    /**
     * Compute edges between collapsed groups if there is an edge between any
     * two nodes in a group then an edge is created between those groups
     * 
     * @param pdg
     *            PDG we are computing in
     * @return JSON representation of the edges between groups
     * @throws JSONException
     *             trouble packing JSON object
     */
    public static JSONObject computeCollapsedEdges(ProgramDependenceGraph pdg) throws JSONException {
    	JSONObject m = new JSONObject();
    	Set<String> encounteredGroupings = new HashSet<String>();
    	Map<String, Set<String>> mappedEdges = new HashMap<String, Set<String>>();
    	for(PDGEdge e : pdg.edgeSet()) {
    		String source = e.getSource().groupingName();
    		String target = e.getTarget().groupingName();
    		encounteredGroupings.add(source);
    		encounteredGroupings.add(target);
    		Set<String> targetEdgeTypes = mappedEdges.get(source);
    		if (targetEdgeTypes == null) {
    			targetEdgeTypes = new HashSet<String>();
    			mappedEdges.put(source, targetEdgeTypes);
    		}
    		targetEdgeTypes.add(target);
    	}
    	
    	JSONArray arr = new JSONArray();
    	for (String k : mappedEdges.keySet()) {
    		for (String v : mappedEdges.get(k)) {
        		JSONObject edge = new JSONObject();
        		if (!k.equals(v) || encounteredGroupings.size() == 1) {
	        		edge.put("source", k);
	        		edge.put("target", v);
	        		edge.put("type", "function");
	        		arr.put(edge);
        		}
    		}
    	}
    	m.put("edges", arr);
    	return m;
    }
    
    /**
     * Get a JSON representation of any nodes in a group whose name contains
     * "grouping"
     * 
     * @param pdg
     *            PDG to pull nodes from
     * @param grouping
     *            substring of group name
     * @return JSON node representation ready for display
     * @throws JSONException
     *             trouble packing the JSON object
     */
    public static JSONObject getEdgesInGrouping(ProgramDependenceGraph pdg, String grouping) throws JSONException {
    	JSONObject m = new JSONObject();
    	JSONArray edges = new JSONArray();
    	for(PDGEdge e : pdg.edgeSet()) {
    		if (e.getSource().groupingName().equals(grouping) || e.getTarget().groupingName().equals(grouping)) {
	    		JSONObject edge = new JSONObject();
	    		edge.put("source", e.getSource().getNodeId());
	    		edge.put("target", e.getTarget().getNodeId());
	    		edge.put("sourceGrouping", e.getSource().groupingName());
	    		edge.put("targetGrouping", e.getTarget().groupingName());
	    		edge.put("type", e.getType().toString());
	    		edges.put(edge);
    		}
    	}
    	JSONObject nodes = new JSONObject();
    	for(AbstractPDGNode n : pdg.vertexSet()) {
    		if (n.groupingName().equals(grouping)) {
	    		JSONObject node = new JSONObject();
	    		node.put("name", n.getName());
	    		node.put("grouping", n.groupingName());
	    		nodes.put(Integer.toString(n.getNodeId()), node);
    		}
    	}
    	m.put("edges", edges);
    	m.put("nodes", nodes);
    	return m;
    }
}
