package conn;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.*;

//import dao.AccessBean;


public class FirstFilter implements Filter {

	public void init(FilterConfig cong) {
		// do nothing
	}

	public void doFilter(ServletRequest srequest, ServletResponse sresponse,
			FilterChain chain) {

		try {
			HttpServletRequest requst = (HttpServletRequest) srequest;
			HttpServletResponse response = (HttpServletResponse) sresponse;
			HttpSession session = requst.getSession();
		
			String admin = (String) session.getAttribute("name");

			if (admin == null) {

				response.sendRedirect("../index.html");

			} else {
				chain.doFilter(srequest, sresponse);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void destroy() {
		// do nothing
	}

}
