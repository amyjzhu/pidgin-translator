package accrue.pdg.node;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import accrue.pdg.util.GeneratedExceptionType;
import accrue.pdg.util.Position;
import accrue.util.JSONHelper;

/**
 * Create PDG nodes from their JSON representations
 */
public class PDGNodeFactory {
    
    /**
     * Map from node ID to node used by PDG edges to look up the source and destinations
     */
    public static final Map<Integer, AbstractPDGNode> nodeMap = new HashMap<>();

    /**
     * Create the node corresponding to the given JSON
     * 
     * @param json
     *            JSON formatted node description to be deserialized
     * @return a new PDG node
     */
    public static AbstractPDGNode create(JSONObject json) {
        PDGNodeClassName className;
        try {
            // Name of the class we want to create a node for
            className = PDGNodeClassName.valueOf(json.getString("class"));
        } catch (JSONException e) {
            throw new RuntimeException("Deserialization error: " + e.getMessage());
        }
        
        AbstractPDGNode n;
        switch (className) {
        case ABSTRACT_LOCATION: n = createAbstractLocationNode(json); break;
        case EXPR: n = createExprNode(json); break;
        case MISSING_CODE: n = createMissingCodeNode(json); break;
//        case PROCEDURE: n = createProcedureNode(json); break;
        default:
            throw new RuntimeException("Invalid class name when deserializing: " + className);
        }
        nodeMap.put(n.getNodeId(), n);
        return n;
    }

    /**
     * Create an {@link AbstractLocationNode} this is the only way to create
     * such a node
     * 
     * @param json
     *            JSON to deserialize into a PDG node for an abstract location
     * @return a new PDG node for an abstract location
     */
    private static AbstractLocationNode createAbstractLocationNode(JSONObject json) {
        AbstractPDGNodeInfo info = new AbstractPDGNodeInfo(json);
        try {
            // String representation of the abstract location
            String location = JSONHelper.getCanonical(json.getString("location"));
            return new AbstractLocationNode(info, location);
        } catch (JSONException e) {
            throw new RuntimeException("Deserialization error: " + e.getMessage());
        }
    }

    /**
     * Create an {@link ExprNode} this is the only way to create such a node
     * 
     * @param json
     *            JSON to deserialize into a PDG node for an AST node
     * @return a new PDG node for an AST node
     */
    private static ExprNode createExprNode(JSONObject json) {
        AbstractPDGNodeInfo info = new AbstractPDGNodeInfo(json);
        try {
            // Fully qualified name for the method/constructor/initializer this
            // node was created in
            String codeName = JSONHelper.getCanonical(json.getString("code"));

            GeneratedExceptionType exType = null;
            if (json.has("exception")) {
                // If this node represents an exception thrown by a runtime
                // system this is the type of that exception
                exType = GeneratedExceptionType.valueOf(json.getString("exception"));
            }
            String paramName = null;
            if (json.has("paramName")) {
                // If this node represents the assignment to a formal parameter
                // then this is the name of that parameter
                paramName = JSONHelper.getCanonical(json.getString("paramName"));
            }
            boolean isBinaryShortCircuit = false;
            if (json.has("isShortCircuit")) {
                // if this node represents a binary && or ||
                isBinaryShortCircuit = json.getBoolean("isShortCircuit");
            }
            String exitKey = null;
            if (json.has("exitkey")) {
                // if this is a procedure exit node in the caller this is the CFG edge key
                exitKey = json.getString("exitkey");
            }
            return new ExprNode(info, codeName, exType, paramName, isBinaryShortCircuit, exitKey);
        } catch (JSONException e) {
            throw new RuntimeException("Deserialization error: " + e.getMessage());
        }
    }

    /**
     * Create an {@link MissingCodeNode} this is the only way to create such a
     * node
     * 
     * @param json
     *            JSON to deserialize into a PDG node for missing code
     * @return a new PDG node for missing code
     */
    private static MissingCodeNode createMissingCodeNode(JSONObject json) {
        AbstractPDGNodeInfo info = new AbstractPDGNodeInfo(json);
        try {
            // Name of the procedure that was missing
            String procedureName = JSONHelper.getCanonical(json.getString("procedure"));
            // True if the receiver was missing (empty points-to set)
            boolean isMissingReceiver = json.getBoolean("missingreceiver");
            String exitKey = null;
            if (json.has("exitkey")) {
                // If this node was the summary for a procedure exit then this
                // is the exit key (i.e. "KEY_NORM_TERM" or an exception key
                exitKey = JSONHelper.getCanonical(json.getString("exitkey"));
            }
            return new MissingCodeNode(info, procedureName, isMissingReceiver, exitKey);
        } catch (JSONException e) {
            throw new RuntimeException("Deserialization error: " + e.getMessage());
        }
    }

    /**
     * Create an {@link ProcedureNode} this is the only way to create such a
     * node
     * 
     * @param json
     *            JSON to deserialize into a PDG node for a procedure summary
     * @return a new PDG node for a procedure summary
     */
//    private static ProcedureNode createProcedureNode(JSONObject json) {
//        AbstractPDGNodeInfo info = new AbstractPDGNodeInfo(json);
//        try {
//            // Fully qualified name for the method/constructor/initializer this
//            // node was created in
//            String codeName = JSONHelper.getCanonical(json.getString("code"));
////            // String representatino of an analysis unit includes
////            // procedure/initializer name and context description
////            String analysisUnitName = json.getString("au");
//            String exitKey = null;
//            if (json.has("exitkey")) {
//                // If this node was the summary for a procedure exit then this
//                // is the exit key (i.e. "KEY_NORM_TERM" or an exception key
//                exitKey = JSONHelper.getCanonical(json.getString("exitkey"));
//            }
//            String paramName = null;
//            if (json.has("paramName")) {
//                // If this node is the summary node for a formal parameter
//                // then this is the name of that parameter
//                paramName = JSONHelper.getCanonical(json.getString("paramName"));
//            }
//            return new ProcedureNode(info, codeName, exitKey, paramName);
//        } catch (JSONException e) {
//            throw new InternalCompilerError("Deserialization error: " + e.getMessage());
//        }
//    }

    /**
     * Class used as a container for data that is common to all PDG nodes
     */
    protected static class AbstractPDGNodeInfo {

        /**
         * Unpack JSON in common to all PDG nodes. See field descriptions below
         * for a description if the JSON format.
         * 
         * @param json
         *            JSON to deserialize
         */
        AbstractPDGNodeInfo(JSONObject json) {
            try {
                if (json.has("position") && json.get("position") != JSONObject.NULL) {
                    position = JSONHelper.positionFromJSON(json.getJSONObject("position"));
                } else {
                    position = null;
                }
                nodeID = json.getInt("nodeid");
                name = JSONHelper.getCanonical(json.getString("name"));
                nodeType = PDGNodeType.valueOf(json.getString("type"));
                groupingName = JSONHelper.getCanonical(json.getString("group"));
                if (json.has("context")) {
                    context = JSONHelper.getCanonical(json.getString("context"));
                } else {
                    context = "null";
                }
                if (json.has("javatype")) {
                    javaType = JSONHelper.getCanonical(json.getString("javatype"));
                } else {
                    javaType = null;
                }
            } catch (JSONException e) {
                throw new RuntimeException("Deserialization error: " + e.getMessage());
            }
        }

        /**
         * Position in the source code related to this node in the PDG
         */
        protected final Position position;
        /**
         * Unique nodeID, used to determine node equality
         */
        protected final int nodeID;
        /**
         * name PDG node (just used as a label)
         */
        protected final String name;
        /**
         * Type of the PDG node
         */
        protected final PDGNodeType nodeType;
        /**
         * Name used to group nodes for presentation.
         */
        protected final String groupingName;

        /**
         * String representation of the analysis context or the String "null" if
         * there is none
         */
        protected final String context;
        /**
         * Java type of the expression represented by the node if any, null if there is none
         */
        protected final String javaType;
    }
}
