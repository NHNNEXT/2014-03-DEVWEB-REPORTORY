package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.GET;
import autumn.annotation.INP;

import java.sql.SQLException;

@Controller
public class LectureController {

    @GET("/lectures")
    public static Result getLectureList(Request req) throws SQLException {
        if(req.getAcceptType().equals("application/json")) {
            return LectureRestController.listLecture(req);
        }
        return Result.Ok.template("lectureList");
    }

    @GET("/lectures/:lectureId")
    public static Result getLectureView(Request req,
                                        @INP("lectureId") String lectureId) throws SQLException {
        if(req.getAcceptType().equals("application/json")) {
            return LectureRestController.viewLecture(req, lectureId);
        }
        return Result.Ok.template("lectureView");
    }

    @GET("/lectures/new")
    public static Result getLectureCreate(Request req) {
        return Result.Ok.template("lectureCreate");
    }

}
