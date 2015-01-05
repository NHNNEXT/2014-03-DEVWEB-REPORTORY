package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.GET;
import autumn.annotation.INP;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SubmissionController {

    private static Class getController(Request req) {
        if (req.getAcceptType().equals("application/json")) {
            return SubmissionRestController.class;
        }

        return SubmissionViewController.class;
    }

    @GET("/lectures/{lectureId}/assignments/{assignmentId}/submissions")
    public static Result getSubmissionList(Request req,
                                        @INP("lectureId") String lectureId,
                                        @INP("assignmentId") String assignmentId) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = getController(req).getMethod("listSubmission", req.getClass(), lectureId.getClass(), assignmentId.getClass());
        return (Result) method.invoke(null, new Object[]{req, lectureId, assignmentId});
    }

    @GET("/lectures/{lectureId}/assignments/{assignmentId}/submissions/{submissionId}")
    public static Result getSubmissionView(Request req,
                                        @INP("lectureId") String lectureId,
                                        @INP("assignmentId") String assignmentId,
                                        @INP("submissionId") String submissionId) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = getController(req).getMethod("viewSubmission", req.getClass(), lectureId.getClass(), assignmentId.getClass(), submissionId.getClass());
        return (Result) method.invoke(null, new Object[]{req, lectureId, assignmentId, submissionId});
    }

}
