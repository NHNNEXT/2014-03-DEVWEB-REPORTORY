package professor.lecture;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entity.Lecture;
import entity.Professor;

public class LectureDao {
	public int makeLectureDao(Lecture lecture) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		
		String address= "jdbc:mysql://vm.link-to-rink.com:8080/ggp_1";  
		String id = "next_ggp";
		String pw = "fuckingnaver";
		String sql = "INSERT INTO MVP_Lectures(professorId, lectureSubject, lectureDiscription) VALUES(?,?,?)";
		int updatedRows=0;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(address, id, pw);
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, lecture.getProfessorId());
			pstmt.setString(2, lecture.getLectureSubject());
			pstmt.setString(3, lecture.getLectureDiscription());
			
			updatedRows = pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e.toString());			
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		System.out.println(lecture.toString());
		return updatedRows;
	}
	public ArrayList<Lecture> retrieveLectureDao(int professorId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs;
		ArrayList<Lecture> returnLecture = new ArrayList<Lecture>();
		
		String address= "jdbc:mysql://vm.link-to-rink.com:8080/ggp_1";  
		String id = "next_ggp";
		String pw = "fuckingnaver";
		String sql = "SELECT LectureId, LectureSubject FROM MVP_Lectures WHERE professorId = ?";

		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(address, id, pw);
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, professorId);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				Lecture lecture = new Lecture(rs.getInt("lectureId"), rs.getString("lectureSubject"));
				returnLecture.add(lecture);
			}
			
			
		} catch (Exception e) {
			System.out.println(e.toString());			
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return returnLecture;
		
	}
}
