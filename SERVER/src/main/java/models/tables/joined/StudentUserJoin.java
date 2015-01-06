package models.tables.joined;

import autumn.database.Column;
import autumn.database.Condition;
import autumn.database.JoinQuery;
import autumn.database.JoinTable;
import models.StudentUser;
import models.tables.StudentTable;
import models.tables.UserTable;

import java.sql.Timestamp;

/**
 * Created by infinitu on 14. 12. 25..
 */
public class StudentUserJoin extends JoinTable<UserTable, StudentTable, StudentUser> {

    private static ThreadLocal<JoinQuery<StudentUserJoin>> tQuery;
    public Column<Integer> uid = left.uid;
    public Column<String> name = left.name;
    public Column<String> email = left.email;
    public Column<String> passwd = left.passwd;
    public Column<String> third_auth = left.third_auth;
    public Column<Timestamp> join_date = left.join_date;
    public Column<String> defIdentity = right.defIdentity;
    public Column<String> defMajor = right.defMajor;

    public StudentUserJoin() throws NoSuchFieldException {
        super(new UserTable(), new StudentTable(), StudentUser.class);
    }

    public static JoinQuery<StudentUserJoin> getQuery() {
        if (tQuery == null)
            tQuery = new ThreadLocal<JoinQuery<StudentUserJoin>>() {
                @Override
                protected JoinQuery<StudentUserJoin> initialValue() {
                    return new JoinQuery<>(StudentUserJoin.class);
                }
            };
        return tQuery.get();
    }

    @Override
    public Condition on(UserTable userTable, StudentTable studentTable) {
        return (userTable.uid).isEqualTo(studentTable.uid);
    }
}
