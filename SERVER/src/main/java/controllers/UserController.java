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
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by infinitu on 14. 12. 25..
 */

@Controller
public class UserController {

    //public static PasswdEncrypter

    @POST("/professor/login")
    public static Result profLogin(Request req) throws SQLException {

        String email = req.body().asFormUrlEncoded().getParam("email");
        String passwd = req.body().asFormUrlEncoded().getParam("passwd");//todo

        if(email==null || passwd==null)
            return Result.BadRequest.plainText("input parameters");

        List<ProfessorUser> profList = ProfessorUserJoin.getQuery()
                .where((t)->(t.email) .isEqualTo (email) .and
                        ((t.passwd) .isEqualTo  (passwd)))
                .list(req.getDBConnection());

        if(profList.isEmpty())
            return Result.Forbidden.plainText("no such professor");

        ProfessorUser logindata = profList.get(0);

        return Result.Ok.plainText("logined with "+logindata.email)
                .withNewSession(
                        new SessionData<String>("userType","prof"),
                        new SessionData<String>("loginData",logindata.serialize())
                );
    }


    @POST("/student/login")
    public static Result stuLogin(Request req) throws SQLException {

        String email = req.body().asFormUrlEncoded().getParam("email");
        String passwd = req.body().asFormUrlEncoded().getParam("passwd");//todo

        if(email==null || passwd==null)
            return Result.BadRequest.plainText("input parameters");

        List<StudentUser> profList = StudentUserJoin.getQuery()
                .where((t)->(t.email) .isEqualTo (email) .and
                        ((t.passwd) .isEqualTo  (passwd)))
                .list(req.getDBConnection());

        if(profList.isEmpty())
            return Result.Forbidden.plainText("no such student");

        StudentUser logindata = profList.get(0);

        return Result.Ok.plainText("logined with "+logindata.email)
                .withNewSession(
                        new SessionData<String>("userType","stu"),
                        new SessionData<String>("loginData",logindata.serialize())
                );
    }

    @POST("/professor/register")
    public static Result profRegister(Request req) throws SQLException {
        ProfessorUser record = req.body().asJson().mapping(ProfessorUser.class);
        return userRegister(req,record.toUser(),(id)->{
                    Professor prof = record.toProfessor();
                    prof.uid = id;

                    ProfessorTable.getQuery()
                            .insert(req.getDBConnection(), new Professor[]{prof});
                });
    }

    @POST("/student/register")
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
                        .insertReturningGenKey(req.getDBConnection(), new User[]{record})
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
