package models.tables;

import autumn.annotation.Model;
import autumn.database.Column;
import autumn.database.Table;
import autumn.database.TableQuery;
import models.User;

import java.sql.Date;

/**
 * Created by infinitu on 14. 12. 25..
 */
@Model("Users")
public class UserTable extends Table<User>{

    public Column<Integer>  uid           = intColumn   ("uid");
    public Column<String>   name          = stringColumn("name");
    public Column<String>   email         = stringColumn("email");
    public Column<String>   passwd        = stringColumn("passwd");
    public Column<String>   third_auth    = stringColumn("3rd_auth");
    public Column<Date>     join_date     = dateColumn("join_date");

    public UserTable() throws NoSuchFieldException {super(User.class);}

    private static TableQuery<UserTable> tQuery;
    public static TableQuery<UserTable> getQuery() throws NoSuchFieldException {
        if(tQuery==null)
            tQuery = new TableQuery<>(UserTable.class);
        return tQuery;
    }
}
