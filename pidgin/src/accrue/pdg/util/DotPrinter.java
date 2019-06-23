package accrue.pdg.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import accrue.pdg.PDGEdgeInterface;
import accrue.pdg.PDGEdgeType;
import accrue.pdg.node.PDGNodeInterface;

/**
 * Print a Program dependence graph to the graphviz .dot format
 */
public class DotPrinter {

    /**
     * Graphviz dot format for the PDG. If nodes are different in the graph they
     * will be different in the dot string
     * 
     * @param nodes
     *            Set of nodes in the graph
     * @param edges
     *            Set of edges in the graph
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
     * @param writer
     *            the dot will be written using this writer
     * @return The {@link Writer}
     * @throws IOException
     *             if an I/O error occurs
     */
    public static Writer uniquifiedDot(
            Set<? extends PDGNodeInterface> nodes,
            Set<? extends PDGEdgeInterface<? extends PDGNodeInterface>> edges,
            boolean cluster,
            double spread,
            Collection<? extends PDGEdgeInterface<? extends PDGNodeInterface>> highlightedEdges,
            Collection<PDGNodeInterface> highlightedNodes, Writer writer)
            throws IOException {

        writer.write("digraph G {\n" + "nodesep=" + spread + ";\n" + "ranksep="
                + spread + ";\n" + "graph [fontsize=10]" + ";\n"
                + "node [fontsize=10]" + ";\n" + "edge [fontsize=10]" + ";\n");
        Map<PDGNodeInterface, String> nodeToDot =
                new LinkedHashMap<PDGNodeInterface, String>();
        Map<String, Integer> dotToCount = new LinkedHashMap<String, Integer>();
        Map<String, Set<PDGNodeInterface>> analysisUnitToNodes =
                new LinkedHashMap<String, Set<PDGNodeInterface>>();
        Set<PDGNodeInterface> heapNodes = new LinkedHashSet<PDGNodeInterface>();

        if (highlightedNodes == null) {
            highlightedNodes = new LinkedHashSet<PDGNodeInterface>();
        }

        for (PDGNodeInterface n : nodes) {
            String nodeString =
                    n.toString()
                     .replace("\"", "")
                     .replace("\\", "\\\\")
                     .replace("\\\\n", "(newline)")
                     .replace("\\\\t", "(tab)");
            Integer count = dotToCount.get(nodeString);
            if (count == null) {
                nodeToDot.put(n, nodeString);
                dotToCount.put(nodeString, 1);
            }
            else if (nodeToDot.get(n) == null) {
                nodeToDot.put(n, nodeString + " (" + count + ")");
                dotToCount.put(nodeString, count + 1);
            }

            String groupName = n.groupingName();
            if (groupName != null) {
                Set<PDGNodeInterface> nodesInContext =
                        analysisUnitToNodes.get(groupName);
                if (nodesInContext == null) {
                    nodesInContext = new LinkedHashSet<PDGNodeInterface>();
                    analysisUnitToNodes.put(groupName, nodesInContext);
                }
                nodesInContext.add(n);
            }
        }
        if (cluster) {
            if (!heapNodes.isEmpty()) {
                writer.write("\tsubgraph \"cluster_" + "HEAP" + "\"{\n");
                writer.write("\tlabel=\"" + "HEAP" + "\";\n");
                for (PDGNodeInterface n : heapNodes) {
                    String nodeLabel = "";
                    if (highlightedNodes.contains(n)) {
                        nodeLabel += "[color=red]";
                    }
                    writer.write("\t\t\"" + nodeToDot.get(n) + "\" "
                            + nodeLabel + "\n");
                }
                writer.write("\t}\n"); // subgraph close
            }

            for (String c : analysisUnitToNodes.keySet()) {
                String label = c.replace("\"", "").replace("\\", "\\\\");
                writer.write("\tsubgraph \"cluster_" + label + "\"{\n");
                writer.write("\tlabel=\"" + label + "\";\n");
                for (PDGNodeInterface n : analysisUnitToNodes.get(c)) {
                    String nodeLabel = "";
                    if (n.getNodeType().isPathCondition()) {
                        // shade in the PC nodes
                        if (highlightedNodes.contains(n)) {
                            nodeLabel += "[color=red, ";
                        }
                        else {
                            nodeLabel += "[";
                        }
                        nodeLabel += "style=filled, fillcolor=gray95]";
                    }
                    else if (highlightedNodes.contains(n)) {
                        nodeLabel += "[color=red]";
                    }
                    writer.write("\t\t\"" + nodeToDot.get(n) + "\" "
                            + nodeLabel + "\n");
                }
                writer.write("\t}\n"); // subgraph close
            }
        }
        else {
            for (PDGNodeInterface n : nodes) {
                String nodeLabel = "";
                if (n.getNodeType().isPathCondition()) {
                    // shade in the PC nodes
                    if (highlightedNodes.contains(n)) {
                        nodeLabel += "[color=red, ";
                    }
                    else {
                        nodeLabel += "[";
                    }
                    nodeLabel += "style=filled, fillcolor=gray95]";
                }
                else if (highlightedNodes.contains(n)) {
                    nodeLabel += "[color=red]";
                }
                writer.write("\t\"" + nodeToDot.get(n) + "\" " + nodeLabel
                        + "\n");
            }
        }

        for (PDGEdgeInterface<? extends PDGNodeInterface> edge : edges) {
            PDGEdgeType type = edge.getType();

            String edgeLabel =
                    "[label=\""
                            + type.shortName()
                            + (edge.getEdgeLabel() != null ? " "
                                    + edge.getEdgeLabel() : "") + "\"";
            if (highlightedEdges != null && highlightedEdges.contains(edge)) {
                edgeLabel += ", color=red";
            }
            edgeLabel += "]";
            // String edgeLabel = "";
            writer.write("\t\"" + nodeToDot.get(edge.getSource()) + "\" -> "
                    + "\"" + nodeToDot.get(edge.getTarget()) + "\" "
                    + edgeLabel + ";\n");
        }

        writer.write("\n};\n");
        return writer;
    }

    /**
     * Write the graph to a dot file
     * 
     * @param filename
     *            name for the newly created file
     * @param directoryName
     *            name for the directory to create the file in
     * @param addDate
     *            if true a date will be appended to the file name (good for
     *            precise versioning)
     * @param nodes
     *            Set of nodes in the graph
     * @param edges
     *            Set of edges in the graph
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
    public static void writeDotToFile(
            String filename,
            String directoryName,
            boolean addDate,
            Set<? extends PDGNodeInterface> nodes,
            Set<? extends PDGEdgeInterface<? extends PDGNodeInterface>> edges,
            boolean cluster,
            double spread,
            Collection<? extends PDGEdgeInterface<? extends PDGNodeInterface>> highlightedEdges,
            Collection<PDGNodeInterface> highlightedNodes) {

        // Create the ouput directory
        new File(directoryName).mkdir();
        String now = "";
        if (addDate) {
            SimpleDateFormat dateFormat =
                    new SimpleDateFormat("-yyyy-MM-dd-HH_mm_ss");
            Date dateNow = new Date();
            now = dateFormat.format(dateNow);
        }

        try {
            String fullFilename = directoryName + "/" + filename + now + ".dot";
            Writer out = new BufferedWriter(new FileWriter(fullFilename));
            out =
                    DotPrinter.uniquifiedDot(nodes,
                                             edges,
                                             cluster,
                                             spread,
                                             highlightedEdges,
                                             highlightedNodes,
                                             out);
            out.close();
            System.err.println("\nDOT written to: " + fullFilename);
        }
        catch (IOException e) {
            System.err.println("Could not write DOT to file, " + filename
                    + ", " + e.getMessage());
        }
    }
}
