package professor.lecture;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.Lecture;
import entity.Professor;

/**
 * Servlet implementation class retrieveLecture
 */
@WebServlet("/RetrieveLectureServlet")
public class RetrieveLectureServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("-> RetrieveLectureServlet");
		HttpSession session = request.getSession();	
		//System.out.println(session);
		Professor logedProf = (Professor) session.getAttribute("loginProf");
		
		LectureBiz retrieveLectureBiz = new LectureBiz();
		ArrayList<Lecture> lectureList = retrieveLectureBiz.retrieveLectureBiz(logedProf.getId());
		
		session.setAttribute("lectureList", lectureList);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/professor/viewLectureList/lectureList.jsp");
		rd.forward(request, response);		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
