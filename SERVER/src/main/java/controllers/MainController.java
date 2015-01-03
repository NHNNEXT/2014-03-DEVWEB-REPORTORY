package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.GET;
import controllers.rest.RestAction;
import controllers.services.UserService;
import models.User;
import util.exceptions.ForbiddenException;

@Controller
public class MainController {

    // TODO: static 리소스에 대한 접근 방법이 필요하다.

    @GET("/")
    public static Result getMain(Request req) {
        return Result.Ok.template("home").withVariable("loginUser", UserService.getUserLoginData(req));
    }

    @GET("/some-path/1")
    public static Result acceptTypeTest(Request req) {
        System.out.println(req.getAcceptType());
        if(req.getAcceptType().equals("application/json")) {
            return Result.Ok.plainText("json");
        }
        return Result.Ok.plainText("html");
    }

    @GET("/some-path/2")
    public static Result someTest(Request req) {
        System.out.println(req.getSession("userType"));
        System.out.println(req.getSession("loginData"));

        System.out.println(UserService.getUserLoginData(req));

        User loginUser = UserService.getUserLoginData(req);

        // return Result.Ok.template("home").withVariable("loginUser", loginUser.name);
        return Result.Ok.template("home").withVariable("loginUser", loginUser);
    }

    @GET("/rest-test")
    public static Result restTestFunc(Request req) {
        return RestAction.doAction(() -> {
            System.out.println(req.getPath());
            throw new ForbiddenException("testMessage");
            // return Result.Ok.json(new JsonResult("It just works"));
        });
    }
}
