package models.tables.joined;

import autumn.database.Column;
import autumn.database.Condition;
import autumn.database.JoinQuery;
import autumn.database.JoinTable;
import models.ProfessorUser;
import models.tables.ProfessorTable;
import models.tables.UserTable;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by infinitu on 14. 12. 25..
 */
public class ProfessorUserJoin extends JoinTable<UserTable, ProfessorTable, ProfessorUser>{

    public Column<Integer>  uid         = left.uid;
    public Column<String>   name        = left.name;
    public Column<String>   email       = left.email;
    public Column<String>   passwd      = left.passwd;
    public Column<String>   third_auth  = left.third_auth;
    public Column<Timestamp>     join_date   = left.join_date;
    public Column<String>   major       = right.major;

    public ProfessorUserJoin() throws NoSuchFieldException {
        super(new UserTable(), new ProfessorTable(), ProfessorUser.class);
    }

    @Override
    public Condition on(UserTable userTable, ProfessorTable professorTable) {
        return (userTable.uid) .isEqualTo  (professorTable.uid);
    }

    private static ThreadLocal<JoinQuery<ProfessorUserJoin>> tQuery;
    public static JoinQuery<ProfessorUserJoin> getQuery() {
        if (tQuery == null)
            tQuery = new ThreadLocal<JoinQuery<ProfessorUserJoin>>() {
                @Override
                protected JoinQuery<ProfessorUserJoin> initialValue() {
                    return new JoinQuery<>(ProfessorUserJoin.class);
                }
            };
        return tQuery.get();
    }
}
