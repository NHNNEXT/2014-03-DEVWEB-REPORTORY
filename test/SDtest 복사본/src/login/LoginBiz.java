package login;

import entity.Professor;

public class LoginBiz {
	public Professor loginBiz(Professor prof) {
		
		LoginDao dao = new LoginDao();
		Professor returnProf = dao.loginDao(prof);
		return returnProf;
	}
}
