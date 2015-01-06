package controllers.services;

import autumn.Request;
import autumn.database.jdbc.DBConnection;
import models.*;
import models.tables.ProfessorTable;
import models.tables.StudentTable;
import models.tables.UserTable;
import models.tables.joined.ProfessorUserJoin;
import models.tables.joined.StudentUserJoin;
import util.ConsumerThatThrow;
import util.JsonDataSerializable;
import util.exceptions.ForbiddenException;
import util.exceptions.InternalServerErrorException;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    public final static String USER_TYPE_SESSION_NAME = "userType";
    public final static String LOGIN_DATA_SESSION_NAME = "loginData";

    public final static String USER_TYPE_STU = "stu";
    public final static String USER_TYPE_PROF = "prof";

    public static User getUser(String email, String passwd, DBConnection dbConnection) throws SQLException, ForbiddenException {
        User user = UserTable.getQuery()
                .where((t) -> (t.email).isEqualTo(email).and
                        ((t.passwd).isEqualTo(passwd)))
                .first(dbConnection);

        if (user == null) {
            throw new ForbiddenException("no_such_user");
        }

        return user;
    }

    public static void registerProfessor(ProfessorUser professorUser, DBConnection dbConnection) throws SQLException, InternalServerErrorException {
        registerUser(professorUser.toUser(), (userId) -> {
            Professor professor = professorUser.toProfessor();
            professor.uid = userId;
            ProfessorTable.getQuery()
                    .insert(dbConnection, new Professor[]{professor});
        }, dbConnection);
    }

    public static void registerStudent(StudentUser studentUser, DBConnection dbConnection) throws SQLException, InternalServerErrorException {
        registerUser(studentUser.toUser(), (userId) -> {
            Student student = studentUser.toStudent();
            student.uid = userId;
            StudentTable.getQuery()
                    .insert(dbConnection, new Student[]{student});
        }, dbConnection);
    }

    public static void registerUser(User user, ConsumerThatThrow<Integer> detailUserRegister, DBConnection dbConnection) throws SQLException, InternalServerErrorException {
        try {
            dbConnection.transaction();
            Integer userId = insertUser(user, dbConnection);
            detailUserRegister.accept(userId);
        } catch (SQLException e) {
            dbConnection.rollBack();
            e.printStackTrace();
            throw new InternalServerErrorException("register_failed");
        }
    }

    private static Integer insertUser(User user, DBConnection dbConnection) throws SQLException {
        user.join_date = new Timestamp(System.currentTimeMillis());

        return UserTable.getQuery()
                .insertReturningGenKey(dbConnection, new User[]{user})
                .get(0);
    }

    public static JsonDataSerializable getDetailUser(Integer userId, DBConnection dbConnection) throws SQLException, ForbiddenException {
        List<Object> userList = new ArrayList<>();

        ProfessorUser professorUser = ProfessorUserJoin.getQuery()
                .where((t)->(t.uid) .isEqualTo( (userId)))
                .first(dbConnection);

        if(professorUser != null)
            return professorUser;

        StudentUser studentUser = StudentUserJoin.getQuery()
                .where((t)->(t.uid) .isEqualTo( (userId)))
                .first(dbConnection);

        if(studentUser != null)
            return studentUser;

        throw new ForbiddenException("no_such_user");
    }

    public static String getUserType(Object user) {
        String userType = "";

        if(user instanceof ProfessorUser) {
            userType = USER_TYPE_PROF;
        }

        if(user instanceof StudentUser) {
            userType = USER_TYPE_STU;
        }

        return userType;
    }

    public static boolean isStudentUser(Request req){
        String type = (String) req.getSession(USER_TYPE_SESSION_NAME);
        return type!=null && type.equals(USER_TYPE_STU);
    }

    public static StudentUser getStuLoginData(Request req){
        if(!isStudentUser(req))
            return null;
        return JsonDataSerializable.deserialize(StudentUser.class, (String) req.getSession(LOGIN_DATA_SESSION_NAME));
    }

    public static boolean isProfessorUser(Request req){
        String type = (String) req.getSession(USER_TYPE_SESSION_NAME);
        return type!=null && type.equals(USER_TYPE_PROF);
    }

    public static ProfessorUser getProfLoginData(Request req){
        if(!isProfessorUser(req))
            return null;
        return JsonDataSerializable.deserialize(ProfessorUser.class,(String)req.getSession(LOGIN_DATA_SESSION_NAME));
    }

    public static User getUserLoginData(Request req) {
        if(isStudentUser(req)) {
            return getStuLoginData(req).toUser();
        }

        if(isProfessorUser(req)) {
            return getProfLoginData(req).toUser();
        }

        return null;
    }

    public static String getLoginUserType(Request req) {
        return (String) req.getSession(USER_TYPE_SESSION_NAME);
    }
}
