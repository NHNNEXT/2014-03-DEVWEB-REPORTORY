package models.tables;

import autumn.annotation.Model;
import autumn.database.Column;
import autumn.database.Table;
import autumn.database.TableQuery;
import models.LectureRegistration;

/**
 * Created by infinitu on 14. 12. 25..
 */

@Model("Lecture_Registrations")
public class LectureRegistrationTable extends Table<LectureRegistration> {

    public Column<Integer>  uid     = intColumn("uid");
    public Column<Integer>  lid     = intColumn("lid");
    public Column<String>   identity= stringColumn("identity");
    public Column<String>   major   = stringColumn("major");
    public Column<String>   stu_name= stringColumn("stu_name");

    public LectureRegistrationTable() throws NoSuchFieldException {
        super(LectureRegistration.class);
    }

    private static TableQuery<LectureRegistrationTable> tQuery;
    public static TableQuery<LectureRegistrationTable> getQuery() {
        if(tQuery==null)
            tQuery = new TableQuery<>(LectureRegistrationTable.class);
        return tQuery;
    }

}
