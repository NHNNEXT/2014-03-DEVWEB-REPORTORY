package models.tables.joined;

import autumn.database.Column;
import autumn.database.Condition;
import autumn.database.JoinQuery;
import autumn.database.JoinTable;
import models.RegistrationAndSubmission;
import models.tables.SubmissionTable;

import java.sql.Timestamp;

/**
 * Created by infinitu on 15. 1. 4..
 */
public class RecentSubmissionRegistrationJoin extends JoinTable<RecentSubmissionIDRegistrationJoin, SubmissionTable, RegistrationAndSubmission> {


    public Column<Integer>      uid      = left.left.right.uid;
    public Column<String>       identity = left.left.right.identity;
    public Column<String>       major    = left.left.right.major;
    public Column<String>       stu_name = left.left.right.stu_name;

    public Column<Integer>      sid         = right.sid;
    public Column<Integer>      aid         = right.aid;
    public Column<String>       description = right.description;
    public Column<Timestamp>    create_date = right.create_date;

    public RecentSubmissionRegistrationJoin() throws NoSuchFieldException {
        super(new RecentSubmissionIDRegistrationJoin(), new SubmissionTable(), RegistrationAndSubmission.class);
    }


    @Override
    public Condition on(RecentSubmissionIDRegistrationJoin left, SubmissionTable right) {
        return (left.sid) .isEqualTo (right.sid);
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
