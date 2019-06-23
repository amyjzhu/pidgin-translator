package accrue.pdg.graph;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import accrue.pdg.PDGEdge;
import accrue.pdg.PDGEdgeType;
import accrue.pdg.node.AbstractPDGNode;
import accrue.pdg.node.PDGNodeFactory;
import accrue.pdg.util.CallSiteEdgeLabel;
import accrue.pdg.util.CallSiteEdgeLabel.SiteType;

/**
 * Edge in a program dependence graph. These (directed) edges represent a
 * dependence between two values in a program, represented by nodes in the PDG.
 */
public class Edge implements PDGEdge {

    /**
     * The source node of the edge. In the PDG, the value represented by the
     * <code>dest</code> node depends on the value represented by the
     * <code>source</code> node.
     */
    private final AbstractPDGNode source;
    /**
     * Destination node of the edge. In the PDG, the value represented by the
     * <code>dest</code> node depends on the value represented by the
     * <code>source</code> node.
     */
    private final AbstractPDGNode target;
    /**
     * Type of dependency this edge represents
     */
    private PDGEdgeType type;
    /**
     * compute once and reuse
     */
    private final int memoizedHashCode;
    /**
     * If this is an edge into or leaving a procedure call this labels the call
     * site
     */
    private final CallSiteEdgeLabel label;

    /**
     * Deserialize from JSON
     * 
     * @param json
     *            JSON created in Infoflow
     * 
     * @see accrue.infoflow.analysis.dependency.graph.node.VarPDGNode
     */
    protected Edge(JSONObject json) {
        try {
            // Lookup the unique ID for the source node
            this.source = PDGNodeFactory.nodeMap.get(json.getInt("source"));
            // Lookup the unique ID for the dest node
            this.target = PDGNodeFactory.nodeMap.get(json.getInt("dest"));
            // Type of edge we are creating
            this.type = PDGEdgeType.valueOf(json.getString("type"));
            if (json.has("label")) {
                // Label of the call or return edge
                this.label = edgeLabelFromJson(json.getJSONObject("label"));
            } else {
                this.label = null;
            }
            if (source == null) {
                throw new RuntimeException("Deserialization error edge source is null");
            }
            if (target == null) {
                throw new RuntimeException("Deserialization error edge dest is null");
            }
        } catch (JSONException e) {
            throw new RuntimeException("Deserialization error: " + e.getMessage());
        }
        memoizedHashCode = computeHashcode();
    }

    /**
     * Get the EdgeLabel for this edge from JSON
     * 
     * @param json
     *            Serialization data structure
     * @return edge label from the json
     */
    private CallSiteEdgeLabel edgeLabelFromJson(JSONObject json) {
        try {
            Set<Integer> receivers = null;
            if (json.has("receivers")) {
                receivers = new HashSet<>();
                JSONArray a = json.getJSONArray("receivers");
                for (int j = 0; j < a.length(); j++) {
                    receivers.add(a.getInt(j));
                }
            }
            return new CallSiteEdgeLabel(json.getInt("id"), SiteType.valueOf(json.getString("type")), receivers);
        }
        catch (JSONException e) {
            throw new RuntimeException("Deserialization error for EdgeLabel: " + e.getMessage());
        }
    }

    /**
     * Create an edge from its components. This edge will not automatically be
     * added to a PDG
     * 
     * @param source
     *            source node
     * @param target
     *            target node
     * @param type
     *            type/label of the edge
     * @param label
     *            label of entry or exit edge
     */
    public Edge(AbstractPDGNode source, AbstractPDGNode target, PDGEdgeType type, CallSiteEdgeLabel label) {
        this.source = source;
        this.target = target;
        this.type = type;
        this.label = label;
        memoizedHashCode = computeHashcode();
    }

    /**
     * Source node of the edge. The value represented by the <code>dest</code>
     * node depends on the value represented by the <code>source</code> node.
     * Checks whether the graph and edge are consistent.
     * 
     * @return Source node
     */
    public AbstractPDGNode getSource() {
        return source;
    }

    /**
     * Destination node of the edge. The value represented by the
     * <code>dest</code> node depends on the value represented by the
     * <code>source</code> node. Checks whether the graph and edge are
     * consistent.
     * 
     * @return Destination node
     */
    public AbstractPDGNode getTarget() {
        return target;
    }

    /**
     * Type of dependency this edge represents
     * 
     * @return Type of dependency
     */
    public PDGEdgeType getType() {
        return type;
    }

    @Override
    public String toString() {
        return (label != null ? label.getType() + "-" + label.getCallSiteID()  + " ": "") + source + " (" + source.getNodeType() + ") -> " + target + " (" + target.getNodeType() + ") [" + type + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PDGEdge)) {
            return false;
        }
        PDGEdge other = (PDGEdge) obj;
        return source.equals(other.getSource())
                && target.equals(other.getTarget())
                && type.equals(other.getType())
                && (label == null ? other.getEdgeLabel() == null
                        : label.equals(other.getEdgeLabel()));
    }

    /**
     * Compute once and reuse
     * 
     * @return hashcode for memoization
     */
    private int computeHashcode() {
        int firstHash = source.hashCode();
        // flip the bits so (source,target) has a different hash than
        // (target,source)
        return (firstHash >>> 16 | firstHash << 16) ^ target.hashCode() ^ type.hashCode() ^ (label != null ? label.hashCode() : 17);
    }

    @Override
    public int hashCode() {
        return memoizedHashCode;
    }

    @Override
    public CallSiteEdgeLabel getEdgeLabel() {
        return label;
    }

    @Override
    public void changeEdgeType(PDGEdgeType newType) {
        assert this.type == PDGEdgeType.MERGE && (newType == PDGEdgeType.CONJUNCTION || newType == PDGEdgeType.COPY) : "Should only be replacing edge types imperatively when replacing unecessary merge edges.";
        this.type = newType;
    }
}
