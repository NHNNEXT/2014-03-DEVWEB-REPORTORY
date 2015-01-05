package models.tables.joined;

import autumn.database.Column;
import autumn.database.Condition;
import autumn.database.JoinQuery;
import autumn.database.JoinTable;
import models.Assignment;
import models.tables.AssignmentTable;
import models.tables.LectureRegistrationTable;

import java.sql.Timestamp;

/**
 * Created by infinitu on 14. 12. 25..
 */
public class LectureRegistrationAssignmentJoin extends JoinTable<LectureAssignmentJoin,LectureRegistrationTable, Assignment>{

    public Column<Integer>   aid        = left.aid;
    public Column<Integer>   lid        = left.lid;
    public Column<Timestamp> createTime = left.createTime;
    public Column<Timestamp> dueTime    = left.dueTime;
    public Column<String>    title      = left.title;
    public Column<String>    description= left.description;

    public LectureRegistrationAssignmentJoin() throws NoSuchFieldException {
        super(new LectureAssignmentJoin(), new LectureRegistrationTable(), Assignment.class);
    }

    @Override
    public Condition on(LectureAssignmentJoin assignmentTable, LectureRegistrationTable lectureRegistrationTable) {
        return lectureRegistrationTable.lid .isEqualTo(assignmentTable.lid) .and(
                lectureRegistrationTable.accepted.isEqualTo(true));
    }

    private static ThreadLocal<JoinQuery<LectureRegistrationAssignmentJoin>> tQuery;
    public static JoinQuery<LectureRegistrationAssignmentJoin> getQuery() {
        if(tQuery==null)
            tQuery = new ThreadLocal<JoinQuery<LectureRegistrationAssignmentJoin>>(){
                @Override
                protected JoinQuery<LectureRegistrationAssignmentJoin> initialValue() {
                    return new JoinQuery<>(LectureRegistrationAssignmentJoin.class);
                }
            };
        return tQuery.get();
    }

}
