package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.POST;
import autumn.header.session.SessionData;
import controllers.rest.RestAction;
import controllers.services.UserService;
import models.ProfessorUser;
import models.StudentUser;
import models.User;
import util.JsonDataSerializable;
import util.JsonResult;
import util.exceptions.BadRequestException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Controller
public class UserRestController {

    @POST("/signin")
    public static Result signIn(Request req) {
        return RestAction.doAction(() -> {
            String emailParam = req.body().asFormUrlEncoded().getParam("email");
            String passwdParam = req.body().asFormUrlEncoded().getParam("passwd");

            if (emailParam == null || passwdParam == null) {
                throw new BadRequestException("invalid_request");
            }

            String email = null;
            String passwd = null;
            try {
                email = URLDecoder.decode(emailParam, "EUC_KR");
                passwd = URLDecoder.decode(passwdParam, "EUC_KR");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            User user = UserService.getUser(email, passwd, req.getDBConnection());
            JsonDataSerializable detailUser = UserService.getDetailUser(user.uid, req.getDBConnection());

            return Result.Ok.json(new JsonResult("Logged in with " + user.email))
                    .withNewSession(
                            new SessionData<String>("userType", UserService.getUserType(detailUser)),
                            new SessionData<String>("loginData", detailUser.serialize())
                    );
        });
    }

    @POST("/professors")
    public static Result registerProfessor(Request req) {
        return RestAction.doAction(() -> {
            ProfessorUser professorUser = req.body().asJson().mapping(ProfessorUser.class);
            UserService.registerProfessor(professorUser, req.getDBConnection());

            return Result.Ok.json(new JsonResult("Professor created"));
        });
    }

    @POST("/students")
    public static Result registerStudent(Request req) {
        return RestAction.doAction(() -> {
            StudentUser studentUser = req.body().asJson().mapping(StudentUser.class);
            UserService.registerStudent(studentUser, req.getDBConnection());

            return Result.Ok.json(new JsonResult("Student created"));
        });
    }

}
