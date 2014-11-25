package professor.lecture;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.Lecture;
import entity.Professor;

@WebServlet("/MakeLectureServlet")
public class MakeLectureServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();	
		//System.out.println(session);
		Professor logedProf = (Professor) session.getAttribute("loginProf");
		String lectureSubject = request.getParameter("lectureSubject");
		String lectureDiscription = request.getParameter("lectureDiscription");
		
		Lecture lecture = new Lecture(logedProf.getId(), lectureSubject, lectureDiscription);
		
		LectureBiz makeLectureBiz = new LectureBiz();
		int updatedRows = makeLectureBiz.makeLectureBiz(lecture);
		
		System.out.println(updatedRows);
		
		if(updatedRows > 0) {
			System.out.println("lecture is made.");
			
			response.sendRedirect("/RetrieveLectureServlet");
		} else {
			System.out.println("lecture is NOT made.");
			
			//response.sendRedirect("/jsp/error.jsp");
		}
		
	}

}
