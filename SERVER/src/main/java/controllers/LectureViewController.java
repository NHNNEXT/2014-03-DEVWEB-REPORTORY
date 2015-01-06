package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.GET;
import controllers.action.ViewAction;
import controllers.services.LectureService;
import controllers.services.UserService;
import util.exceptions.ForbiddenException;

@Controller
public class LectureViewController {

    // @GET("/lectures")
    public static Result listLecture(Request req) {
        return ViewAction.doActionWithLoginUser(req, () -> Result.Ok.template("lectureList"));
    }

    // @GET("/lectures/:lectureId")
    public static Result viewLecture(Request req, String lectureIdParam) {
        Integer lectureId = Integer.parseInt(lectureIdParam);

        return ViewAction.doActionWithLoginUser(req, () -> {
            if (!(UserService.isStudentUser(req) || UserService.isProfessorUser(req)))
                throw new ForbiddenException("permission_denied");
            return Result.Ok.template("lectureView")
                    .withVariable("lecture", LectureService.getLecture(lectureId, req.getDBConnection()));
        });
    }

    @GET("/lectures/new")
    public static Result createLecture(Request req) {
        return ViewAction.doActionWithLoginUser(req, () -> Result.Ok.template("lectureCreate"));
    }

}
