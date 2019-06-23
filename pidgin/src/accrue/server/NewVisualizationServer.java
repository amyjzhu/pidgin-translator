package accrue.server;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletHandler;

/**
 * Server for Querying PDGs
 */
public class NewVisualizationServer {
	
    /**
     * Port number
     */
	private static final int PORT = 8080;
	
    /**
     * Start the server at port given by the static field
     * 
     * @param args
     *            Unused
     * @throws Exception
     *             If something goes wrong
     */
    public static void main(String[] args) throws Exception {
    	Server server = new Server();
    	// Need to create a connector and increase the header size
        SelectChannelConnector connector0 = new SelectChannelConnector();
        connector0.setPort(PORT);
        connector0.setRequestHeaderSize(1048576);
        server.setConnectors(new Connector[] {connector0});
    	
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        
        handler.addServletWithMapping(NewGraphQueryServlet.class, "/q/*");
        handler.addServletWithMapping(StaticAssetsServlet.class, "/*");
        
        server.start();
        server.join();
    }
}
