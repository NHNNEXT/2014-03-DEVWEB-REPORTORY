package models.tables;

import autumn.annotation.Model;
import autumn.database.Column;
import autumn.database.Table;
import autumn.database.TableQuery;
import models.User;

import java.sql.Timestamp;

/**
 * Created by infinitu on 14. 12. 25..
 */
@Model("Users")
public class UserTable extends Table<User> {

    private static ThreadLocal<TableQuery<UserTable>> tQuery;
    public Column<Integer> uid = intColumn("uid");
    public Column<String> name = stringColumn("name");
    public Column<String> email = stringColumn("email");
    public Column<String> passwd = stringColumn("passwd");
    public Column<String> third_auth = stringColumn("3rd_auth");
    public Column<Timestamp> join_date = timestampColumn("join_date");

    public UserTable() throws NoSuchFieldException {
        super(User.class);
    }

    public static TableQuery<UserTable> getQuery() {
        if (tQuery == null)
            tQuery = new ThreadLocal<TableQuery<UserTable>>() {
                @Override
                protected TableQuery<UserTable> initialValue() {
                    return new TableQuery<>(UserTable.class);
                }
            };

        return tQuery.get();
    }
}
