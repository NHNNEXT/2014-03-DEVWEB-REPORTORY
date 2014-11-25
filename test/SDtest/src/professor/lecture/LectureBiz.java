package professor.lecture;

import java.util.ArrayList;

import entity.Lecture;

public class LectureBiz {
	public int makeLectureBiz(Lecture lecture) {
		LectureDao dao = new LectureDao();
		int updatedRows = dao.makeLectureDao(lecture);
		return updatedRows;
	}
	
	public ArrayList<Lecture> retrieveLectureBiz(int professorId) {
		LectureDao dao = new LectureDao();
		ArrayList<Lecture> retrievedLecturesList = dao.retrieveLectureDao(professorId);
		return retrievedLecturesList;
	}
}
