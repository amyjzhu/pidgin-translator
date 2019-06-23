let sources = pdg.returnsOf("getServerPort") OR pdg.returnsOf("getServerName") in
// Fields in the message header
let sinks = pdg.fieldsNamed("CharChunk.buff") OR pdg.fieldsNamed("ByteChunk.buff") OR pdg.fieldsNamed("LOC org.apache.tomcat.util.buf.MessageBytes") in
pdg.cfl(sources,sinks) is empty
