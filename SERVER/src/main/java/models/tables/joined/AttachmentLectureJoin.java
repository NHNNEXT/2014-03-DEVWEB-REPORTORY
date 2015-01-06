package models.tables.joined;

import autumn.database.Column;
import autumn.database.Condition;
import autumn.database.JoinQuery;
import autumn.database.JoinTable;
import models.AttachmentLecture;
import models.tables.AssignmentTable;
import models.tables.AttachmentsTable;
import models.tables.LectureTable;
import models.tables.SubmissionAttachmentsTable;

/**
 * Created by infinitu on 15. 1. 6..
 */
public class AttachmentLectureJoin extends JoinTable<JoinTable<JoinTable<LectureTable, AssignmentTable, Object>, SubmissionAttachmentsTable, Object>, AttachmentsTable, AttachmentLecture> {

    private static ThreadLocal<JoinQuery<AttachmentLectureJoin>> tQuery;
    public Column<String> hashcode_id = right.hashcode_id;
    public Column<String> directory = right.directory;
    public Column<String> filename = right.filename;
    public Column<String> type = right.type;
    public Column<Integer> owner = right.owner;
    public Column<Integer> prof = left.left.left.prof;

    public AttachmentLectureJoin() throws NoSuchFieldException {
        super(new JoinTable<JoinTable<LectureTable, AssignmentTable, Object>, SubmissionAttachmentsTable, Object>(
                new JoinTable<LectureTable, AssignmentTable, Object>(new LectureTable(), new AssignmentTable(), Object.class) {
                    @Override
                    public Condition on(LectureTable lectureTable, AssignmentTable assignmentTable) {
                        return lectureTable.lid.isEqualTo(assignmentTable.lid);
                    }
                }
                , new SubmissionAttachmentsTable(), Object.class
        ) {
            @Override
            public Condition on(JoinTable<LectureTable, AssignmentTable, Object> left, SubmissionAttachmentsTable right) {
                return left.right.aid.isEqualTo(right.aid);
            }
        }, new AttachmentsTable(), AttachmentLecture.class);
    }

    public static JoinQuery<AttachmentLectureJoin> getQuery() {
        if (tQuery == null)
            tQuery = new ThreadLocal<JoinQuery<AttachmentLectureJoin>>() {
                @Override
                protected JoinQuery<AttachmentLectureJoin> initialValue() {
                    return new JoinQuery<>(AttachmentLectureJoin.class);
                }
            };
        return tQuery.get();
    }

    @Override
    public Condition on(JoinTable<JoinTable<LectureTable, AssignmentTable, Object>, SubmissionAttachmentsTable, Object> left, AttachmentsTable right) {
        return left.right.hashcode_id.isEqualTo(right.hashcode_id);
    }
}
