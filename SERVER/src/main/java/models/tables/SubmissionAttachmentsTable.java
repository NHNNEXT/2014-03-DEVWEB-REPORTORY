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
public class SubmissionAttachmentsTable extends Table<SubmissionAttachment>{

    public Column<Integer>  uid          = intColumn("uid");
    public Column<Integer>  lid          = intColumn("lid");
    public Column<Integer>  aid          = intColumn("aid");
    public Column<String>   hashcode_id  = stringColumn("hashcode_id");

    public SubmissionAttachmentsTable() throws NoSuchFieldException {
        super(SubmissionAttachment.class);
    }

    private static ThreadLocal<TableQuery<SubmissionAttachmentsTable>> tQuery;
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
