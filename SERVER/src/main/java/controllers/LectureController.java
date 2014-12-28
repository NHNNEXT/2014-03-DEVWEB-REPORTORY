package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.*;
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
public class LectureController {


    @POST("/lectures/create")
    public static Result createLecture(Request req) throws SQLException {

        if(!UserController.isPrefessorUser(req))
            return Result.Forbidden.plainText("Lecture should be created by only professor account.");

        Lecture input;
        try {
            input = req.body().asJson().mapping(Lecture.class);
        } catch(Exception e){
            return Result.BadRequest.plainText("not allowed Create Form");
        }
        if(input == null)
            return Result.BadRequest.plainText("not allowed Create Form");


        ProfessorUser prof = UserController.getProfLoginData(req);
        input.prof = prof.uid;
        Integer idx = LectureTable.getQuery().insertRetunningGenKey(req.getDBConnection(), input);
        if(idx == null)
            return Result.InternalServerError.plainText("failed");

        return Result.Ok.plainText("success " + idx);
    }

    @POST("/lectures/{id}/join")
    public static Result joinLecture(@INP("id")String id, Request req) throws SQLException {
        if(!UserController.isStudentUser(req))
            return Result.Forbidden.plainText("Only Student can join a lecture.");

        LectureRegistration input;
        try {
            input = req.body().asJson().mapping(LectureRegistration.class);
        } catch(Exception e){
            return Result.BadRequest.plainText("not allowed Join Form");
        }

        input.lid = Integer.parseInt(id);
        input.accepted = false;

        StudentUser stu = UserController.getStuLoginData(req);
        if(input.major == null)input.major=stu.defMajor;
        if(input.identity == null)input.identity = stu.defIdentity;

        int line = LectureTable.getQuery().insert(req.getDBConnection(),input);
        if(line<1)
            return Result.BadRequest.plainText("invalid lecture");
        return Result.Ok.plainText("successful");
    }

    @POST("/lectures/{id}/exit")
    public static Result exitLecture(@INP("id")String id, Request req) throws SQLException {
        if(!UserController.isStudentUser(req))
            return Result.Forbidden.plainText("Only Student can exit a lecture.");

        int lid = Integer.parseInt(id);

        StudentUser stu = UserController.getStuLoginData(req);

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

    @GET("/lectures")
    public static Result getLectureData(Request req) throws SQLException {
        String query = req.getUrlQueryParam("search");
        if(UserController.isPrefessorUser(req))
            return professorOwnLectures(req);
        else if(UserController.isStudentUser(req)){
            return findLecture(req,query);
        }
        return Result.Forbidden.plainText("login required request.");
    }

    private static Result professorOwnLectures(Request req) throws SQLException {
        ProfessorUser prof = UserController.getProfLoginData(req);
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


    @GET("/lectures/{id}")
    public static Result getLecture(Request req,@INP("id") String id) throws SQLException {
        if(!(UserController.isStudentUser(req)||UserController.isPrefessorUser(req)))
            return Result.Forbidden.plainText("Studenet and Professor can exit a lecture.");
        LectureAndProfessor lec = LectureProfessorJoin.getQuery().where((t) -> (t.lid).isEqualTo(Integer.parseInt(id))).first(req.getDBConnection());
        if(lec == null)
            return Result.NotFound.plainText("no such lecture");
        return Result.Ok.json(lec);
    }

    @DELETE("/lectures/{id}")
    public static Result createLecture(Request req,@INP("id") String id) throws SQLException {

        if (!UserController.isPrefessorUser(req))
            return Result.Forbidden.plainText("Lecture should be delete by only professor account.");

        int profuid = UserController.getProfLoginData(req).uid;
        int delline = LectureTable.getQuery()
                .where((t) -> (t.lid).isEqualTo(Integer.parseInt(id)).and(
                        (t.prof).isEqualTo(profuid)))
                .delete(req.getDBConnection());
        if(delline<1)
            Result.BadRequest.plainText("no such lecture");
        return Result.Ok.plainText("successfully");
    }



}
