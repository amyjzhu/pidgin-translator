package accrue.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import accrue.algorithm.UserInterfaceAlgorithms;
import accrue.parser.PidginParser;
import accrue.pdg.PDGEdge;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.graph.PDGFactory;
import accrue.pdg.node.AbstractPDGNode;
import accrue.query.TopLevel;
import accrue.query.policy.Policy;
import accrue.query.query.Query;
import accrue.query.util.Environment;

public class NewGraphQueryServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
      private static final String FOLDER = "../walaAnalysis/tests/";
//      private static final String FOLDER = "/Users/mu/pdg/tests/";
//      private static final String FOLDER = "/Volumes/HDD/pdg-policies-tests-archive-May-2014/usenix2014/";
      // No prefix. If they are regenerated then this is the file name	
//    private static final String GRAPH_FILE = FOLDER  + "test.programs.conference.ClientTest.json";
//    private static final String GRAPH_FILE = FOLDER  + "nojdk.ClientTest.json";
//    private static final String GRAPH_FILE = FOLDER  + "test.Scratch.json";
//      private static final String GRAPH_FILE = FOLDER + "freecs.Driver.json";	
//    private static final String GRAPH_FILE = FOLDER  + "com._17od.upm.Driver.json";
//    private static final String GRAPH_FILE = FOLDER  + "cms.Driver.json";  
//	  private static final String GRAPH_FILE = FOLDER  + "test.programs.guessinggame.SimpleGame.json"; 
//      private static final String GRAPH_FILE = FOLDER  + "pdg_test.SimpleGame.json"; 
//      private static final String GRAPH_FILE = FOLDER  + "test.programs.tax.TaxFreeMain.json"; 

//      private static final String GRAPH_FILE = FOLDER  + "PLDI/pdg_com._17od.upm.Driver.json.2t1h-x-scs1.allFlagsTrue.gz";
//    private static final String GRAPH_FILE = FOLDER  + "PLDI/pdg_test.programs.tax.TaxFreeMain.json.2t1h-x-scs2.noFlags.gz";

    private static String GRAPH_FILE = FOLDER  + "pdg_com._17od.upm.Driver.json.gz";

//      private static final String GRAPH_FILE = FOLDER  + "pdg_test.cfl.MutuallyRecursive.json.gz"; 
//      private static final String GRAPH_FILE = FOLDER  + "pdg_test.programs.tax.TaxFreeMain.json.gz"; 
//      private static final String GRAPH_FILE = FOLDER  + "pdg_test.Scratch.json.gz"; 
//      private static final String GRAPH_FILE = FOLDER  + "pdg_test.programs.conference.ClientTest.json.gz"; 

//      private static final String GRAPH_FILE = FOLDER  + "pdg_test.programs.tax.TaxFreeMain.Specialized.json.gz"; 
//      private static final String GRAPH_FILE = FOLDER  + "pdg_test.Scratch.json.gz";
//      private static final String GRAPH_FILE = FOLDER  + "pdg_test.programs.tax.TaxFreeMain.nonsingleton.nosig.json.gz"; 
//      private static final String GRAPH_FILE = FOLDER  + "pdg_test.programs.tax.TaxFreeMain.singleton.nosig.json.gz"; 
      
    // Used for Oakland 2014 submission
//  private static final String GRAPH_FILE = "tests/oakland2014/2.1.2cms.Driver.json";
//  private static final String GRAPH_FILE = "tests/oakland2014/2.1.2com._17od.upm.Driver.json";
//  private static final String GRAPH_FILE = "tests/oakland2014/2.1.2freecs.Driver.json";
//  private static final String GRAPH_FILE = "tests/oakland2014/2.1.2test.programs.guessinggame.SimpleGame.json";
//  private static final String GRAPH_FILE = "tests/oakland2014/2.1.2test.programs.tax.TaxFreeMain.json";
    
    private static final String PRELUDE_FILE = "queries/preludeNew.ql";

    private ProgramDependenceGraph resultPDG;
    private ProgramDependenceGraph originalPDG;

    private ResponseType requestedResponse;

    private List<String> filters;

    /**
     * Set the file to be queried
     * 
     * @param fileName name of the file and path to it
     */
    public static void setGRAPH_FILE(String fileName) {
        GRAPH_FILE = fileName;
    }
	
    /**
     * Handle a PidginQL query
     * 
     * @param responseObject
     *            response to query (to be constructed)
     * @param query
     *            query string to parse and evaluate
     * 
     * @throws JSONException
     *             thrown if there is a JSON issue
     */
    public void handleQuery(JSONObject responseObject, String query) throws JSONException {
        TopLevel<?> tl = null;
        resultPDG = originalPDG;
        boolean empty = query.isEmpty();
        try {
            saveQuery(query);
            long in = System.currentTimeMillis();
            tl = PidginParser.parse(getPrelude() + query);
            System.err.println("(parsed prelude and query in " + (System.currentTimeMillis() - in));
        } catch (Exception e) {
            if (empty) {
                // putting this here allows for the prelude to contain a valid
                // query and the query to be empty. If we get here it probably
                // means the prelude ends in "in" and the query was empty
                responseObject.put("error", "Empty query.");
                return;
            }
            responseObject.put("error", e.getMessage());
            return;
        }
        
        try {            
            if (tl instanceof Query) {
                Query q = (Query) tl;
                long in = System.currentTimeMillis();
                resultPDG = q.evaluate(Environment.fresh(originalPDG));
                System.out.println((System.currentTimeMillis() - in));
                responseObject.put("result", GraphToJSON.processToJSON(resultPDG, requestedResponse));
            } else if (tl instanceof Policy) {
                Policy p = (Policy) tl;
                JSONObject o = new JSONObject();
                JSONArray a = new JSONArray();
                long in = System.currentTimeMillis();
                try {                   
                    Boolean result = p.evaluate(Environment.fresh(originalPDG));
                    System.err.println("Policy Holds!");
                    System.out.println((System.currentTimeMillis() - in));
                    if (result) {
                        a.put("Policy Holds!");
                    } else {
                        a.put("Policy FAILED!");
                    }
                } catch (AssertionError e) {
                    System.out.println("FAILED POLICY: " + (System.currentTimeMillis() - in));
                    a.put("Policy assertion FAILED!");
                }
                o.put("type", ResponseType.SUMMARY.toString());
                o.put("results", a);
                responseObject.put("result", o);
            }
        } catch(RuntimeException e) {
            System.err.println("RuntimeException: " + e.getMessage());
            responseObject.put("error", e.getMessage());
            e.printStackTrace();
        }        
    }
	
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	    long in = System.currentTimeMillis();
	    System.err.println("Started handling request " + request.getRequestURI());
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		
		String operation = extractOperation(request);
        long checkA = System.currentTimeMillis();
		
        long checkB;
		if (operation.equals("add_filter")) {
			addFilter(request.getParameter("type"), request);
			response.sendRedirect("/");
			checkB = System.currentTimeMillis();
		} else {
			try {
				JSONObject responseObject = handleOperation(operation, request);
				checkB = System.currentTimeMillis();
				responseObject.write(response.getWriter());
			} catch (JSONException e) {
				throw new ServletException(e);
			}
		}
		long out = System.currentTimeMillis();
        System.err.println("Finished handling request " + request.getRequestURI() + " took " + String.valueOf(out-in));
        System.err.println("     extracting operation took about " + String.valueOf(checkA-in));
        System.err.println("     processing operation took about " + String.valueOf(checkB-checkA));
	}
	
	private void addFilter(String type, HttpServletRequest request) {
		if (type.equals("reset")) {
			init();
		}
	}

	private JSONObject handleOperation(String operation, HttpServletRequest request) throws JSONException {
		JSONObject responseObject = new JSONObject();
		//System.out.println(request);
		if (operation.equals("name")) {
			getName(responseObject);
		} else if (operation.equals("stats")) {
			graphStats(responseObject);
		} else if (operation.equals("collapsed_edges")) {
			graphCollapsed(responseObject);
		} else if (operation.equals("nodes_in")) {
			nodesInGrouping(responseObject, request.getParameter("n"));
		} else if (operation.equals("node_info")) {
			nodeStats(responseObject, request.getParameter("n"));
		} else if (operation.equals("direct_edges")) {
			directEdges(responseObject, request.getParameter("n"));
		} else if (operation.equals("query")) {
			handleQuery(responseObject, request.getParameter("q"));
	    } else if (operation.equals("response-type")) {
	        String t = request.getParameter("value");
	        if (t.equals("DEFAULT")) {
	            requestedResponse = null;
	        } else {
	            requestedResponse = ResponseType.valueOf(t);
	        }
	        responseObject.put("result", GraphToJSON.processToJSON(resultPDG, requestedResponse));
	        
	        System.out.println("Requested response type is now: " + requestedResponse);
		} else {
			responseObject.put("error", "unknown operation: " + operation);
		}
		
		return responseObject;
	}
	
	public static String getPrelude() {
        String prelude = "";
        try {
            FileReader fr = new FileReader(PRELUDE_FILE);
            BufferedReader br = new BufferedReader(fr);
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                prelude += line + "\n";
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to load prelude! " + e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load prelude! " + e);
        }
        return prelude;
    }
	
    /**
     * Check for a File directive at the start of the query. If found, save the
     * query at the designated path.
     * 
     * Or it will be saved to queries/temp.ql
     *
     * A File directive must be the first line in the query with the syntax:
     * "// File: <path>". <path> may be followed with a "//" comment.
     *
     * Example query with a File directive:
     *   // File: queries/simple.ql /////////////
     *   // A very simple query
     *   ////////////////////////////////////////
     *   allNodes
     *
     * @param query
     *            Query in which to check and handle File directive
     */
    private void saveQuery(String query) throws IOException {
        if (query.startsWith("// File: ")) {
            String filename = query.substring(9).split("//|(\\r?\\n)", 2)[0].trim();
            FileWriter fw = new FileWriter(new File(filename));
            System.err.println("Saving query to " + filename);
            fw.write(query);
            fw.close();
        } else {
            String filename = "queries/temp.ql";
            FileWriter fw = new FileWriter(new File(filename));
            System.err.println("Saving query to " + filename);
            fw.write(query);
            fw.close();
        }
    }

	private void directEdges(JSONObject m, String nodeId) throws JSONException {
		AbstractPDGNode node = resultPDG.getNodeById(Integer.parseInt(nodeId));
		JSONArray edges = new JSONArray();
		JSONArray nodes = new JSONArray();
		Set<AbstractPDGNode> seenNodes = new HashSet<AbstractPDGNode>();
		Set<PDGEdge> edgesToProcess = new HashSet<PDGEdge>();
        for (PDGEdge e : resultPDG.incomingEdgesOf(node)) {
            edgesToProcess.add(e);
            edgesToProcess.addAll(resultPDG.incomingEdgesOf(e.getSource()));
            edgesToProcess.addAll(resultPDG.outgoingEdgesOf(e.getSource()));
        }
        for (PDGEdge e : resultPDG.outgoingEdgesOf(node)) {
            edgesToProcess.add(e);
            edgesToProcess.addAll(resultPDG.incomingEdgesOf(e.getTarget()));
            edgesToProcess.addAll(resultPDG.outgoingEdgesOf(e.getTarget()));
        }
		for (PDGEdge e : edgesToProcess) {
			processNode(e.getSource(), nodes, seenNodes);
			processNode(e.getTarget(), nodes, seenNodes);
			JSONObject edge = new JSONObject();
			edge.put("source", "" + e.getSource().getNodeId());
			edge.put("target", "" + e.getTarget().getNodeId());
			edge.put("type", e.getType().toString());
			edges.put(edge);
		}
		m.put("edges", edges);
		m.put("nodes", nodes);
	}
	
	private void processNode(AbstractPDGNode node,JSONArray nodes, Set<AbstractPDGNode> seenNodes) throws JSONException {
		if (!seenNodes.contains(node)) {
			seenNodes.add(node);
			JSONObject jsonNode = new JSONObject();
			jsonNode.put("name", node.getName());
			jsonNode.put("grouping", node.groupingName());
			jsonNode.put("nodeid", "" + node.getNodeId());
			jsonNode.put("id", "" + node.getNodeId());
			jsonNode.put("is_inside_function", true);
			nodes.put(jsonNode);
		}
	}

	public void getName(JSONObject m) throws JSONException {
		m.put("name", GRAPH_FILE);
	}
	
	public void graphStats(JSONObject m) throws JSONException {
		m.put("numEdges", resultPDG.edgeSet().size());
		m.put("numNodes", resultPDG.vertexSet().size());
		m.put("filters", filters);
		m.put("name", GRAPH_FILE);
	}
	
	public void graphCollapsed(JSONObject m) throws JSONException {
		m.put("graph", UserInterfaceAlgorithms.computeCollapsedEdges(resultPDG));
	}
	
	public void nodesInGrouping(JSONObject m, String f) throws JSONException {
		m.put("graph", UserInterfaceAlgorithms.getEdgesInGrouping(resultPDG, f));
	}
	
	public void nodeStats(JSONObject m, String nodeId) throws JSONException {
		AbstractPDGNode node = resultPDG.getNodeById(Integer.parseInt(nodeId));
		m.put("name", node.getName());
		if (node.getPosition() != null) {
		    m.put("position", node.getPosition().toString());      
		}
	}
	
	private String extractOperation(HttpServletRequest request) {
		String operation = request.getPathInfo();
		if (operation == null || operation.length() == 1) {
			operation = "";
		} else {
			operation = operation.substring(1);
		}
		return operation;
	}

	@Override
	public void init() {
		loadPDG();
		resultPDG = originalPDG;
		filters = new ArrayList<String>();
	}
	
    private void loadPDG() {
        try {
            originalPDG = PDGFactory.graphFromJSONFile(GRAPH_FILE, true);
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    public void loadPDG(String filename) {
        try {
            originalPDG = PDGFactory.graphFromJSONFile(filename, true);
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
