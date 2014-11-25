package professor.register;

import entity.Professor;

public class RegisterBiz {
	public int registerBiz(Professor prof) {
		
		RegisterDao dao = new RegisterDao();
		int updatedRows = dao.registerDao(prof);
		return updatedRows;
	}
	
	
}
