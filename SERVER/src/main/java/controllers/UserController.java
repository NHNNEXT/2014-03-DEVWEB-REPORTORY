package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.POST;
import autumn.header.session.SessionData;
import models.*;
import models.tables.ProfessorTable;
import models.tables.StudentTable;
import models.tables.UserTable;
import models.tables.joined.ProfessorUserJoin;
import models.tables.joined.StudentUserJoin;
import util.ConsumerThatThrow;
import util.JsonDataSerializable;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by infinitu on 14. 12. 25..
 */

@Controller
public class UserController {

    // TODO: 로그인은 교수와 학생이 통합되어야 한다.
    // 로그인 정보는 교수와 학생과 무관하게 unique 하다.

    @POST("/login")
    public static Result login(Request req) throws SQLException {

        String email = req.body().asFormUrlEncoded().getParam("email");
        String passwd = req.body().asFormUrlEncoded().getParam("passwd");//todo

        System.out.println("email: " + email);
        System.out.println("passwd: " + passwd);

        if(email==null || passwd==null)
            return Result.BadRequest.plainText("input parameters");

        User user = UserTable.getQuery()
                .where((t)->(t.email) .isEqualTo (email) .and
                        ((t.passwd) .isEqualTo  (passwd)))
                .first(req.getDBConnection());

        if(user == null)
            return Result.Forbidden.plainText("no such user");

        List<JsonDataSerializable> userList = new ArrayList<>();

        ProfessorUser professorUser = ProfessorUserJoin.getQuery()
                .where((t)->(t.uid) .isEqualTo( (user.uid)))
                .first(req.getDBConnection());

        userList.add(professorUser);

        StudentUser studentUser = StudentUserJoin.getQuery()
                .where((t)->(t.uid) .isEqualTo( (user.uid)))
                .first(req.getDBConnection());

        userList.add(studentUser);

        String userType = "";

        if(userList.get(0) instanceof ProfessorUser) {
            userType = "prof";
        }

        if(userList.get(0) instanceof StudentUser) {
            userType = "stu";
        }

        return Result.Ok.plainText("logined with "+user.email)
                .withNewSession(
                        new SessionData<String>("userType",userType),
                        new SessionData<String>("loginData",userList.get(0).serialize())
                );
    }

    // TODO: 가입은 professor/student (collection)에 POST method로 대체되어야 한다.
    // REST URI에 register 라는 동사를 사용하는 것은 의미를 모호하게 한다.

    @POST("/professor")
    public static Result profRegister(Request req) throws SQLException {
        ProfessorUser record = req.body().asJson().mapping(ProfessorUser.class);
        return userRegister(req,record.toUser(),(id)->{
                    Professor prof = record.toProfessor();
                    prof.uid = id;

            ProfessorTable.getQuery()
                    .insert(req.getDBConnection(), new Professor[]{prof});
                });
    }

    @POST("/student")
    public static Result stuRegister(Request req) throws SQLException {
        StudentUser record = req.body().asJson().mapping(StudentUser.class);
        return userRegister(req, record.toUser(), (id) -> {
            Student stu = record.toStudent();
            stu.uid = id;
            StudentTable.getQuery()
                    .insert(req.getDBConnection(), new Student[]{stu});
        });
    }

    private static int insertUserRow(Request req, User record) throws SQLException {
        record.passwd = record.passwd;//todo
        record.join_date = new Timestamp(System.currentTimeMillis());
        return UserTable.getQuery()
                .insertRetunningGenKey(req.getDBConnection(), new User[]{record})
                        .get(0);
    }

    private static Result userRegister(Request req, User user, ConsumerThatThrow<Integer> other) throws SQLException {
        try {
            req.getDBConnection().transaction();
            int newid = insertUserRow(req, user);
            other.accept(newid);
        } catch (SQLException e) {
            e.printStackTrace();
            req.getDBConnection().rollBack();
            return Result.InternalServerError.plainText("register failed");
        }

        return Result.Ok.plainText("success.");
    }

    protected static boolean isStudentUser(Request req){
        String type = (String) req.getSession("userType");
        return type!=null && type.equals("stu");
    }

    protected static StudentUser getStuLoginData(Request req){
        if(!isStudentUser(req))
            return null;
        return JsonDataSerializable.deserialize(StudentUser.class,(String)req.getSession("loginData"));
    }

    protected static boolean isPrefessorUser(Request req){
        String type = (String) req.getSession("userType");
        return type!=null && type.equals("prof");
    }

    protected static ProfessorUser getProfLoginData(Request req){
        if(!isPrefessorUser(req))
            return null;
        return JsonDataSerializable.deserialize(ProfessorUser.class,(String)req.getSession("loginData"));
    }

}
