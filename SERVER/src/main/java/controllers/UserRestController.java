package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.POST;
import autumn.header.session.SessionData;
import controllers.services.UserService;
import models.ProfessorUser;
import models.StudentUser;
import models.User;
import util.JsonDataSerializable;
import util.JsonResult;
import util.exceptions.ForbiddenException;
import util.exceptions.InternalServerErrorException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;

@Controller
public class UserRestController {

    @POST("/signin")
    public static Result signIn(Request req) throws SQLException, UnsupportedEncodingException {

        String emailParam = req.body().asFormUrlEncoded().getParam("email");
        String passwdParam = req.body().asFormUrlEncoded().getParam("passwd");

        if (emailParam == null || passwdParam == null)
            return Result.BadRequest.json(new JsonResult("Check input parameters"));

        String email = URLDecoder.decode(emailParam, "EUC_KR");
        String passwd = URLDecoder.decode(passwdParam, "EUC_KR");

        try {
            User user = UserService.getUser(email, passwd, req.getDBConnection());
            JsonDataSerializable detailUser = UserService.getDetailUser(user.uid, req.getDBConnection());

            return Result.Ok.json(new JsonResult("Logged in with " + user.email))
                    .withNewSession(
                            new SessionData<String>("userType", UserService.getUserType(detailUser)),
                            new SessionData<String>("loginData", detailUser.serialize())
                    );
        } catch (ForbiddenException e) {
            return Result.Forbidden.json(new JsonResult(e.getMessage()));
        }
    }

    @POST("/professors")
    public static Result registerProfessor(Request req) throws SQLException {
        ProfessorUser professorUser = req.body().asJson().mapping(ProfessorUser.class);

        try {
            UserService.registerProfessor(professorUser, req.getDBConnection());
            return Result.Ok.json(new JsonResult("Professor created"));
        } catch (InternalServerErrorException e) {
            return Result.InternalServerError.json(new JsonResult(e.getMessage()));
        }
    }
    @POST("/students")
    public static Result registerStudent(Request req) throws SQLException {
        StudentUser studentUser = req.body().asJson().mapping(StudentUser.class);

        try {
            UserService.registerStudent(studentUser, req.getDBConnection());
            return Result.Ok.json(new JsonResult("Student created"));
        } catch (InternalServerErrorException e) {
            return Result.InternalServerError.json(new JsonResult(e.getMessage()));
        }
    }

}
