package models.tables;

import autumn.annotation.Model;
import autumn.database.Column;
import autumn.database.Table;
import autumn.database.TableQuery;
import models.Student;

/**
 * Created by infinitu on 14. 12. 25..
 */

@Model("Students")
public class StudentTable extends Table<Student>{

    public Column<Integer>  uid         = intColumn("uid");
    public Column<String>   defIdentity = stringColumn("def_identity");
    public Column<String>   defMajor    = stringColumn("major");

    public StudentTable() throws NoSuchFieldException {
        super(Student.class);
    }

    private static TableQuery<StudentTable> tQuery;
    public static TableQuery<StudentTable> getQuery() {
        if(tQuery==null)
            tQuery = new TableQuery<>(StudentTable.class);
        return tQuery;
    }

}
