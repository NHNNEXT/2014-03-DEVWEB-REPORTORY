package entity;

public class Lecture {
	int lectureId;
	int professorId;
	String lectureSubject;
	String lectureDiscription;
	
	public Lecture(int lectureId, int professorId, String lectureSubject, String lectureDiscription) {
		this.lectureId = lectureId;
		this.professorId = professorId;
		this.lectureSubject = lectureSubject;
		this.lectureDiscription = lectureDiscription;
	}
	
	public Lecture(int professorId, String lectureSubject, String lectureDiscription) {
		this.professorId = professorId;
		this.lectureSubject = lectureSubject;
		this.lectureDiscription = lectureDiscription;
	}

	public Lecture(int lectureId, String lectureSubject) {
		this.lectureId = lectureId;
		this.lectureSubject = lectureSubject;

	}
	
	public int getLectureId() {
		return lectureId;
	}

	public int getProfessorId() {
		return professorId;
	}

	public String getLectureSubject() {
		return lectureSubject;
	}

	public String getLectureDiscription() {
		return lectureDiscription;
	}

	@Override
	public String toString() {
		return "Lecture [lectureId=" + lectureId + ", professorId="
				+ professorId + ", lectureSubject=" + lectureSubject
				+ ", lectureDiscription=" + lectureDiscription + "]";
	}
	
	
}
