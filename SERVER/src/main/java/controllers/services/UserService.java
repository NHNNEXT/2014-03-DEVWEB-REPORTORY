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

    public static User getUser(String email, String passwd, DBConnection dbConnection) throws SQLException, ForbiddenException {
        User user = UserTable.getQuery()
                .where((t) -> (t.email).isEqualTo(email).and
                        ((t.passwd).isEqualTo(passwd)))
                .first(dbConnection);

        if (user == null) {
            throw new ForbiddenException("No such user");
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
            throw new InternalServerErrorException("Register failed");
        }
    }

    private static Integer insertUser(User user, DBConnection dbConnection) throws SQLException {
        user.join_date = new Timestamp(System.currentTimeMillis());

        return UserTable.getQuery()
                .insertReturningGenKey(dbConnection, new User[]{user})
                .get(0);
    }

    public static JsonDataSerializable getDetailUser(Integer userId, DBConnection dbConnection) throws SQLException {
        List<Object> userList = new ArrayList<>();

        ProfessorUser professorUser = ProfessorUserJoin.getQuery()
                .where((t)->(t.uid) .isEqualTo( (userId)))
                .first(dbConnection);

        userList.add(professorUser);

        StudentUser studentUser = StudentUserJoin.getQuery()
                .where((t)->(t.uid) .isEqualTo( (userId)))
                .first(dbConnection);

        userList.add(studentUser);

        return (JsonDataSerializable) userList.get(0);

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

    public static boolean isProfessorUser(Request req){
        String type = (String) req.getSession("userType");
        return type!=null && type.equals("prof");
    }

    public static ProfessorUser getProfLoginData(Request req){
        if(!isProfessorUser(req))
            return null;
        return JsonDataSerializable.deserialize(ProfessorUser.class,(String)req.getSession("loginData"));
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

}
