package ustc.edu.cn.version.textrank;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchEngineServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String input = request.getParameter("user_input");

		try {
			Page[] list = null;
			list = SearchEngine.test(input);

			if (list != null) {
				response.setContentType("text/html;charset=utf-8");

				PrintWriter out = response.getWriter();
				out.println("<!DOCTYPE html><html><head><title>result</title></head><body>");
				out.println("<div width=500px>");
				if (list != null) {
					for (int i = 1; i <= list.length; i++) {
						out.println("<h4><a href=" + list[i - 1].url + ">" + list[i - 1].title + "</a>" + "<br/></h4>");
						if (list[i - 1].content.length() > 100) {
							out.println("<p>" + list[i - 1].content.substring(0, 100) + "</p><br/>");
						} else {
							out.println("<p>" + list[i - 1].content + "</p><br/>");
						}
					}
				}
				out.println("</div>");
				out.println("</body></html>");
			} else {
				/*
				 * response.setContentType("text/html"); PrintWriter out =
				 * response.getWriter(); out.println(
				 * "<!DOCTYPE html><html><head><title>Insert title here</title></head><body>"
				 * );
				 * 
				 * out.println("<h1>" + "shibai" + "</h1>");
				 * out.println("</body></html>");
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
