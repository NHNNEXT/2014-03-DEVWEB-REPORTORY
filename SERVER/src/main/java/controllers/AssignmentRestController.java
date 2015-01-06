package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.INP;
import autumn.annotation.POST;
import autumn.header.Header;
import controllers.action.RestAction;
import controllers.models.AssignmentWithAttach;
import controllers.services.AssignmentService;
import controllers.services.UserService;
import models.ProfessorUser;
import util.ResultResponse;
import util.exceptions.BadRequestException;
import util.exceptions.ForbiddenException;

@Controller
public class AssignmentRestController {

    // @GET("/lectures/:lectureId/assignments")
    public static Result listAssignment(Request req,
                                        String lectureIdParam) {
        Integer lectureId = Integer.parseInt(lectureIdParam);

        return RestAction.doAction(() -> {
            if (UserService.isProfessorUser(req)) {
                return Result.Ok.json(AssignmentService.getAssignments(lectureId, UserService.getProfLoginData(req), req.getDBConnection()));
            } else if (UserService.isStudentUser(req)) {
                return Result.Ok.json(AssignmentService.getAssignments(lectureId, UserService.getStuLoginData(req), req.getDBConnection()));
            }
            throw new ForbiddenException("login_required");
        });
    }

    // @GET("/lectures/:lectureId/assignments/:assignmentId")
    public static Result viewAssignment(Request req,
                                        String lectureIdParam, String assignmentIdParam) {
        Integer lectureId = Integer.parseInt(lectureIdParam);
        Integer assignmentId = Integer.parseInt(assignmentIdParam);

        return RestAction.doAction(() -> {
            if (UserService.isProfessorUser(req)) {
                return Result.Ok.json(AssignmentService.getAssignment(lectureId, assignmentId, UserService.getProfLoginData(req), req.getDBConnection()));
            } else if (UserService.isStudentUser(req)) {
                return Result.Ok.json(AssignmentService.getAssignment(lectureId, assignmentId, UserService.getStuLoginData(req), req.getDBConnection()));
            }
            throw new ForbiddenException("login_required");
        });
    }

    @POST("/lectures/:lectureId/assignments")
    public static Result createAssignment(Request req,
                                          @INP("lectureId") String lectureIdParam) {
        Integer lectureId = Integer.parseInt(lectureIdParam);

        return RestAction.doAction(() -> {
            if (!UserService.isProfessorUser(req)) {
                throw new ForbiddenException("only_professors_can_create_assignment");
            }

            ProfessorUser user = UserService.getProfLoginData(req);

            AssignmentWithAttach assignment;
            try {
                assignment = req.body().asJson().mapping(AssignmentWithAttach.class);
            } catch (Exception e) {
                throw new BadRequestException("invalid_request");
            }

            assignment.lid = lectureId;

            Integer generatedAssignmentId = AssignmentService.createAssignment(assignment, user.uid, req.getDBConnection());
            return Result.Ok.json(new ResultResponse("Assignment created"))
                    .with(new Header(Header.LOCATION, "/lectures/" + lectureId + "/assignments/" + generatedAssignmentId));
        });
    }

}
