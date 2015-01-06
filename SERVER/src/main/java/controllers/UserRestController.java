package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.POST;
import autumn.header.session.SessionData;
import controllers.action.RestAction;
import controllers.services.UserService;
import models.ProfessorUser;
import models.StudentUser;
import models.User;
import util.JsonDataSerializable;
import util.ResultResponse;
import util.exceptions.BadRequestException;
import util.exceptions.InternalServerErrorException;

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
                throw new InternalServerErrorException(e.getLocalizedMessage());
            }

            User user = UserService.getUser(email, passwd, req.getDBConnection());
            JsonDataSerializable detailUser = UserService.getDetailUser(user.uid, req.getDBConnection());

            return Result.Ok.json(new ResultResponse("Logged in with " + user.email))
                    .withNewSession(
                            new SessionData<String>(UserService.USER_TYPE_SESSION_NAME, UserService.getUserType(detailUser)),
                            new SessionData<String>(UserService.LOGIN_DATA_SESSION_NAME, detailUser.serialize())
                    );
        });
    }

    @POST("/professors")
    public static Result registerProfessor(Request req) {
        return RestAction.doAction(() -> {
            ProfessorUser professorUser = req.body().asJson().mapping(ProfessorUser.class);
            UserService.registerProfessor(professorUser, req.getDBConnection());

            return Result.Ok.json(new ResultResponse("Professor created"));
        });
    }

    @POST("/students")
    public static Result registerStudent(Request req) {
        return RestAction.doAction(() -> {
            StudentUser studentUser = req.body().asJson().mapping(StudentUser.class);
            UserService.registerStudent(studentUser, req.getDBConnection());

            return Result.Ok.json(new ResultResponse("Student created"));
        });
    }

}
