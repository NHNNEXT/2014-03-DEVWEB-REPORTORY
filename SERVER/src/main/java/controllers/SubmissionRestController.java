package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.INP;
import autumn.annotation.POST;
import autumn.header.Header;
import controllers.action.RestAction;
import controllers.models.SubmissionWithAttach;
import controllers.services.AssignmentService;
import controllers.services.SubmissionService;
import controllers.services.UserService;
import models.Assignment;
import models.StudentUser;
import util.ResultResponse;
import util.exceptions.BadRequestException;
import util.exceptions.ForbiddenException;
import util.exceptions.NotFoundException;

import java.sql.Timestamp;

@Controller
public class SubmissionRestController {

    // @GET("/lectures/{lectureId}/assignments/{assignmentId}/submissions")
    public static Result listSubmission(Request req,
                                        String lectureIdParam, String assignmentIdParam) {
        int lectureId = Integer.parseInt(lectureIdParam);
        int assignmentId = Integer.parseInt(lectureIdParam);

        return RestAction.doAction(() -> {
            if (UserService.isProfessorUser(req))
                return Result.Ok.json(SubmissionService.getSubmissions(lectureId, assignmentId, UserService.getProfLoginData(req), req.getDBConnection()));
            else if (UserService.isStudentUser(req))
                return Result.Ok.json(SubmissionService.getSubmissions(lectureId, assignmentId, UserService.getStuLoginData(req), req.getDBConnection()));
            throw new ForbiddenException("permission_denied");
        });
    }

    // @GET("/lectures/{lectureId}/assignments/{assignmentId}/submissions/{submissionId}")
    public static Result viewSubmission(Request req,
                                        String lectureIdParam, String assignmentIdParam, String submissionIdParam) {
        int lectureId = Integer.parseInt(lectureIdParam);
        int assignmentId = Integer.parseInt(lectureIdParam);
        int submissionId = Integer.parseInt(submissionIdParam);

        return RestAction.doAction(() -> {
            if (UserService.isProfessorUser(req))
                return Result.Ok.json(SubmissionService.getSubmission(lectureId, assignmentId, submissionId, UserService.getProfLoginData(req), req.getDBConnection()));
            else if (UserService.isStudentUser(req))
                return Result.Ok.json(SubmissionService.getSubmission(lectureId, assignmentId, submissionId, UserService.getStuLoginData(req), req.getDBConnection()));
            throw new ForbiddenException("permission_denied");
        });
    }

    @POST("/lectures/{lectureId}/assignments/{assignmentId}/submissions")
    public static Result createSubmission(Request req,
                                          @INP("lectureId") String lectureIdParam,
                                          @INP("assignmentId") String assignmentIdParam) {
        int lectureId = Integer.parseInt(lectureIdParam);
        int assignmentId = Integer.parseInt(assignmentIdParam);

        return RestAction.doAction(() -> {
            if (!UserService.isStudentUser(req))
                throw new ForbiddenException("only_students_can_create_submission");

            StudentUser user = UserService.getStuLoginData(req);

            Assignment assignment = null;
            try {
                assignment = AssignmentService.getAssignment(lectureId, assignmentId, user, req.getDBConnection());
            } catch (NotFoundException e) {
                throw new ForbiddenException("permission_denied");
            }

            SubmissionWithAttach submission;
            try {
                submission = req.body().asJson().mapping(SubmissionWithAttach.class);
            } catch (Exception e) {
                throw new BadRequestException("invalid_request");
            }

            submission.uid = user.uid;
            submission.aid = assignmentId;
            submission.create_date = new Timestamp(System.currentTimeMillis());

            Integer generatedSubmissionId = SubmissionService.createSubmission(submission, assignmentId, user.uid, req.getDBConnection());
            return Result.Ok.json(new ResultResponse("Submission created"))
                    .with(new Header(Header.LOCATION, "/lectures/" + lectureId + "/assignments/" + assignmentId + "/submissions/" + generatedSubmissionId));
        });
    }

}
