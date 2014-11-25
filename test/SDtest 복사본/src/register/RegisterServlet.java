package register;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Professor;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("-> RegisterServlet");
		String email = request.getParameter("registerEmail");
		String password = request.getParameter("registerPassword");
		String name = request.getParameter("registerName");
		
		//System.out.println(id +"  "+ password +"  "+ name);
		Professor prof = new Professor(email, password, name);
		
		RegisterBiz registerBiz = new RegisterBiz();
		int updatedRows = registerBiz.registerBiz(prof);
		
		System.out.println(updatedRows);
		
		if(updatedRows > 0) {
			response.sendRedirect("/jsp/professor/register/registerSuccess.jsp");
		} else {
			response.sendRedirect("/jsp/error.jsp");
		}
		
	}

}
