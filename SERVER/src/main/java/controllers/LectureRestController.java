package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.DELETE;
import autumn.annotation.INP;
import autumn.annotation.POST;
import autumn.header.Header;
import controllers.services.LectureService;
import controllers.services.UserService;
import models.Lecture;
import models.LectureRegistration;
import models.StudentUser;
import util.JsonResult;
import util.exceptions.BadRequestException;
import util.exceptions.InternalServerErrorException;
import util.exceptions.NotFoundException;

import java.sql.SQLException;

@Controller
public class LectureRestController {

    // @GET("/lectures")
    public static Result listLecture(Request req) throws SQLException {
        if (UserService.isProfessorUser(req)) {
            return Result.Ok.json(LectureService.getLecturesByProfessor(UserService.getProfLoginData(req).uid, req.getDBConnection()));
        }
        else if (UserService.isStudentUser(req)){
            return Result.Ok.json(LectureService.getLecturesByQuery(req.getUrlQueryParam("search"), req.getDBConnection()));
        }
        return Result.Forbidden.json(new JsonResult("Login required"));
    }

    // @GET("/lectures/:lectureId")
    public static Result viewLecture(Request req, String lectureId) throws SQLException {
        if (!(UserService.isStudentUser(req) || UserService.isProfessorUser(req)))
            return Result.Forbidden.json(new JsonResult("Permission denied"));

        try {
            return Result.Ok.json(LectureService.getLecture(Integer.parseInt(lectureId), req.getDBConnection()));
        } catch (NotFoundException e) {
            return Result.NotFound.json(new JsonResult(e.getMessage()));
        }
    }

    @POST("/lectures")
    public static Result createLecture(Request req) throws SQLException {
        if (!UserService.isProfessorUser(req))
            return Result.Forbidden.json(new JsonResult("Only professors can create lecture."));

        Lecture lecture;
        try {
            lecture = req.body().asJson().mapping(Lecture.class);

            if (lecture == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            return Result.BadRequest.json(new JsonResult("Invalid request form"));
        }

        lecture.prof = UserService.getProfLoginData(req).uid;

        try {
           Integer generatedLectureId = LectureService.createLecture(lecture, req.getDBConnection());
            return Result.Ok.json(new JsonResult("Lecture created")).
                    with(new Header(Header.LOCATION, "/lectures" + generatedLectureId));
        } catch (InternalServerErrorException e) {
            return Result.InternalServerError.json(new JsonResult(e.getMessage()));
        }
    }

    @DELETE("/lectures/:lectureId")
    public static Result deleteLecture(Request req,
                                       @INP("lectureId") String lectureId) throws SQLException {
        if (!UserService.isProfessorUser(req))
            return Result.Forbidden.json(new JsonResult("Only professors can delete lecture."));

        try {
            LectureService.deleteLecture(Integer.parseInt(lectureId), UserService.getProfLoginData(req).uid, req.getDBConnection());
            return Result.Ok.json(new JsonResult("Lecture deleted"));
        } catch (BadRequestException e) {
            return Result.BadRequest.json(new JsonResult(e.getMessage()));
        }
    }

    @POST("/lectures/:lectureId/join")
    public static Result joinLecture(Request req,
                                     @INP("lectureId") String lectureId) throws SQLException {
        if (!UserService.isStudentUser(req)) {
            return Result.Forbidden.json(new JsonResult("Only students can join lecture."));
        }

        LectureRegistration lectureRegistration;
        try {
            lectureRegistration = req.body().asJson().mapping(LectureRegistration.class);

            if (lectureRegistration == null) {
                throw new Exception();
            }
        } catch (Exception e){
            return Result.BadRequest.json(new JsonResult("Invalid request form"));
        }

        lectureRegistration.lid = Integer.parseInt(lectureId);
        lectureRegistration.accepted = false;

        StudentUser stu = UserService.getStuLoginData(req);

        if (lectureRegistration.major == null) {
            lectureRegistration.major = stu.defMajor;
        }
        if (lectureRegistration.identity == null) {
            lectureRegistration.identity = stu.defIdentity;
        }

        try {
            LectureService.joinLecture(lectureRegistration, req.getDBConnection());
            return Result.Ok.json(new JsonResult("Successfully joined"));
        } catch (BadRequestException e) {
            return Result.BadRequest.json(new JsonResult(e.getMessage()));
        }
    }

    @POST("/lectures/:lectureId/leave")
    public static Result leaveLecture(Request req,
                                     @INP("lectureId") String lectureId) throws SQLException {
        if (!UserService.isStudentUser(req)) {
            return Result.Forbidden.json(new JsonResult("Only students can leave lecture."));
        }

        try {
            LectureService.leaveLecture(Integer.parseInt(lectureId), UserService.getStuLoginData(req).uid, req.getDBConnection());
            return Result.Ok.json(new JsonResult("Successfully leaved"));
        } catch (BadRequestException e) {
            return Result.BadRequest.json(new JsonResult(e.getMessage()));
        }

    }
}
