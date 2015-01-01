package models.tables;

import autumn.annotation.Model;
import autumn.database.Column;
import autumn.database.Table;
import autumn.database.TableQuery;
import models.AssignmnetAttachment;

/**
 * Created by infinitu on 14. 12. 25..
 */

@Model("Assignment_Attachments")
public class AssignmnetAttachmentTable extends Table<AssignmnetAttachment>{

    public Column<Integer>  aid      = intColumn("aid");
    public Column<String >  hashcode = stringColumn("hashcode_id");
    public Column<Integer>  owner    = intColumn("owner");

    public AssignmnetAttachmentTable() throws NoSuchFieldException {
        super(AssignmnetAttachment.class);
    }

    private static ThreadLocal<TableQuery<AssignmnetAttachmentTable>> tQuery;
    public static TableQuery<AssignmnetAttachmentTable> getQuery() {
        if (tQuery == null)
            tQuery = new ThreadLocal<TableQuery<AssignmnetAttachmentTable>>() {
                @Override
                protected TableQuery<AssignmnetAttachmentTable> initialValue() {
                    return new TableQuery<>(AssignmnetAttachmentTable.class);
                }
            };

        return tQuery.get();
    }

}
