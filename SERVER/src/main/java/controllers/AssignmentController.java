package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.GET;
import autumn.annotation.INP;

@Controller
public class AssignmentController {

    @GET("/lectures/:lectureId/assignments")
    public static Result getAssignmentList(Request req,
                                           @INP("lectureId") String lectureId) {
        if(req.getAcceptType().equals("application/json")) {
            return AssignmentRestController.listAssignment(req, lectureId);
        }
        return Result.Ok.template("assignmentList");
    }

    @GET("/lectures/:lectureId/assignments/:assignmentId")
    public static Result getAssignmentView(Request req,
                                        @INP("lectureId") String lectureId,
                                        @INP("assignmentId") String assignmentId) {
        if(req.getAcceptType().equals("application/json")) {
            return AssignmentRestController.viewAssignment(req, assignmentId);
        }
        return Result.Ok.template("assignmentView");
    }

    @GET("/lectures/:lectureId/assignments/new")
    public static Result getAssignmentCreate(Request req,
                                             @INP("lectureId") String lectureId) {
        return Result.Ok.template("assignmentCreate");
    }

}
