package accrue.pdg.util;

import java.util.Set;

/**
 * Label for an edge from a call site or to a return site.
 */
public class CallSiteEdgeLabel {

    /**
     * Position and context of call or return site
     */
    private final Integer callSiteID;
    /**
     * Receivers for the call
     */
    private final Set<Integer> receivers;

    /**
     * Create a new label for this entry or exit edge
     * 
     * @param siteID Integer ID for call or return site
     * @param type Indication of whether this is an entry or exit
     */
    public CallSiteEdgeLabel(Integer siteID, SiteType type, Set<Integer> receivers) {
        this.callSiteID = siteID;
        this.type = type;
        this.receivers = receivers;
    }

    /**
     * Indication of whether this is an entry or exit
     */
    private final SiteType type;

    /**
     * Whether this edge is into an entry node or out of an exit node for the procedure
     */
    public enum SiteType {
        /**
         * This is a procedure entry edge
         */
        ENTRY,
        /**
         * This is a procedure exit edge
         */
        EXIT;
    }

    /**
     * Indication of whether this is an entry or exit
     * 
     * @return Indication of whether this is an entry or exit
     */
    public SiteType getType() {
        return type;
    }

    /**
     * Get the unique ID for the procedure call site
     * 
     * @return call site ID
     */
    public Integer getCallSiteID() {
        return callSiteID;
    }

    /**
     * Get the set of receivers for the call-site
     * 
     * @return set of unique IDs for the receivers (could be null)
     */
    public Set<Integer> getReceivers() {
        return receivers;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CallSiteEdgeLabel)) {
            return false;
        }
        CallSiteEdgeLabel other = (CallSiteEdgeLabel) obj;
        return this.callSiteID == other.callSiteID
                && this.getType().equals(other.getType())
                && (this.getReceivers() == null ? other.getReceivers() == null : this.getReceivers()
                                                                                     .equals(other.getReceivers()));
    }

    @Override
    public String toString() {
        return getType().toString() + "_" + callSiteID + (this.getReceivers() == null ? "" : "\n\n" + this.getReceivers());
    }

    @Override
    public int hashCode() {
        return callSiteID.hashCode() * 17 + 19 * getType().hashCode() + 23
                * (this.getReceivers() == null ? 0 : this.getReceivers().hashCode());
    }
}
