package models.tables.joined;

import autumn.database.Column;
import autumn.database.Condition;
import autumn.database.JoinQuery;
import autumn.database.JoinTable;
import models.AttachmentRegistration;
import models.tables.AssignmentAttachmentTable;
import models.tables.AssignmentTable;
import models.tables.AttachmentsTable;
import models.tables.LectureRegistrationTable;

/**
 * Created by infinitu on 15. 1. 6..
 */
public class AttachmentRegistrationJoin extends JoinTable<JoinTable<JoinTable<LectureRegistrationTable, AssignmentTable, Object>, AssignmentAttachmentTable, Object>, AttachmentsTable, AttachmentRegistration> {

    private static ThreadLocal<JoinQuery<AttachmentRegistrationJoin>> tQuery;
    public Column<String> hashcode_id = right.hashcode_id;
    public Column<String> directory = right.directory;
    public Column<String> filename = right.filename;
    public Column<String> type = right.type;
    public Column<Integer> owner = right.owner;
    public Column<Integer> listener = left.left.left.uid;
    public Column<Boolean> accepted = left.left.left.accepted;

    public AttachmentRegistrationJoin() throws NoSuchFieldException {
        super(new JoinTable<JoinTable<LectureRegistrationTable, AssignmentTable, Object>, AssignmentAttachmentTable, Object>(
                new JoinTable<LectureRegistrationTable, AssignmentTable, Object>(new LectureRegistrationTable(), new AssignmentTable(), Object.class) {
                    @Override
                    public Condition on(LectureRegistrationTable lectureTable, AssignmentTable assignmentTable) {
                        return lectureTable.lid.isEqualTo(assignmentTable.lid);
                    }
                }
                , new AssignmentAttachmentTable(), Object.class
        ) {
            @Override
            public Condition on(JoinTable<LectureRegistrationTable, AssignmentTable, Object> left, AssignmentAttachmentTable right) {
                return left.right.aid.isEqualTo(right.aid);
            }
        }, new AttachmentsTable(), AttachmentRegistration.class);
    }

    public static JoinQuery<AttachmentRegistrationJoin> getQuery() {
        if (tQuery == null)
            tQuery = new ThreadLocal<JoinQuery<AttachmentRegistrationJoin>>() {
                @Override
                protected JoinQuery<AttachmentRegistrationJoin> initialValue() {
                    return new JoinQuery<>(AttachmentRegistrationJoin.class);
                }
            };
        return tQuery.get();
    }

    @Override
    public Condition on(JoinTable<JoinTable<LectureRegistrationTable, AssignmentTable, Object>, AssignmentAttachmentTable, Object> left, AttachmentsTable right) {
        return (left.right.hashcode).isEqualTo(right.hashcode_id);
    }
}
