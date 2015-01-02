package controllers.services;

import autumn.Request;
import autumn.Result;
import autumn.database.jdbc.DBConnection;
import models.ProfessorUser;
import models.StudentUser;
import models.User;
import models.tables.UserTable;
import models.tables.joined.ProfessorUserJoin;
import models.tables.joined.StudentUserJoin;
import util.ConsumerThatThrow;
import util.JsonDataSerializable;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    public static Object getUser(DBConnection dbConnection, int uid) throws SQLException {
        List<Object> userList = new ArrayList<>();

        ProfessorUser professorUser = ProfessorUserJoin.getQuery()
                .where((t)->(t.uid) .isEqualTo( (uid)))
                .first(dbConnection);

        userList.add(professorUser);

        StudentUser studentUser = StudentUserJoin.getQuery()
                .where((t)->(t.uid) .isEqualTo( (uid)))
                .first(dbConnection);

        userList.add(studentUser);

        return userList.get(0);

    }

    public static String getUserType(Object user) {
        String userType = "";

        if(user instanceof ProfessorUser) {
            userType = "prof";
        }

        if(user instanceof StudentUser) {
            userType = "stu";
        }

        return userType;
    }

    public static boolean isStudentUser(Request req){
        String type = (String) req.getSession("userType");
        return type!=null && type.equals("stu");
    }

    public static StudentUser getStuLoginData(Request req){
        if(!isStudentUser(req))
            return null;
        return JsonDataSerializable.deserialize(StudentUser.class, (String) req.getSession("loginData"));
    }

    public static boolean isPrefessorUser(Request req){
        String type = (String) req.getSession("userType");
        return type!=null && type.equals("prof");
    }

    public static ProfessorUser getProfLoginData(Request req){
        if(!isPrefessorUser(req))
            return null;
        return JsonDataSerializable.deserialize(ProfessorUser.class,(String)req.getSession("loginData"));
    }

    public static User getUserLoginData(Request req) {
        if(isStudentUser(req)) {
            return getStuLoginData(req).toUser();
        }

        if(isPrefessorUser(req)) {
            return getProfLoginData(req).toUser();
        }

        return null;
    }

}
