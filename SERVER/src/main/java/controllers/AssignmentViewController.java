package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.GET;
import autumn.annotation.INP;

@Controller
public class AssignmentViewController {

    // @GET("/lectures/:lectureId/assignments")
    public static Result listAssignment(Request req,
                                        String lectureIdParam) {
        Integer lectureId = Integer.parseInt(lectureIdParam);

        return Result.Ok.template("assignmentList");
    }

    // @GET("/lectures/:lectureId/assignments/:assignmentId")
    public static Result viewAssignment(Request req,
                                        String lectureIdParam, String assignmentIdParam) {
        Integer lectureId = Integer.parseInt(lectureIdParam);
        Integer assignmentId = Integer.parseInt(assignmentIdParam);

        return Result.Ok.template("assignmentView");
    }

    @GET("/lectures/:lectureId/assignments/new")
    public static Result getAssignmentCreate(Request req,
                                             @INP("lectureId") String lectureIdParam) {
        Integer lectureId = Integer.parseInt(lectureIdParam);

        return Result.Ok.template("assignmentCreate");
    }
}
