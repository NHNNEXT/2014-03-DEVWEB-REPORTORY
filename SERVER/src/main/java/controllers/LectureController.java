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

    @GET("/lectures/:id")
    public static Result getLectureView(Request req,
                                        @INP("id") String id) throws SQLException {
        if(req.getAcceptType().equals("application/json")) {
            return LectureRestController.viewLecture(req, id);
        }
        return Result.Ok.template("lectureView");
    }

    @GET("/lectures/new")
    public static Result getLectureCreate(Request req) {
        return Result.Ok.template("lectureCreate");
    }

}
