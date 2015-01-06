package models.tables;

import autumn.annotation.Model;
import autumn.database.Column;
import autumn.database.Table;
import autumn.database.TableQuery;
import models.SubmissionAttachment;

/**
 * Created by infinitu on 14. 12. 25..
 */
@Model("Submission_Attachments")
public class SubmissionAttachmentsTable extends Table<SubmissionAttachment> {

    private static ThreadLocal<TableQuery<SubmissionAttachmentsTable>> tQuery;
    public Column<Integer> sid = intColumn("sid");
    public Column<Integer> aid = intColumn("aid");
    public Column<String> hashcode_id = stringColumn("hashcode_id");
    public Column<Integer> owner = intColumn("owner");

    public SubmissionAttachmentsTable() throws NoSuchFieldException {
        super(SubmissionAttachment.class);
    }

    public static TableQuery<SubmissionAttachmentsTable> getQuery() {
        if (tQuery == null)
            tQuery = new ThreadLocal<TableQuery<SubmissionAttachmentsTable>>() {
                @Override
                protected TableQuery<SubmissionAttachmentsTable> initialValue() {
                    return new TableQuery<>(SubmissionAttachmentsTable.class);
                }
            };

        return tQuery.get();
    }

}
