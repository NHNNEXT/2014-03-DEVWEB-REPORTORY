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

    private static ThreadLocal<TableQuery<LectureRegistrationTable>> tQuery;
    public Column<Integer> uid = intColumn("uid");
    public Column<Integer> lid = intColumn("lid");
    public Column<String> identity = stringColumn("identity");
    public Column<String> major = stringColumn("major");
    public Column<String> stu_name = stringColumn("stu_name");
    public Column<Boolean> accepted = booleanColumn("accepted");

    public LectureRegistrationTable() throws NoSuchFieldException {
        super(LectureRegistration.class);
    }

    public static TableQuery<LectureRegistrationTable> getQuery() {
        if (tQuery == null)
            tQuery = new ThreadLocal<TableQuery<LectureRegistrationTable>>() {
                @Override
                protected TableQuery<LectureRegistrationTable> initialValue() {
                    return new TableQuery<>(LectureRegistrationTable.class);
                }
            };

        return tQuery.get();
    }

}
