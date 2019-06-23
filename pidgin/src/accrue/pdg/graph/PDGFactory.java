package accrue.pdg.graph;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import accrue.pdg.PDGEdge;
import accrue.pdg.PDGEdgeType;
import accrue.pdg.PDGPath;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.node.AbstractPDGNode;
import accrue.pdg.node.PDGNodeFactory;
import accrue.pdg.node.PDGNodeInterface;
import accrue.pdg.util.CallSiteEdgeLabel;
import accrue.pdg.util.DotPrinter;

/**
 * Helper class used to create and manage {@link ProgramDependenceGraph}s
 */
public class PDGFactory {

    /**
     * Number of nodes to simply just create a new graph instead of using a
     * GraphSubgraph representation. 
     * TODO Pulled this number out of thin air. Could be tuned.
     */
    private static final int SUBGRAPH_THRESHHOLD = 10000;

    /**
     * Create a new PDG from a set of edges
     * 
     * @param edges
     *            edges in the new graph
     * @return a graph containing the given edges and nodes on the edges
     */
    public static ProgramDependenceGraph graphFromEdges(Set<PDGEdge> edges) {
        return new GraphOriginal(null, edges);
    }

    /**
     * Create a PDG with no nodes or edges
     * 
     * @return empty PDG
     */
    public static ProgramDependenceGraph emptyGraph() {
        return new GraphOriginal(Collections.<AbstractPDGNode> emptySet(), Collections.<PDGEdge> emptySet());
    }

    /**
     * Create a new PDG with the given nodes and edges
     * 
     * @param nodes
     *            nodes in the new PDG
     * @param edges
     *            edges in the new PDG
     * @return PDG with the given nodes and edges
     */
    public static ProgramDependenceGraph graph(Set<AbstractPDGNode> nodes, Set<PDGEdge> edges) {
        return new GraphOriginal(nodes, edges);
    }

    /**
     * Create a new PDG that is a subgraph of <code>orig</code> but is
     * restricted to the given sets of nodes and edges
     * 
     * @param orig
     *            original graph
     * @param nodes
     *            nodes in the subgraph
     * @param edges
     *            edges in the subgraph
     * @return PDG containing the given nodes and edges
     */
    public static ProgramDependenceGraph graphSubgraph(ProgramDependenceGraph orig, Set<AbstractPDGNode> nodes, Set<PDGEdge> edges) {
        if (nodes.size() < SUBGRAPH_THRESHHOLD) {
            return new GraphOriginal(nodes, edges);
        }
        return new GraphSubgraph(orig, nodes, edges);
    }

    /**
     * Return <code>g</code> minus <code>minus</code>.
     * 
     * @param g
     *            original set
     * @param minus
     *            set to subtract
     * @return PDG containing nodes and edges in <code>g</code> but not in
     *         <code>minus</code>
     */
    public static ProgramDependenceGraph graphMinus(ProgramDependenceGraph g, ProgramDependenceGraph minus) {
        ProgramDependenceGraph pdg = removeNodes(g, minus.vertexSet());
        Set<AbstractPDGNode> nodes = pdg.vertexSet();
        Set<PDGEdge> edges = new LinkedHashSet<PDGEdge>(pdg.edgeSet());
        edges.removeAll(minus.edgeSet());
        return graphSubgraph(g, nodes, edges);

    }

    /**
     * PDG that is the union of the two given graphs
     * 
     * @param p1
     *            one graph
     * @param p2
     *            another graph
     * @return PDG containing all nodes and edges in either p1 or p2
     */
    public static ProgramDependenceGraph union(ProgramDependenceGraph p1, ProgramDependenceGraph p2) {
        Set<AbstractPDGNode> nodes = new LinkedHashSet<AbstractPDGNode>(p1.vertexSet());
        Set<PDGEdge> edges = new LinkedHashSet<PDGEdge>(p1.edgeSet());
        nodes.addAll(p2.vertexSet());
        edges.addAll(p2.edgeSet());
        return graph(nodes, edges);
    }

    /**
     * Remove a set of nodes from the PDG
     * 
     * @param g
     *            original PDG
     * @param nodesToRemove
     *            nodes we want to remove
     * @return a new PDG containing all nodes in the original PDG not in the
     *         given set
     */
    public static ProgramDependenceGraph removeNodes(ProgramDependenceGraph g, Set<AbstractPDGNode> nodesToRemove) {
        Set<AbstractPDGNode> nodes = new LinkedHashSet<AbstractPDGNode>(g.vertexSet());
        Set<PDGEdge> edges = new LinkedHashSet<PDGEdge>(g.edgeSet());
        nodes.removeAll(nodesToRemove);
        if (edges.size() < nodesToRemove.size()) {
             // Fast if there are less edges than nodesToRemove
            Iterator<PDGEdge> iter = edges.iterator();
            while (iter.hasNext()) {
                PDGEdge e = iter.next();
                if (nodesToRemove.contains(e.getSource()) || nodesToRemove.contains(e.getTarget())) {
                    iter.remove();
                }
            }
        } else {
            // Fast if there are less nodesToRemove than total edges
            for (AbstractPDGNode n : nodesToRemove) {
                edges.removeAll(g.incomingEdgesOf(n));
                edges.removeAll(g.outgoingEdgesOf(n));
            }
        }
        return graphSubgraph(g, nodes, edges);
    }

    /**
     * Create a PDG containing only the nodes in the given set and the input PDG
     * 
     * @param pdg
     *            original PDG
     * @param retainNodes
     *            nodes to retain from the input PDG
     * @return new PDG containing all nodes in the original PDG that are also in
     *         the given set
     */
    public static ProgramDependenceGraph retainNodes(ProgramDependenceGraph pdg, Set<AbstractPDGNode> retainNodes) {
        Set<AbstractPDGNode> nodes = new LinkedHashSet<AbstractPDGNode>(retainNodes);
        Set<PDGEdge> edges = new LinkedHashSet<PDGEdge>();
        for (AbstractPDGNode n : retainNodes) {
            // Note that we only need incoming edges since
            // we only add edges where both nodes are in the set
            Set<PDGEdge> es = pdg.incomingEdgesOf(n);
            for (PDGEdge e : es) {
                if (retainNodes.contains(e.getSource())) {
                    edges.add(e);
                }
            }
        }

        return graphSubgraph(pdg, nodes, edges);
    }

    /**
     * Create a constraint graph from a file containing a JSON serialized
     * representation
     * 
     * @param filename
     *            name of the file containing the JSON
     * @param removeUnecessaryMerges
     *            of true then merge edges will be replaced with copy edges
     *            wherever possible
     * @return new {@link ProgramDependenceGraph} deserialized from the given
     *         JSON file
     */
    public static ProgramDependenceGraph graphFromJSONFile(String filename, boolean removeUnecessaryMerges) {
	BufferedReader br = null;
	try {
	    if (filename.endsWith("gz")) {
		br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(filename))));
	    } else {
		br = new BufferedReader(new FileReader(filename));
	    }
	} catch (RuntimeException | IOException e) {
	    e.printStackTrace();
	    throw new RuntimeException(e);
	}
    
        long in = System.currentTimeMillis();
        System.err.println("Loading File " + filename + " and constructing...");

        ProgramDependenceGraph graph;
        try {
            graph = graphFromJSONTokener(new JSONTokener(br));
        } catch (JSONException e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1);
        }

        long out = System.currentTimeMillis();
        System.err.println("Finished Loading " + filename + " it took " + String.valueOf(out - in));

        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        if (removeUnecessaryMerges) {
            in = System.currentTimeMillis();
            System.err.println("Replacing merges");
            graph = GraphUnnecessaryMerges.construct(graph);
            out = System.currentTimeMillis();
            System.err.println("Finished Replacing merges, it took " + String.valueOf(out - in));
        }
        return graph;
    }

    /**
     * Create a new PDG from the given {@link JSONTokener}
     * 
     * @param x
     *            tokener
     * @return new PDG from the tokener
     * @throws JSONException
     *             json trouble such as missing element
     */
    private static ProgramDependenceGraph graphFromJSONTokener(JSONTokener x) throws JSONException {
        Set<AbstractPDGNode> nodes = new LinkedHashSet<AbstractPDGNode>();
        Set<PDGEdge> edges = new LinkedHashSet<PDGEdge>();

        if (x.nextClean() != '{') {
            throw x.syntaxError("A JSONObject text must begin with '{'");
        }
        // read the nodes array
        List<String> keys = new LinkedList<String>(Arrays.asList("nodes", "edges"));

        while (!keys.isEmpty()) {
            String nextKey = x.nextValue().toString();
            if (keys.remove(nextKey)) {
                if (x.nextClean() != ':') {
                    throw x.syntaxError(":");
                }
                boolean isNodes = "nodes".equals(nextKey);
                readJSONArray(x, isNodes, isNodes ? nodes : edges);
            }
            else {
                throw x.syntaxError("Expected one of " + keys);
            }
            if (!keys.isEmpty()) {
                // now read in the comma
                x.nextClean();
            }
        }

        // read the closing brace
        if (x.nextClean() != '}') {
            throw x.syntaxError("A JSONObject text must end with '}'");
        }
        return graph(nodes, edges);
    }

    /**
     * Read a JSON array of nodes or edges from the given tokener
     * 
     * @param x
     *            tokener
     * @param isNodes
     *            is the array a set of nodes, otherwise it is an array edges
     * @param s
     *            set to add the parsed elements to
     * @throws JSONException
     *             JSON trouble like a missing field
     */
    @SuppressWarnings("unchecked")
    private static void readJSONArray(JSONTokener x, boolean isNodes, @SuppressWarnings("rawtypes") Set s) throws JSONException {
        if (x.nextClean() != '[') {
            throw x.syntaxError("A JSONArray text must start with '['");
        }
        if (x.nextClean() != ']') {
            x.back();
            for (;;) {
                if (x.nextClean() == ',') {
                    x.back();
                    s.add(null);
                } else {
                    x.back();
                    if (isNodes) {
                        s.add(PDGNodeFactory.create((JSONObject) x.nextValue()));
                    }
                    else {
                        s.add(new Edge((JSONObject) x.nextValue()));
                    }
                }
                switch (x.nextClean()) {
                case ';':
                case ',':
                    if (x.nextClean() == ']') {
                        return;
                    }
                    x.back();
                    break;
                case ']':
                    return;
                default:
                    throw x.syntaxError("Expected a ',' or ']'");
                }
            }
        }

    }

    /**
     * Create a new PDG from a set of paths in the given PDG
     * 
     * @param pdg
     *            PDG the paths are in
     * @param paths
     *            set of paths
     * @return new PDG containing any nodes or edges in one of the given paths
     */
    public static ProgramDependenceGraph fromPaths(ProgramDependenceGraph pdg, Set<PDGPath> paths) {
        Set<AbstractPDGNode> nodes = new LinkedHashSet<AbstractPDGNode>();
        Set<PDGEdge> edges = new LinkedHashSet<PDGEdge>();
        for (PDGPath p : paths) {
            edges.addAll(p.getEdgeList());
            nodes.addAll(p.getNodeList());
        }
        return graph(nodes, edges);
    }

    /**
     * Create a path from the given PDG and list of edges
     * 
     * @param pdg
     *            PDG the path is in
     * @param edges
     *            edges in the path
     * @return new Path in the given PDG containing the given edges
     */
    public static PDGPath path(ProgramDependenceGraph pdg, List<PDGEdge> edges) {
        return new Path(pdg, edges);
    }

    /**
     * Create a new PDG edge
     * 
     * @param s
     *            edge source
     * @param t
     *            edge target
     * @param type
     *            edge type
     * @param label
     *            entry or exit edge label
     * @return new edge
     */
    public static PDGEdge edge(AbstractPDGNode s, AbstractPDGNode t, PDGEdgeType type, CallSiteEdgeLabel label) {
        return new Edge(s, t, type, label);
    }

    /**
     * Create a new PDG with the given edge removed
     * 
     * @param pdg
     *            PDG to remove the edge from
     * @param e
     *            edge to remove
     * @return new PDG identical to the input except missing the given edge
     */
    public static ProgramDependenceGraph removeEdge(ProgramDependenceGraph pdg, PDGEdge e) {
        Set<PDGEdge> edges = new LinkedHashSet<PDGEdge>(pdg.edgeSet());
        edges.remove(e);
        return graph(pdg.vertexSet(), edges);
    }

    /**
     * Create a new PDG with the given edges removed
     * 
     * @param pdg
     *            PDG to remove the edge from
     * @param es
     *            edges to remove
     * @return new PDG identical to the input except missing the given edges
     */
    public static ProgramDependenceGraph removeEdges(ProgramDependenceGraph pdg, Set<PDGEdge> es) {
        Set<PDGEdge> edges = new LinkedHashSet<PDGEdge>(pdg.edgeSet());
        edges.removeAll(es);
        return graph(pdg.vertexSet(), edges);
    }

    /**
     * Retain only the given edges in the given graph
     * 
     * @param pdg
     *            graph to get a subgraph of
     * @param edgesToRetain
     *            edges to retain in the graph
     * @return New graph with only edges in edgesToRetain
     */
    public static ProgramDependenceGraph retainEdges(ProgramDependenceGraph pdg, Set<PDGEdge> edgesToRetain) {
        Set<PDGEdge> edges = new LinkedHashSet<PDGEdge>(pdg.edgeSet());
        edges.retainAll(edgesToRetain);

        Set<AbstractPDGNode> nodes = new LinkedHashSet<AbstractPDGNode>();
        for (PDGEdge e : edges) {
            nodes.add(e.getSource());
            nodes.add(e.getTarget());
        }
        return graph(nodes, edges);
    }
    
    /**
     * Graphviz dot format for the PDG. If nodes are different in the graph they
     * will be different in the dot string
     * 
     * @param pdg
     *            PDG to get the dot string for
     * @param cluster
     *            if true then nodes will be clustered into analysisUnits, this
     *            is pretty flakey using graphviz
     * @param spread
     *            Separation between nodes in inches
     * @param highlightedEdges
     *            collection of edges to highlight in the graph (colors the line
     *            red)
     * @param highlightedNodes
     *            Collection of nodes to highlight in the graph (outlines them
     *            in red)
     * 
     * @return dot output with unique node names (for unique nodes)
     */
    @SuppressWarnings("resource")
    public static String uniquifiedDot(ProgramDependenceGraph pdg, boolean cluster,
            double spread,
            Collection<PDGEdge> highlightedEdges,
            Collection<PDGNodeInterface> highlightedNodes) {
        Writer writer = new StringWriter();
        try {
            writer = DotPrinter.uniquifiedDot(pdg.vertexSet(), pdg.edgeSet(), cluster, spread, highlightedEdges, highlightedNodes, writer);
            String ret = writer.toString();
            return ret;
        } catch (IOException e) {
            throw new RuntimeException("IOException writing dot string for PDG " + e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException writing dot string for PDG " + e);
                }
            }
        }
    }

    /**
     * Write the graph to a dot file
     * 
     * @param pdg
     *            PDG to get the dot string for
     * @param filename
     *            name for the newly created file
     * @param directoryName
     *            name for the directory to create the file in
     * @param addDate
     *            if true a date will be appended to the file name (good for
     *            precise versioning)
     * @param cluster
     *            if true then the graph will contain subgraphs for each
     *            procedure, one for the heap, and other subgraphs for each
     * @param spread
     *            Separation between nodes in inches different
     * @param highlightedEdges
     *            collection of edges to highlight in the graph (colors the line
     *            red)
     * @param highlightedNodes
     *            Collection of nodes to highlight in the graph (outlines them
     *            in red)
     */
    public static void writeDotToFile(ProgramDependenceGraph pdg, String filename, String directoryName, boolean addDate, boolean cluster, double spread,
            Collection<PDGEdge> highlightedEdges, Collection<PDGNodeInterface> highlightedNodes) {
        DotPrinter.writeDotToFile(filename, directoryName, addDate, pdg.vertexSet(),
                pdg.edgeSet(), cluster, spread,
                highlightedEdges, highlightedNodes);
    }
}
