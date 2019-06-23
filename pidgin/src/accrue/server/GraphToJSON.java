package accrue.server;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import accrue.pdg.PDGEdge;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.node.AbstractPDGNode;
import accrue.pdg.util.CallSiteEdgeLabel;

/**
 * Convert a PDG into a JSONObject for the GUI
 */
public class GraphToJSON {
    
    /**
     * Max number of items printed in client GUI list
     */
    private static final int MAX_LIST_SIZE = 10000;

    /**
     * Convert the graph into JSON, results are different based on the size
     * 
     * @param graph
     *            graph to convert
     * @param t
     *            response type if selected, null otherwise (and the default
     *            will be used)
     *            
     * @return JSON response object
     * @throws JSONException
     *             thrown if there is a JSON error
     */
    public static JSONObject processToJSON(ProgramDependenceGraph graph, ResponseType t) throws JSONException {
        JSONObject o = new JSONObject();

        
        if (graph.vertexSet().size() > MAX_LIST_SIZE || graph.edgeSet().size() > MAX_LIST_SIZE) {
            t = ResponseType.SUMMARY;
        } else {
            if (t == null) {
                boolean noEdge = false;
                for (AbstractPDGNode n : graph.vertexSet()) {
                    noEdge = graph.incomingEdgesOf(n).isEmpty()
                            && graph.outgoingEdgesOf(n).isEmpty();
                    if (noEdge) {
                        t = ResponseType.NODES;
                        break;
                    }
                }
                if (!noEdge) {
                    t = ResponseType.EDGES;
                }
            }
        }

        switch (t) {
        case NODES:
            nodesToJson(o, graph.vertexSet());
            break;
        case EDGES:
            edgesToJson(o, graph.edgeSet());
            break;
        case SUMMARY:
            summaryToJson(o, graph.getStats());
            break;
        default:
            throw new IllegalStateException("not possible");
        }

        o.put("type", t.toString());
        
        return o;
    }

    /**
     * Convert a list of strings into a {@link JSONObject}
     * 
     * @param o
     *            JSON object to add the list to
     * @param result
     *            results to add
     * @throws JSONException
     *             JSON issue
     */
    private static void summaryToJson(JSONObject o, List<String> result) throws JSONException {
        JSONArray a = new JSONArray();
        for (String res : result) {
            a.put(res);
        }
        o.put("results", a);

    }

    /**
     * Convert a set of edges to a {@link JSONObject}
     *  
     * @param o
     *            JSON object to add the list to
     * @param result
     *            results to add
     * @throws JSONException
     *             JSON issue
     */
    private static void edgesToJson(JSONObject o, Set<PDGEdge> result) throws JSONException {
        JSONArray a = new JSONArray();
        for (PDGEdge e : result) {
            JSONObject jsonEdge = new JSONObject();
            AbstractPDGNode t = e.getTarget();
            AbstractPDGNode s = e.getSource();
            jsonEdge.put("fromName", nodeName(s));
            jsonEdge.put("toName", nodeName(t));
            CallSiteEdgeLabel label = e.getEdgeLabel();
            String typeString = e.getType().toString();
            if (label != null) {
                typeString += "<br><br>" + label.getType() + "_" + label.getCallSiteID();
                Set<Integer> rec = label.getReceivers();
                if (rec != null) {
                    typeString += "<br><br>" +  "Receivers: " + rec;
                }
            }
            jsonEdge.put("type", typeString);
            a.put(jsonEdge);
        }
        o.put("edges", a);
    }

    /**
     * Convert a set of nodes to a {@link JSONObject}
     *  
     * @param o
     *            JSON object to add the list to
     * @param result
     *            results to add
     * @throws JSONException
     *             JSON issue
     */
    private static void nodesToJson(JSONObject o, Set<AbstractPDGNode> result) throws JSONException {
        JSONArray a = new JSONArray();
        for (AbstractPDGNode n : result) {
            JSONObject jsonNode = new JSONObject();
            jsonNode.put("name",  nodeName(n));
            jsonNode.put("nodeId", n.getNodeId());
            jsonNode.put("grouping", StringEscapeUtils.escapeHtml4(n.getProcedureName()) + "<br><br>"
                    + StringEscapeUtils.escapeHtml4(n.getContext()));
            a.put(jsonNode);
        }
        o.put("nodes", a);

    }
    
    private static String nodeName(AbstractPDGNode n) {
        StringBuilder nameField = new StringBuilder();
        nameField.append(n.getNodeId());
        nameField.append(" ");
        nameField.append(StringEscapeUtils.escapeHtml4(n.getName()));
        nameField.append("<br><br>");
        if (n.getPosition() != null) {
            nameField.append(StringEscapeUtils.escapeHtml4(n.getPosition().toString()));
            nameField.append("<br><br>");
        }
        nameField.append(n.getNodeType());
        nameField.append("<br><br>");
        nameField.append(StringEscapeUtils.escapeHtml4(n.getProcedureName()));
        nameField.append("<br><br>");
        nameField.append(StringEscapeUtils.escapeHtml4(n.getContext()));
        nameField.append("<br><br>");
        nameField.append(StringEscapeUtils.escapeHtml4(n.getJavaType()));
        return nameField.toString();
    }

}
