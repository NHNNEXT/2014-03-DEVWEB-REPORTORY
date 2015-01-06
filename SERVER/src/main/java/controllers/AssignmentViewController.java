package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.GET;
import autumn.annotation.INP;
import controllers.action.ViewAction;
import controllers.services.AssignmentService;
import controllers.services.UserService;
import util.exceptions.ForbiddenException;

@Controller
public class AssignmentViewController {

    // @GET("/lectures/:lectureId/assignments")
    public static Result listAssignment(Request req,
                                        String lectureIdParam) {
        Integer lectureId = Integer.parseInt(lectureIdParam);

        return ViewAction.doActionWithLoginUser(req, () -> Result.Ok.template("assignmentList"));
    }

    // @GET("/lectures/:lectureId/assignments/:assignmentId")
    public static Result viewAssignment(Request req,
                                        String lectureIdParam, String assignmentIdParam) {
        Integer lectureId = Integer.parseInt(lectureIdParam);
        Integer assignmentId = Integer.parseInt(assignmentIdParam);

        return ViewAction.doActionWithLoginUser(req, () -> {
            if(UserService.isProfessorUser(req)) {
                return Result.Ok.template("assignmentView")
                        .withVariable("assignment", AssignmentService.getAssignment(lectureId, assignmentId, UserService.getProfLoginData(req), req.getDBConnection()));
            } else if (UserService.isStudentUser(req)) {
                return Result.Ok.template("assignmentView")
                        .withVariable("assignment", AssignmentService.getAssignment(lectureId, assignmentId, UserService.getStuLoginData(req), req.getDBConnection()))
                        .withVariable("submitted", AssignmentService.isSubmitted(lectureId, assignmentId, UserService.getUserLoginData(req).uid, req.getDBConnection()));
            }
            throw new ForbiddenException("login_required");
        });
    }

    @GET("/lectures/:lectureId/assignments/new")
    public static Result getAssignmentCreate(Request req,
                                             @INP("lectureId") String lectureIdParam) {
        Integer lectureId = Integer.parseInt(lectureIdParam);

        return ViewAction.doActionWithLoginUser(req, () -> Result.Ok.template("assignmentCreate"));
    }
}
