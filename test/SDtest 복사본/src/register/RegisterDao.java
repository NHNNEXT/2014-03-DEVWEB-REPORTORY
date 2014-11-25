package register;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
//import java.sql.ResultSet;
import java.sql.SQLException;

import entity.Professor;

public class RegisterDao {
	public int registerDao(Professor prof) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//ResultSet rs;
		
		String address= "jdbc:mysql://vm.link-to-rink.com:8080/ggp_1";  
		String id = "next_ggp";
		String pw = "fuckingnaver";
		String sql = "INSERT INTO MVP_Professors(professorEmail, professorPassword, professorName) VALUES(?,?,?)";
		int updatedRows=0;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(address, id, pw);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, prof.getEmail());
			pstmt.setString(2, prof.getPassword());
			pstmt.setString(3, prof.getName());
			
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
		
		System.out.println(prof.toString());
		return updatedRows;
	}
}
