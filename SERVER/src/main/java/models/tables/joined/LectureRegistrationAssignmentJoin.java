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
public class LectureRegistrationAssignmentJoin extends JoinTable<LectureRegistrationTable,LectureAssignmentJoin, Assignment>{

    public Column<Integer>   aid        = right.aid;
    public Column<Integer>   lid        = right.lid;
    public Column<Timestamp> createTime = right.createTime;
    public Column<Timestamp> dueTime    = right.dueTime;
    public Column<String>    title      = right.title;
    public Column<String>    description= right.description;

    public LectureRegistrationAssignmentJoin() throws NoSuchFieldException {
        super(new LectureRegistrationTable(), new LectureAssignmentJoin(), Assignment.class);
    }

    @Override
    public Condition on(LectureRegistrationTable lectureRegistrationTable, LectureAssignmentJoin assignmentTable) {
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
