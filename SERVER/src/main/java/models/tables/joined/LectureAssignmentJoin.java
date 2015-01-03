package models.tables.joined;

import autumn.database.Column;
import autumn.database.Condition;
import autumn.database.JoinQuery;
import autumn.database.JoinTable;
import models.Assignment;
import models.tables.AssignmentTable;
import models.tables.LectureTable;

import java.sql.Timestamp;

/**
 * Created by infinitu on 14. 12. 25..
 */
public class LectureAssignmentJoin extends JoinTable<LectureTable,AssignmentTable, Assignment>{

    public Column<Integer>   aid        = right.aid;
    public Column<Integer>   lid        = right.lid;
    public Column<Timestamp> createTime = right.createTime;
    public Column<Timestamp> dueTime    = right.dueTime;
    public Column<String>    title      = right.title;
    public Column<String>    description= right.description;

    public LectureAssignmentJoin() throws NoSuchFieldException {
        super(new LectureTable(), new AssignmentTable(), Assignment.class);
    }

    @Override
    public Condition on(LectureTable lectureRegistrationTable, AssignmentTable assignmentTable) {
        return lectureRegistrationTable.lid .isEqualTo(assignmentTable.lid);
    }

    private static ThreadLocal<JoinQuery<LectureAssignmentJoin>> tQuery;
    public static JoinQuery<LectureAssignmentJoin> getQuery() {
        if(tQuery==null)
            tQuery = new ThreadLocal<JoinQuery<LectureAssignmentJoin>>(){
                @Override
                protected JoinQuery<LectureAssignmentJoin> initialValue() {
                    return new JoinQuery<>(LectureAssignmentJoin.class);
                }
            };
        return tQuery.get();
    }

}
