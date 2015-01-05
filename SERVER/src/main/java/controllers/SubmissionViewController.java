package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.GET;
import autumn.annotation.INP;

public class SubmissionViewController {

    // @GET("/lectures/{lectureId}/assignments/{assignmentId}/submissions")
    public static Result listSubmission(Request req,
                                        String lectureIdParam, String assignmentIdParam) {
        int lectureId = Integer.parseInt(lectureIdParam);
        int assignmentId = Integer.parseInt(lectureIdParam);

        return Result.Ok.template("submissionList");
    }

    // @GET("/lectures/{lectureId}/assignments/{assignmentId}/submissions/{submissionId}")
    public static Result viewSubmission(Request req,
                                        String lectureIdParam, String assignmentIdParam, String submissionIdParam) {
        int lectureId = Integer.parseInt(lectureIdParam);
        int assignmentId = Integer.parseInt(lectureIdParam);
        int submissionId = Integer.parseInt(submissionIdParam);

        return Result.Ok.template("submissionView");
    }

    @GET("/lectures/:lectureId/assignments/{assignmentId}/submissions/new")
    public static Result getAssignmentCreate(Request req,
                                             @INP("lectureId") String lectureIdParam,
                                             @INP("assignmentId") String assignmentIdParam) {
        Integer lectureId = Integer.parseInt(lectureIdParam);
        int assignmentId = Integer.parseInt(assignmentIdParam);

        return Result.Ok.template("submissionCreate");
    }
}
