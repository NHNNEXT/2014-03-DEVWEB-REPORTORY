package controllers;

import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.GET;

@Controller
public class UserController {

    @GET("/signin")
    public static Result getSigninPage() {
        return Result.Ok.template("signin");
    }

    @GET("/signup")
    public static Result getSignupPage() {
        return Result.Ok.template("signup");
    }

}

