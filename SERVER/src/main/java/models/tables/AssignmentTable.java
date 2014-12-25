package models.tables;

import autumn.annotation.Model;
import autumn.database.Column;
import autumn.database.Table;
import autumn.database.TableQuery;
import models.Assignment;

import java.sql.Timestamp;

/**
 * Created by infinitu on 14. 12. 25..
 */

@Model("Assignment")
public class AssignmentTable extends Table<Assignment>{

    public Column<Integer>   aid = intColumn("aid");
    public Column<Integer>   lid = intColumn("lid");
    public Column<Timestamp> createTime = timestampColumn("create_date");
    public Column<Timestamp> dueTime = timestampColumn("due_date");
    public Column<String>    title = stringColumn("title");
    public Column<String>    description = stringColumn("description");

    public AssignmentTable() throws NoSuchFieldException {
        super(Assignment.class);
    }

    private static TableQuery<AssignmentTable> tQuery;
    public static TableQuery<AssignmentTable> getQuery() {
        if(tQuery==null)
            tQuery = new TableQuery<>(AssignmentTable.class);
        return tQuery;
    }

}
