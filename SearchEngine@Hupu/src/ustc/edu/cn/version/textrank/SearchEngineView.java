package ustc.edu.cn.version.textrank;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchEngineView extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		String input = request.getParameter("user_input");
		try{
			Page[] list = null;
			list = SearchEngine.test(input);
			List<Page> pages = Arrays.asList(list);
			if(pages != null){
				request.setAttribute("list", pages);
				//System.out.println("list:" + pages);
				request.getRequestDispatcher("/show.jsp").forward(request, response);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
