package models.tables.joined;

import autumn.database.Column;
import autumn.database.Condition;
import autumn.database.JoinQuery;
import autumn.database.JoinTable;
import models.RecentSubmission;
import models.Submission;
import models.tables.RecentSubmissionIdTable;
import models.tables.SubmissionTable;

import java.sql.Timestamp;

/**
 * Created by infinitu on 15. 1. 4..
 */
public class RecentSubmissionJoin extends JoinTable<RecentSubmissionIdTable,SubmissionTable, RecentSubmission> {

    public Column<Integer>      sid         = right.sid        ;
    public Column<Integer>      count       = left.count       ;
    public Column<Integer>      uid         = right.uid        ;
    public Column<Integer>      aid         = right.aid        ;
    public Column<String> description = right.description;
    public Column<Timestamp>    create_time = right.create_time;
    
    public RecentSubmissionJoin() throws NoSuchFieldException {
        super(new RecentSubmissionIdTable(), new SubmissionTable(), RecentSubmission.class);
    }

    @Override
    public Condition on(RecentSubmissionIdTable recentSubmissionIdTable, SubmissionTable submissionTable) {
        return (recentSubmissionIdTable.maxSid) .isEqualTo (submissionTable.sid);
    }

    private static ThreadLocal<JoinQuery<RecentSubmissionJoin>> tQuery;
    public static JoinQuery<RecentSubmissionJoin> getQuery() {
        if (tQuery == null)
            tQuery = new ThreadLocal<JoinQuery<RecentSubmissionJoin>>() {
                @Override
                protected JoinQuery<RecentSubmissionJoin> initialValue() {
                    return new JoinQuery<>(RecentSubmissionJoin.class);
                }
            };
        return tQuery.get();
    }
}
