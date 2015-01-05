package models.tables.joined;

import autumn.database.Column;
import autumn.database.Condition;
import autumn.database.JoinQuery;
import autumn.database.LeftJoinTable;
import models.RegistrationAndSubmission;
import models.tables.RecentSubmissionIdTable;

import java.sql.Timestamp;

/**
 * Created by infinitu on 15. 1. 4..
 */
public class RecentSubmissionIDRegistrationJoin extends LeftJoinTable<LectureRegistrationAssignmentJoin, RecentSubmissionIdTable, RegistrationAndSubmission> {


    public Column<Integer>      uid      = left.right.uid;
    public Column<String>       identity = left.right.identity;
    public Column<String>       major    = left.right.major;
    public Column<String>       stu_name = left.right.stu_name;

    public Column<Integer>      sid         = right.maxSid;
    public Column<Integer>      aid         = right.aid;

    public RecentSubmissionIDRegistrationJoin() throws NoSuchFieldException {
        super(new LectureRegistrationAssignmentJoin(), new RecentSubmissionIdTable(), RegistrationAndSubmission.class);
    }


    @Override
    public Condition on(LectureRegistrationAssignmentJoin left, RecentSubmissionIdTable right) {
        return (left.aid) .isEqualTo (right.aid) .and(
                (left.right.uid) .isEqualTo(right.uid)
        );
    }
}
