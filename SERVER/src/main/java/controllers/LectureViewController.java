package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.GET;
import controllers.action.ViewAction;
import controllers.services.LectureService;
import controllers.services.UserService;
import util.exceptions.ForbiddenException;

public class LectureViewController {

    // @GET("/lectures")
    public static Result listLecture(Request req) {
        return Result.Ok.template("lectureList");
    }

    // @GET("/lectures/:lectureId")
    public static Result viewLecture(Request req, String lectureId) {
        return ViewAction.doAction(() -> {
            if (!(UserService.isStudentUser(req) || UserService.isProfessorUser(req)))
                throw new ForbiddenException("permission_denied");
            return Result.Ok.template("lectureView").withVariable("lecture", LectureService.getLecture(Integer.parseInt(lectureId), req.getDBConnection()));
        });
    }

    @GET("/lectures/new")
    public static Result createLecture(Request req) {
        return Result.Ok.template("lectureCreate");
    }

}
