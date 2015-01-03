package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.GET;
import autumn.annotation.INP;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Controller
public class AssignmentController {

    private static Class getController(Request req) {
        if (req.getAcceptType().equals("application/json")) {
            return AssignmentRestController.class;
        }

        return AssignmentViewController.class;
    }

    @GET("/lectures/:lectureId/assignments")
    public static Result getAssignmentList(Request req,
                                           @INP("lectureId") String lectureId) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = getController(req).getMethod("listAssignment", req.getClass(), lectureId.getClass());
        return (Result) method.invoke(null, new Object[]{req, lectureId});
    }


    @GET("/lectures/:lectureId/assignments/:assignmentId")
    public static Result getAssignmentView(Request req,
                                        @INP("lectureId") String lectureId,
                                        @INP("assignmentId") String assignmentId) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = getController(req).getMethod("viewAssignment", req.getClass(), lectureId.getClass(), assignmentId.getClass());
        return (Result) method.invoke(null, new Object[]{req, lectureId, assignmentId});
    }

}
