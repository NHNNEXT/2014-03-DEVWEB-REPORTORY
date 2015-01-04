package models.tables.joined;

import autumn.database.Column;
import autumn.database.Condition;
import autumn.database.JoinQuery;
import autumn.database.JoinTable;
import models.Submission;
import models.tables.SubmissionTable;

import java.sql.Timestamp;

/**
 * Created by infinitu on 15. 1. 4..
 */
public class SubmissionRegistrationJoin extends JoinTable<LectureRegistrationAssignmentJoin, SubmissionTable, Submission> {

    public Column<Integer>      sid         = right.sid        ;
    public Column<Integer>      uid         = right.uid        ;
    public Column<Integer>      aid         = right.aid        ;
    public Column<String>       description = right.description;
    public Column<Timestamp>    create_time = right.create_time;

    public SubmissionRegistrationJoin() throws NoSuchFieldException {
        super(new LectureRegistrationAssignmentJoin(), new SubmissionTable(), Submission.class);
    }


    @Override
    public Condition on(LectureRegistrationAssignmentJoin left, SubmissionTable right) {
        return (left.aid) .isEqualTo (right.aid);
    }

    private static ThreadLocal<JoinQuery<SubmissionRegistrationJoin>> tQuery;
    public static JoinQuery<SubmissionRegistrationJoin> getQuery() {
        if (tQuery == null)
            tQuery = new ThreadLocal<JoinQuery<SubmissionRegistrationJoin>>() {
                @Override
                protected JoinQuery<SubmissionRegistrationJoin> initialValue() {
                    return new JoinQuery<>(SubmissionRegistrationJoin.class);
                }
            };
        return tQuery.get();
    }
}
