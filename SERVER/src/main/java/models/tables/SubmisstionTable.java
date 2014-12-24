package models.tables;

import autumn.annotation.Model;
import autumn.database.Column;
import autumn.database.Table;
import autumn.database.TableQuery;
import models.Submisstion;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by infinitu on 14. 12. 25..
 */
@Model("Submissions")
public class SubmisstionTable extends Table<Submisstion>{

    public Column<Integer>      uid         = intColumn("uid")   ;
    public Column<Integer>      lid         = intColumn("lid")   ;
    public Column<Integer>      aid         = intColumn("aid")   ;
    public Column<Integer>      revision    = intColumn("revision")   ;
    public Column<String>       description = stringColumn("description");
    public Column<Timestamp>    create_time = timestampColumn("create_date");


    public SubmisstionTable() throws NoSuchFieldException {
        super(Submisstion.class);
    }

    private static TableQuery<SubmisstionTable> tQuery;
    public static TableQuery<SubmisstionTable> getQuery() throws NoSuchFieldException {
        if(tQuery==null)
            tQuery = new TableQuery<>(SubmisstionTable.class);
        return tQuery;
    }
}
