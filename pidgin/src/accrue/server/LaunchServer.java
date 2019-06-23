package accrue.server;


public class LaunchServer {

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        String graphFile = args[0];
        NewGraphQueryServlet.setGRAPH_FILE(graphFile);
        NewVisualizationServer.main(null);
    }
}
