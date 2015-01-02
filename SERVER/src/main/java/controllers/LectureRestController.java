package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.*;
import autumn.result.JsonResult;
import com.google.gson.JsonElement;
import controllers.services.UserService;
import models.*;
import models.tables.LectureRegistrationTable;
import models.tables.LectureTable;
import models.tables.joined.LectureProfessorJoin;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by infinitu on 14. 12. 26..
 */

@Controller
public class LectureRestController {

    // @GET("/lectures")
    public static Result listLecture(Request req) throws SQLException {
        String query = req.getUrlQueryParam("search");
        if(UserService.isPrefessorUser(req))
            return professorOwnLectures(req);
        else if(UserService.isStudentUser(req)){
            return findLecture(req,query);
        }
        return Result.Forbidden.plainText("login required request.");
    }

    private static Result professorOwnLectures(Request req) throws SQLException {
        ProfessorUser prof = UserService.getProfLoginData(req);
        List<Lecture> lectureList =
                LectureTable.getQuery()
                        .where((t) ->
                                (t.prof).isEqualTo(prof.uid))
                        .list(req.getDBConnection());

        return Result.Ok.json(lectureList);
    }

    private static Result findLecture(Request req, String query) throws SQLException {
        List<LectureAndProfessor> list = LectureProfessorJoin.getQuery().where((t)->(t.lecturename).isLike(query)).list(req.getDBConnection());
        return Result.Ok.json(list);
    }


    // @GET("/lectures/:lectureId")
    public static Result viewLecture(Request req,
                                     @INP("lectureId") String lectureId) throws SQLException {
        if(!(UserService.isStudentUser(req)|| UserService.isPrefessorUser(req)))
            return Result.Forbidden.plainText("Student and Professor can exit a lecture.");
        LectureAndProfessor lec = LectureProfessorJoin.getQuery().where((t) -> (t.lid).isEqualTo(Integer.parseInt(lectureId))).first(req.getDBConnection());
        if(lec == null)
            return Result.NotFound.plainText("no such lecture");
        return Result.Ok.json(lec);
    }


    @POST("/lectures")
    public static Result createLecture(Request req) throws SQLException {

        if(!UserService.isPrefessorUser(req))
            return Result.Forbidden.plainText("Lecture should be created by only professor account.");

        Lecture input;
        try {
            input = req.body().asJson().mapping(Lecture.class);
        } catch(Exception e){
            return Result.BadRequest.plainText("not allowed Create Form");
        }
        if(input == null)
            return Result.BadRequest.plainText("not allowed Create Form");

        ProfessorUser prof = UserService.getProfLoginData(req);
        input.prof = prof.uid;
        Integer idx = LectureTable.getQuery().insertReturningGenKey(req.getDBConnection(), input);
        if(idx == null)
            return Result.InternalServerError.plainText("failed");

        return Result.Ok.plainText("success " + idx);
    }


    @DELETE("/lectures/:lectureId")
    public static Result createLecture(Request req,
                                       @INP("lectureId") String lectureId) throws SQLException {

        if (!UserService.isPrefessorUser(req))
            return Result.Forbidden.plainText("Lecture should be delete by only professor account.");

        int profuid = UserService.getProfLoginData(req).uid;
        int delline = LectureTable.getQuery()
                .where((t) -> (t.lid).isEqualTo(Integer.parseInt(lectureId)).and(
                        (t.prof).isEqualTo(profuid)))
                .delete(req.getDBConnection());
        if(delline<1)
            Result.BadRequest.plainText("no such lecture");
        return Result.Ok.plainText("successfully");
    }


    @POST("/lectures/:lectureId/join")
    public static Result joinLecture(Request req,
                                     @INP("lectureId") String lectureId) throws SQLException {
        if(!UserService.isStudentUser(req))
            return Result.Forbidden.plainText("Only Student can join a lecture.");

        LectureRegistration input;
        try {
            input = req.body().asJson().mapping(LectureRegistration.class);
        } catch(Exception e){
            return Result.BadRequest.plainText("not allowed Join Form");
        }

        input.lid = Integer.parseInt(lectureId);
        input.accepted = false;

        StudentUser stu = UserService.getStuLoginData(req);
        if(input.major == null)input.major=stu.defMajor;
        if(input.identity == null)input.identity = stu.defIdentity;

        int line = LectureTable.getQuery().insert(req.getDBConnection(),input);
        if(line<1)
            return Result.BadRequest.plainText("invalid lecture");
        return Result.Ok.plainText("successful");
    }


    @POST("/lectures/:lectureId/exit")
    public static Result exitLecture(Request req,
                                     @INP("lectureId") String lectureId) throws SQLException {
        if(!UserService.isStudentUser(req))
            return Result.Forbidden.plainText("Only Student can exit a lecture.");

        int lid = Integer.parseInt(lectureId);

        StudentUser stu = UserService.getStuLoginData(req);

        int line =
                LectureRegistrationTable.getQuery()
                        .where((t) ->
                                (t.lid).isEqualTo(lid).and(
                                        (t.uid).isEqualTo(stu.uid)))
                        .delete(req.getDBConnection());
        if(line<1)
            return Result.BadRequest.plainText("invalid registration");
        return Result.Ok.plainText("successful");
    }
}
