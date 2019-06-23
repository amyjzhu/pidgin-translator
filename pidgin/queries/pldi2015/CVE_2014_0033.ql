let sources = pdg.returnsOf("org.apache.catalina.connector.Request.getPathParameter") in
let sinks = pdg.returnsOf("org.apache.catalina.connector.Request.getRequestedSessionId") in
let check = pdg.returnsOf("isURLRewritingDisabled") in
pdg.removeGuardedByBool(!check).explicitGraph.between(sources,sinks) is empty