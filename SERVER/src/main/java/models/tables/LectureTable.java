package models.tables;

import autumn.annotation.Model;
import autumn.database.Column;
import autumn.database.Table;
import autumn.database.TableQuery;
import models.Lecture;

import java.sql.Date;

/**
 * Created by infinitu on 14. 12. 25..
 */

@Model("Lecture")
public class LectureTable extends Table<Lecture>{

    public Column<Integer>  lid  = intColumn("lid");
    public Column<Integer>  prof = intColumn("prof");
    public Column<String>   name = stringColumn("name");
    public Column<Date>     startDate = dateColumn("start_date");
    public Column<Date>     finishDate = dateColumn("finish_date");

    public LectureTable() throws NoSuchFieldException {
        super(Lecture.class);
    }

    private static TableQuery<LectureTable> tQuery;
    public static TableQuery<LectureTable> getQuery() throws NoSuchFieldException {
        if(tQuery==null)
            tQuery = new TableQuery<>(LectureTable.class);
        return tQuery;
    }

}
