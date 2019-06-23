package accrue.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StaticAssetsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String STATIC_PAGE_RESOURCE = "accrue/server/index.html";
	private static final boolean ALWAYS_RELOAD = true; //for debug, dev
	private String indexContents;
	
	@Override
	public void init() {
		InputStream str = getClass().getClassLoader().getResourceAsStream(STATIC_PAGE_RESOURCE);
		Scanner scan = new Scanner(str,"UTF-8");
        indexContents = scan.useDelimiter("\\A").next();
        scan.close();
	}
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (ALWAYS_RELOAD) {
			init();
		}
		
    	response.getWriter().print(indexContents);
	}

}
