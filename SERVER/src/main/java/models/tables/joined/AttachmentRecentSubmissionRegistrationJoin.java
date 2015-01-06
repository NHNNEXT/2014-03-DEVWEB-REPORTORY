package models.tables.joined;

import autumn.database.Column;
import autumn.database.Condition;
import autumn.database.JoinQuery;
import autumn.database.JoinTable;
import models.RegistrationAndAttachment;
import models.tables.AttachmentsTable;
import models.tables.SubmissionAttachmentsTable;

import java.sql.Timestamp;

/**
 * Created by infinitu on 15. 1. 4..
 */
public class AttachmentRecentSubmissionRegistrationJoin extends JoinTable<JoinTable<RecentSubmissionRegistrationJoin, SubmissionAttachmentsTable, Object>, AttachmentsTable, RegistrationAndAttachment> {


    private static ThreadLocal<JoinQuery<AttachmentRecentSubmissionRegistrationJoin>> tQuery;
    public Column<Integer> uid = left.left.left.left.right.uid;
    public Column<String> identity = left.left.left.left.right.identity;
    public Column<String> major = left.left.left.left.right.major;
    public Column<String> stu_name = left.left.left.left.right.stu_name;
    public Column<Integer> sid = left.left.right.sid;
    public Column<Integer> aid = left.left.right.aid;
    public Column<String> description = left.left.right.description;
    public Column<Timestamp> create_date = left.left.right.create_date;
    public Column<String> directory = right.directory;
    public Column<String> filename = right.filename;

    public AttachmentRecentSubmissionRegistrationJoin() throws NoSuchFieldException {
        super(new JoinTable<RecentSubmissionRegistrationJoin, SubmissionAttachmentsTable, Object>(new RecentSubmissionRegistrationJoin(), new SubmissionAttachmentsTable(), Object.class) {
            @Override
            public Condition on(RecentSubmissionRegistrationJoin left, SubmissionAttachmentsTable right) {
                return (left.aid).isEqualTo(right.aid);
            }
        }, new AttachmentsTable(), RegistrationAndAttachment.class);
    }

    public static JoinQuery<AttachmentRecentSubmissionRegistrationJoin> getQuery() {
        if (tQuery == null)
            tQuery = new ThreadLocal<JoinQuery<AttachmentRecentSubmissionRegistrationJoin>>() {
                @Override
                protected JoinQuery<AttachmentRecentSubmissionRegistrationJoin> initialValue() {
                    return new JoinQuery<>(AttachmentRecentSubmissionRegistrationJoin.class);
                }
            };
        return tQuery.get();
    }

    @Override
    public Condition on(JoinTable<RecentSubmissionRegistrationJoin, SubmissionAttachmentsTable, Object> left, AttachmentsTable right) {
        return (left.right.hashcode_id).isEqualTo(right.hashcode_id);
    }
}
