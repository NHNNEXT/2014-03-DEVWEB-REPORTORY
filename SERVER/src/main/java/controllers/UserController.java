package controllers;

import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.GET;

/**
 * Created by Byeol on 2015. 1. 2..
 */

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

