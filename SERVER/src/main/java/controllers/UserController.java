package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.GET;
import autumn.header.session.SessionData;
import controllers.services.UserService;
import util.ResultResponse;

@Controller
public class UserController {

    @GET("/signin")
    public static Result getSigninPage(Request req) {
        return Result.Ok.template("signin");
    }

    @GET("/signup")
    public static Result getSignupPage(Request req) {
        return Result.Ok.template("signup");
    }

    @GET("/signout")
    public static Result signOut(Request req) {

        return Result.Ok.plainText("<script>javascript:window.location='/';</script>")
                .contentType("text/html")
                .withNewSession();
    }

}

