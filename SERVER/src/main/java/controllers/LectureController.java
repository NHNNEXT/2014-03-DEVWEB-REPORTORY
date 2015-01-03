package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.GET;
import autumn.annotation.INP;
import controllers.services.LectureService;
import controllers.services.UserService;
import util.exceptions.ForbiddenException;
import util.exceptions.NotFoundException;

import java.sql.SQLException;

@Controller
public class LectureController {

    @GET("/lectures")
    public static Result getLectureList(Request req) {
        if(req.getAcceptType().equals("application/json")) {
            return LectureRestController.listLecture(req);
        }
        return Result.Ok.template("lectureList");
    }

    @GET("/lectures/:lectureId")
    public static Result getLectureView(Request req,
                                        @INP("lectureId") String lectureId) {
        if (req.getAcceptType().equals("application/json")) {
            return LectureRestController.viewLecture(req, lectureId);
        }

        return viewLecture(req, lectureId);
    }

    // @GET("/lectures/:lectureId")
    public static Result viewLecture(Request req, String lectureId) {
        try {
            if (!(UserService.isStudentUser(req) || UserService.isProfessorUser(req)))
                throw new ForbiddenException("permission_denied");
            return Result.Ok.template("lectureView").withVariable("lecture", LectureService.getLecture(Integer.parseInt(lectureId), req.getDBConnection()));
        } catch (ForbiddenException e) {
            return Result.Forbidden.template("error").withVariable("errorMessage", e.getLocalizedMessage());
        } catch (NotFoundException e) {
            return Result.NotFound.template("error").withVariable("errorMessage", e.getLocalizedMessage());
        } catch (SQLException e) {
            return Result.InternalServerError.template("error").withVariable("errorMessage", e.getLocalizedMessage());
        }
    }

    @GET("/lectures/new")
    public static Result getLectureCreate(Request req) {
        return Result.Ok.template("lectureCreate");
    }

}
