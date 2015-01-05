package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.DELETE;
import autumn.annotation.INP;
import autumn.annotation.POST;
import autumn.header.Header;
import controllers.action.RestAction;
import controllers.services.LectureService;
import controllers.services.UserService;
import models.Lecture;
import models.LectureRegistration;
import models.StudentUser;
import util.ResultResponse;
import util.exceptions.BadRequestException;
import util.exceptions.ForbiddenException;

@Controller
public class LectureRestController {

    // @GET("/lectures")
    public static Result listLecture(Request req) {
        return RestAction.doAction(() -> {
            String query = req.getUrlQueryParam("search");

            if (UserService.isProfessorUser(req)) {
                return Result.Ok.json(LectureService.getLecturesByProfessor(UserService.getUserLoginData(req).uid, req.getDBConnection()));
            } else if (UserService.isStudentUser(req)) {
                if (query != null) {
                    return Result.Ok.json(LectureService.getLectures(query, req.getDBConnection()));
                }
                return Result.Ok.json(LectureService.getLectures(UserService.getUserLoginData(req).uid, req.getDBConnection()));
            }
            throw new ForbiddenException("login_required");
        });
    }

    // @GET("/lectures/:lectureId")
    public static Result viewLecture(Request req, String lectureIdParam) {
        Integer lectureId = Integer.parseInt(lectureIdParam);

        return RestAction.doAction(() -> {
            if (!(UserService.isStudentUser(req) || UserService.isProfessorUser(req)))
                throw new ForbiddenException("permission_denied");
            return Result.Ok.json(LectureService.getLecture(lectureId, req.getDBConnection()));
        });
    }

    @POST("/lectures")
    public static Result createLecture(Request req) {
        return RestAction.doAction(() -> {
            if (!UserService.isProfessorUser(req))
                throw new ForbiddenException("only_professors_can_create_lecture");

            Lecture lecture;
            try {
                lecture = req.body().asJson().mapping(Lecture.class);
            } catch (Exception e) {
                throw new BadRequestException("invalid_request");
            }

            lecture.prof = UserService.getProfLoginData(req).uid;
            System.out.println(lecture.prof);

            Integer generatedLectureId = LectureService.createLecture(lecture, req.getDBConnection());
            return Result.Ok.json(new ResultResponse("Lecture created"))
                    .with(new Header(Header.LOCATION, "/lectures/" + generatedLectureId));
        });
    }

    @DELETE("/lectures/:lectureId")
    public static Result deleteLecture(Request req,
                                       @INP("lectureId") String lectureIdParam) {
        Integer lectureId = Integer.parseInt(lectureIdParam);

        return RestAction.doAction(() -> {
            if (!UserService.isProfessorUser(req)) {
                throw new ForbiddenException("only_professors_can_delete_lecture");
            }

            LectureService.deleteLecture(lectureId, UserService.getProfLoginData(req).uid, req.getDBConnection());
            return Result.Ok.json(new ResultResponse("Lecture deleted"));
        });
    }

    @POST("/lectures/:lectureId/join")
    public static Result joinLecture(Request req,
                                     @INP("lectureId") String lectureIdParam) {
        Integer lectureId = Integer.parseInt(lectureIdParam);

        return RestAction.doAction(() -> {
            if (!UserService.isStudentUser(req)) {
                throw new ForbiddenException("only_professors_can_join_lecture");
            }

            LectureRegistration lectureRegistration;
            try {
                lectureRegistration = req.body().asJson().mapping(LectureRegistration.class);
            } catch (Exception e){
                throw new BadRequestException("invalid_request");
            }

            lectureRegistration.lid = lectureId;
            lectureRegistration.accepted = true; // TODO

            StudentUser stu = UserService.getStuLoginData(req);
            lectureRegistration.uid = stu.uid;

            if (lectureRegistration.major == null) {
                lectureRegistration.major = stu.defMajor;
            }
            if (lectureRegistration.identity == null) {
                lectureRegistration.identity = stu.defIdentity;
            }

            LectureService.joinLecture(lectureRegistration, req.getDBConnection());
            return Result.Ok.json(new ResultResponse("Successfully joined"));
        });
    }

    @POST("/lectures/:lectureId/leave")
    public static Result leaveLecture(Request req,
                                     @INP("lectureId") String lectureIdParam) {
        Integer lectureId = Integer.parseInt(lectureIdParam);

        return RestAction.doAction(() -> {
            if (!UserService.isStudentUser(req)) {
                throw new ForbiddenException("only_professors_can_leave_lecture");
            }

            LectureService.leaveLecture(lectureId, UserService.getStuLoginData(req).uid, req.getDBConnection());
            return Result.Ok.json(new ResultResponse("Successfully leaved"));
        });
    }

}
