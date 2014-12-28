package models.tables.joined;

import autumn.database.Column;
import autumn.database.Condition;
import autumn.database.JoinQuery;
import autumn.database.JoinTable;
import models.LectureAndProfessor;
import models.tables.LectureTable;

import java.sql.Date;

/**
 * Created by infinitu on 14. 12. 25..
 */
public class LectureProfessorJoin extends JoinTable<ProfessorUserJoin,LectureTable,LectureAndProfessor>{

    public Column<Integer>  lid  = right.lid;
    public Column<Integer>  prof_uid = left.uid;
    public Column<String>   lecturename = right.name;
    public Column<Date>     startDate = right.startDate;
    public Column<Date>     finishDate = right.finishDate;

    public Column<String>   prof_name   = left.name;
    public Column<String>   email       = left.email;
    public Column<String>   major       = left.major;



    public LectureProfessorJoin() throws NoSuchFieldException {
        super(new ProfessorUserJoin(), new LectureTable(), LectureAndProfessor.class);
    }

    @Override
    public Condition on(ProfessorUserJoin professorUserJoin, LectureTable lectureTable) {
        return (lectureTable.prof) .isEqualTo (professorUserJoin.uid);
    }

    private static ThreadLocal<JoinQuery<LectureProfessorJoin>> tQuery;
    public static JoinQuery<LectureProfessorJoin> getQuery() {
        if(tQuery==null)
            tQuery = new ThreadLocal<JoinQuery<LectureProfessorJoin>>(){
                @Override
                protected JoinQuery<LectureProfessorJoin> initialValue() {
                    return new JoinQuery<>(LectureProfessorJoin.class);
                }
            };
        return tQuery.get();
    }

}
