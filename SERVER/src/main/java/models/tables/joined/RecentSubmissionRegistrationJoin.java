package models.tables.joined;

import autumn.database.*;
import models.Submission;

import java.sql.Timestamp;

/**
 * Created by infinitu on 15. 1. 4..
 */
public class RecentSubmissionRegistrationJoin extends LeftJoinTable<LectureRegistrationAssignmentJoin, RecentSubmissionJoin, Submission> {


    public Column<Integer>      uid      = left.left.uid;
    public Column<String>       identity = left.left.identity;
    public Column<String>       major    = left.left.major;
    public Column<String>       stu_name = left.left.stu_name;

    public Column<Integer>      sid         = right.sid;
    public Column<Integer>      aid         = right.aid;
    public Column<String>       description = right.description;
    public Column<Timestamp>    create_date = right.create_time;

    public RecentSubmissionRegistrationJoin() throws NoSuchFieldException {
        super(new LectureRegistrationAssignmentJoin(), new RecentSubmissionJoin(), Submission.class);
    }


    @Override
    public Condition on(LectureRegistrationAssignmentJoin left, RecentSubmissionJoin right) {
        return (left.aid) .isEqualTo (right.aid) .and(
                (left.left.uid) .isEqualTo(right.uid)
        );
    }
    

    private static ThreadLocal<JoinQuery<RecentSubmissionRegistrationJoin>> tQuery;
    public static JoinQuery<RecentSubmissionRegistrationJoin> getQuery() {
        if (tQuery == null)
            tQuery = new ThreadLocal<JoinQuery<RecentSubmissionRegistrationJoin>>() {
                @Override
                protected JoinQuery<RecentSubmissionRegistrationJoin> initialValue() {
                    return new JoinQuery<>(RecentSubmissionRegistrationJoin.class);
                }
            };
        return tQuery.get();
    }
}
