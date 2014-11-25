package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entity.Professor;

public class LoginDao {
	public Professor loginDao(Professor prof) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Professor returnProf = null;
		
		String address= "jdbc:mysql://vm.link-to-rink.com:8080/ggp_1"; 
		String id = "next_ggp";
		String pw = "fuckingnaver";
		String sql = "SELECT professorEmail, professorName, professorPassword FROM MVP_Professors WHERE professorEmail=? AND professorPassword=?";
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(address, id, pw);
			System.out.println("conn");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, prof.getEmail());
			pstmt.setString(2, prof.getPassword());
			System.out.println("pstmt"+pstmt);
			rs = pstmt.executeQuery();
			while(rs.next()){
				returnProf = new Professor(rs.getString("professorEmail"), rs.getString("professorPassword"), rs.getString("professorName"));
			}
			System.out.println("loginDao" + returnProf.toString());
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
		
		//System.out.println(returnUsr.toString());
		return returnProf;
	}
}
