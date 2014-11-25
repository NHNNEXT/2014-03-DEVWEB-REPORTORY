package professor.login;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.Professor;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String loginEmail = request.getParameter("loginEmail");
		String loginPassword = request.getParameter("loginPassword");
		
		Professor prof = new Professor(loginEmail, loginPassword);
		
		LoginBiz biz = new LoginBiz();
		Professor returnProf = biz.loginBiz(prof);
		System.out.println(returnProf);
		
		if(returnProf != null){
			HttpSession session = request.getSession();
			session.setAttribute("loginProf", returnProf);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/professor/professorMain.jsp");
			rd.forward(request, response);		
		} else {
			response.sendRedirect("/jsp/error.jsp");
		}
		
	}

}
