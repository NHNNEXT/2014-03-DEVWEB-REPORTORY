package models.tables.joined;

import autumn.database.Column;
import autumn.database.Condition;
import autumn.database.JoinQuery;
import autumn.database.JoinTable;
import models.StudentUser;
import models.tables.StudentTable;
import models.tables.UserTable;

import java.sql.Date;

/**
 * Created by infinitu on 14. 12. 25..
 */
public class StudentUserJoin extends JoinTable<UserTable,StudentTable, StudentUser>{

    public Column<Integer> uid         = left.uid;
    public Column<String>   name        = left.name;
    public Column<String>   email       = left.email;
    public Column<String>   passwd      = left.passwd;
    public Column<String>   third_auth  = left.third_auth;
    public Column<Date>     join_date   = left.join_date;
    public Column<String>   defIdentity = right.defIdentity;
    public Column<String>   defMajor    = right.defMajor;

    public StudentUserJoin() throws NoSuchFieldException {
        super(new UserTable(), new StudentTable(), StudentUser.class);
    }

    @Override
    public Condition on(UserTable userTable, StudentTable studentTable) {
        return (userTable.uid) .isEqualTo  (studentTable.uid);
    }

    private static JoinQuery<StudentUserJoin> tQuery;
    public static JoinQuery<StudentUserJoin> getQuery() {
        if(tQuery==null)
            tQuery = new JoinQuery<>(StudentUserJoin.class);
        return tQuery;
    }
}
