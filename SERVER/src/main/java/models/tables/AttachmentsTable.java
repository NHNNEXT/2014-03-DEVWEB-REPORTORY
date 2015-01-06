package models.tables;

import autumn.annotation.Model;
import autumn.database.Column;
import autumn.database.Table;
import autumn.database.TableQuery;
import models.Attachment;

/**
 * Created by infinitu on 14. 12. 25..
 */
@Model("Attachments")
public class AttachmentsTable extends Table<Attachment> {
    private static ThreadLocal<TableQuery<AttachmentsTable>> tQuery;
    public Column<String> hashcode_id = stringColumn("hashcode_id");
    public Column<String> directory = stringColumn("directory");
    public Column<String> filename = stringColumn("filename");
    public Column<String> type = stringColumn("type");
    public Column<Integer> owner = intColumn("owner");

    public AttachmentsTable() throws NoSuchFieldException {
        super(Attachment.class);
    }

    public static TableQuery<AttachmentsTable> getQuery() {
        if (tQuery == null)
            tQuery = new ThreadLocal<TableQuery<AttachmentsTable>>() {
                @Override
                protected TableQuery<AttachmentsTable> initialValue() {
                    return new TableQuery<>(AttachmentsTable.class);
                }
            };

        return tQuery.get();
    }

}
