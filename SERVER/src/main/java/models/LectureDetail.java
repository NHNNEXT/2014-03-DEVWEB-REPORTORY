package models;

import util.JsonDataSerializable;

import java.sql.Date;

/**
 * Created by infinitu on 14. 12. 25..
 */
public class LectureDetail extends JsonDataSerializable {

    public int lid;
    public String lecturename;
    public Date startDate;
    public Date finishDate;

    public int prof_uid;
    public String prof_name;
    public String email;
    public String major;

    public Lecture toLecture() {
        Lecture lecture = new Lecture();
        lecture.lid = lid;
        lecture.prof = prof_uid;
        lecture.name = lecturename;
        lecture.startDate = startDate;
        lecture.finishDate = finishDate;
        return lecture;
    }

}
