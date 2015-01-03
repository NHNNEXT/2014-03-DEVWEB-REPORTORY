package models.tables;

import autumn.annotation.Model;
import autumn.database.Column;
import autumn.database.Table;
import autumn.database.TableQuery;
import models.Submission;

import java.sql.Timestamp;

/**
 * Created by infinitu on 14. 12. 25..
 */
@Model("Submissions")
public class SubmissionTable extends Table<Submission>{

    public Column<Integer>      sid         = intColumn("sid")   ;
    public Column<Integer>      uid         = intColumn("uid")   ;
    public Column<Integer>      aid         = intColumn("aid")   ;
    public Column<String>       description = stringColumn("description");
    public Column<Timestamp>    create_time = timestampColumn("create_date");


    public SubmissionTable() throws NoSuchFieldException {
        super(Submission.class);
    }

    private static ThreadLocal<TableQuery<SubmissionTable>> tQuery;
    public static TableQuery<SubmissionTable> getQuery() {
        if (tQuery == null)
            tQuery = new ThreadLocal<TableQuery<SubmissionTable>>() {
                @Override
                protected TableQuery<SubmissionTable> initialValue() {
                    return new TableQuery<>(SubmissionTable.class);
                }
            };

        return tQuery.get();
    }
}
