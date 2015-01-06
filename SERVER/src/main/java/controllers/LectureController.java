package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.GET;
import autumn.annotation.INP;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Controller
public class LectureController {

    private static Class getController(Request req) {
        if (req.getAcceptType().equals("application/json")) {
            return LectureRestController.class;
        }

        return LectureViewController.class;
    }

    @GET("/lectures")
    public static Result getLectureList(Request req) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = getController(req).getMethod("listLecture", req.getClass());
        return (Result) method.invoke(null, req);
    }

    @GET("/lectures/:lectureId")
    public static Result getLectureView(Request req,
                                        @INP("lectureId") String lectureId) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = getController(req).getMethod("viewLecture", req.getClass(), lectureId.getClass());
        return (Result) method.invoke(null, req, lectureId);
    }

}
