package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.GET;
import controllers.action.ViewAction;

@Controller
public class MainController {

    @GET("/")
    public static Result getMain(Request req) {
        return ViewAction.doActionWithLoginUser(req, () -> Result.Ok.template("home"));
    }

    @GET("/about")
    public static Result getAbout(Request req) {
        return ViewAction.doAction(() -> Result.Ok.template("about"));
    }
}
