package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.POST;
import autumn.database.jdbc.DBConnection;
import autumn.header.session.SessionData;
import controllers.services.UserService;
import models.*;
import models.tables.ProfessorTable;
import models.tables.StudentTable;
import models.tables.UserTable;
import models.tables.joined.ProfessorUserJoin;
import models.tables.joined.StudentUserJoin;
import util.ConsumerThatThrow;
import util.JsonDataSerializable;
import util.JsonResult;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserRestController {

    @POST("/signin")
    public static Result signin(Request req) throws SQLException, UnsupportedEncodingException {

        String emailParam = req.body().asFormUrlEncoded().getParam("email");
        String passwdParam = req.body().asFormUrlEncoded().getParam("passwd");

        if(emailParam==null || passwdParam==null)
            return Result.BadRequest.json(new JsonResult("check input parameters"));
            // return Result.BadRequest.plainText("input parameters");

        String email = URLDecoder.decode(emailParam, "EUC_KR");
        String passwd = URLDecoder.decode(passwdParam, "EUC_KR");

        System.out.println("email: " + email);
        System.out.println("passwd: " + passwd);

        User user = UserTable.getQuery()
                .where((t) -> (t.email).isEqualTo(email).and
                        ((t.passwd).isEqualTo(passwd)))
                .first(req.getDBConnection());

        if(user == null)
            return Result.BadRequest.json(new JsonResult("no such user"));
            // return Result.Forbidden.plainText("no such user");

        JsonDataSerializable detailUser = (JsonDataSerializable) UserService.getUser(req.getDBConnection(), user.uid);

        return Result.Ok.plainText("logined with "+user.email)
                .withNewSession(
                        new SessionData<String>("userType", UserService.getUserType(detailUser)),
                        new SessionData<String>("loginData", detailUser.serialize())
                );
    }

    @POST("/professors")
    public static Result profRegister(Request req) throws SQLException {
        ProfessorUser record = req.body().asJson().mapping(ProfessorUser.class);
        return userRegister(req, record.toUser(), (id) -> {
            Professor prof = record.toProfessor();
            prof.uid = id;

            ProfessorTable.getQuery()
                    .insert(req.getDBConnection(), new Professor[]{prof});
        });
    }

    @POST("/students")
    public static Result stuRegister(Request req) throws SQLException {
        StudentUser record = req.body().asJson().mapping(StudentUser.class);
        return userRegister(req, record.toUser(), (id) -> {
            Student stu = record.toStudent();
            stu.uid = id;
            StudentTable.getQuery()
                    .insert(req.getDBConnection(), new Student[]{stu});
        });
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

    private static int insertUserRow(Request req, User record) throws SQLException {
        record.passwd = record.passwd;//todo
        record.join_date = new Timestamp(System.currentTimeMillis());
        return UserTable.getQuery()
                .insertReturningGenKey(req.getDBConnection(), new User[]{record})
                .get(0);
    }
}
