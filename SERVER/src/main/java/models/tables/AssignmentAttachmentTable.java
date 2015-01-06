package models.tables;

import autumn.annotation.Model;
import autumn.database.Column;
import autumn.database.Table;
import autumn.database.TableQuery;
import models.AssignmentAttachment;

/**
 * Created by infinitu on 14. 12. 25..
 */

@Model("Assignment_Attachments")
public class AssignmentAttachmentTable extends Table<AssignmentAttachment> {

    private static ThreadLocal<TableQuery<AssignmentAttachmentTable>> tQuery;
    public Column<Integer> aid = intColumn("aid");
    public Column<String> hashcode = stringColumn("hashcode_id");
    public Column<Integer> owner = intColumn("owner");

    public AssignmentAttachmentTable() throws NoSuchFieldException {
        super(AssignmentAttachment.class);
    }

    public static TableQuery<AssignmentAttachmentTable> getQuery() {
        if (tQuery == null)
            tQuery = new ThreadLocal<TableQuery<AssignmentAttachmentTable>>() {
                @Override
                protected TableQuery<AssignmentAttachmentTable> initialValue() {
                    return new TableQuery<>(AssignmentAttachmentTable.class);
                }
            };

        return tQuery.get();
    }

}
