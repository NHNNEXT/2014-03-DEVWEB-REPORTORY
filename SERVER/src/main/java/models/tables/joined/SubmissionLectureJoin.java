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
public class SubmissionLectureJoin extends JoinTable<LectureAssignmentJoin, SubmissionTable, Submission> {

    public Column<Integer>      sid         = right.sid        ;
    public Column<Integer>      uid         = right.uid        ;
    public Column<Integer>      aid         = right.aid        ;
    public Column<String>       description = right.description;
    public Column<Timestamp>    create_date = right.create_date;

    public SubmissionLectureJoin() throws NoSuchFieldException {
        super(new LectureAssignmentJoin(), new SubmissionTable(), Submission.class);
    }


    @Override
    public Condition on(LectureAssignmentJoin left, SubmissionTable right) {
        return (left.aid) .isEqualTo (right.aid);
    }

    private static ThreadLocal<JoinQuery<SubmissionLectureJoin>> tQuery;
    public static JoinQuery<SubmissionLectureJoin> getQuery() {
        if (tQuery == null)
            tQuery = new ThreadLocal<JoinQuery<SubmissionLectureJoin>>() {
                @Override
                protected JoinQuery<SubmissionLectureJoin> initialValue() {
                    return new JoinQuery<>(SubmissionLectureJoin.class);
                }
            };
        return tQuery.get();
    }
}
