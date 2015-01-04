package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.GET;
import autumn.annotation.INP;
import autumn.annotation.POST;
import autumn.database.jdbc.DBConnection;
import controllers.services.UserService;
import models.Assignment;
import models.Submission;
import models.SubmissionAttachment;
import models.User;
import models.tables.AssignmentAttachmentTable;
import models.tables.SubmissionAttachmentsTable;
import models.tables.SubmissionTable;
import models.tables.joined.RecentSubmissionRegistrationJoin;
import models.tables.joined.SubmissionLectureJoin;
import models.tables.joined.SubmissionRegistrationJoin;
import util.exceptions.ForbiddenException;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by infinitu on 15. 1. 4..
 */
public class SubmissionRestController {

    public static class SubmissionWithAttach extends Submission {
        public SubmissionWithAttach() {
        }

        public SubmissionWithAttach(Submission submission) {
            this.sid         = submission.sid         ;
            this.uid         = submission.uid         ;
            this.aid         = submission.aid         ;
            this.description = submission.description ;
            this.create_date = submission.create_date ;
        }
        String[] previous;
        String[] attachments;
    }
    @POST("/{lid}/{aid}/submission") //only student
    public static Result postSubmission(Request req, @INP("lid") String lidStr,@INP("aid") String aidStr)
            throws ForbiddenException, SQLException {
        
        if(!UserService.isStudentUser(req))
            throw new ForbiddenException("permission_denied");
        
        User user = UserService.getUserLoginData(req);
        int lid = Integer.parseInt(lidStr);
        int aid = Integer.parseInt(aidStr);
        Assignment assignment = AssignmentRestController.getFirstStudentAssignment(req, lid, aid);
        
        if(assignment == null)
            throw new ForbiddenException("assignment_permission_denied");

        SubmissionWithAttach submission = req.body().asJson().mapping(SubmissionWithAttach.class);
        submission.uid = user.uid;
        submission.aid = aid;
        submission.create_date = new Timestamp(System.currentTimeMillis());

        DBConnection db = req.getDBConnection();
        
        db.transaction();
        
        Integer sid = SubmissionTable.getQuery().insertReturningGenKey(db,submission);
        
        if(sid == null)
            return Result.BadRequest.plainText("bad submission.");
        
        if(submission.attachments!=null && submission.attachments.length>0) {
            SubmissionAttachment[] atts = new SubmissionAttachment[submission.attachments.length];
            for (int i = 0; i < atts.length; i++) {
                atts[i] = new SubmissionAttachment();
                atts[i].aid = aid;
                atts[i].hashcode_id = submission.attachments[i];
                atts[i].sid = sid;
                atts[i].owner = user.uid;

            }

            int rows = AssignmentAttachmentTable.getQuery().insert(db, atts);
            if (rows < atts.length) {
                db.rollBack();
                return Result.BadRequest.plainText("bad attachments.");
            }
        }
        db.commit();
        return Result.Ok.plainText("successfully");
    }
    
    @GET("/{lid}/{aid}/submission") //only professor
    public static Result getSubmissions(Request req, @INP("lid") String lidStr,@INP("aid") String aidStr) throws ForbiddenException, SQLException {
        
        if(!UserService.isProfessorUser(req))
            throw new ForbiddenException("permission denied");
        
        int lid = Integer.parseInt(lidStr);
        int aid = Integer.parseInt(aidStr);
        User user = UserService.getUserLoginData(req);

        List<Submission> submissions =
                RecentSubmissionRegistrationJoin.getQuery().where((t) ->
                        (t.left.lid).isEqualTo(lid).and(
                                (t.left.aid).isEqualTo(aid).and(
                                        (t.left.right.left.prof).isEqualTo(user.uid))))
                    .list(req.getDBConnection());
        
        if(submissions == null || submissions.size() ==0){
            throw new ForbiddenException("no such submissions.");
        }
        
        return Result.Ok.json(submissions);
    }

    @GET("/{lid}/{aid}/{sid}") //professor and owner.
    public static Result viewSubmission(Request req, @INP("lid") String lidStr,@INP("aid") String aidStr, @INP("sid") String sidStr) throws ForbiddenException, SQLException {
        int lid = Integer.parseInt(lidStr);
        int aid = Integer.parseInt(aidStr);
        int sid = Integer.parseInt(sidStr);
        if(UserService.isProfessorUser(req))
            return profViewSubmission(req,lid,aid,sid);
        else if(UserService.isStudentUser(req))
            return stuViewSubmission(req,lid,aid,sid);
        throw new ForbiddenException("permission denied");
    }

    private static Result stuViewSubmission(Request req, int lid, int aid, int sid) throws SQLException, ForbiddenException {
        User user = UserService.getUserLoginData(req);

        Submission submission =
                SubmissionRegistrationJoin.getQuery()
                .where((t) ->
                        (t.left.lid).isEqualTo(lid).and(
                                (t.aid).isEqualTo(aid).and(
                                        (t.sid).isEqualTo(sid) .and(
                                                (t.uid) .isEqualTo (user.uid)))))
                .first(req.getDBConnection());
        
        if(submission == null)
            throw new ForbiddenException("permission denied");
        
        return Result.Ok.json(addAttachment(req,aid,sid,user.uid,submission));
    }
    
    private static Result profViewSubmission(Request req, int lid, int aid, int sid) throws SQLException, ForbiddenException {
        User user = UserService.getUserLoginData(req);

        Submission submission =
                SubmissionLectureJoin.getQuery()
                        .where((t) ->
                                (t.left.lid).isEqualTo(lid).and(
                                        (t.aid).isEqualTo(aid).and(
                                                (t.sid).isEqualTo(sid) .and(
                                                        (t.left.left.prof) .isEqualTo (user.uid)))))
                        .first(req.getDBConnection());

        if(submission == null)
            throw new ForbiddenException("permission denied");

        return Result.Ok.json(addAttachment(req,aid,sid,user.uid,submission));
    }
    
    private static SubmissionWithAttach addAttachment(Request req, int aid, int sid, int uid, Submission submission) throws SQLException {
        SubmissionWithAttach submissionWithAttach = new SubmissionWithAttach(submission);

        List<SubmissionAttachment> atts=
                SubmissionAttachmentsTable.getQuery().where((t) ->
                        t.aid.isEqualTo(aid).and(
                                t.owner.isEqualTo(uid).and(
                                        t.sid.isEqualTo(sid))))
                        .list(req.getDBConnection());
        if(atts != null)
            submissionWithAttach.attachments = (String[]) atts.stream().map((t)->t.hashcode_id).toArray();

        return submissionWithAttach;
    }


    @GET("/{lid}/{aid}/submission") //only student.
    public static Result viewAllSubmission(Request req, @INP("lid") String lidStr,@INP("aid") String aidStr) throws ForbiddenException, SQLException {
        int lid = Integer.parseInt(lidStr);
        int aid = Integer.parseInt(aidStr);
        if(!UserService.isStudentUser(req))
            throw new ForbiddenException("permission denied");
        User user = UserService.getUserLoginData(req);

        List<Submission> submissions =
                SubmissionLectureJoin.getQuery()
                        .where((t) ->
                                (t.left.lid).isEqualTo(lid).and(
                                        (t.aid).isEqualTo(aid).and(
                                                    (t.left.left.prof) .isEqualTo (user.uid))))
                        .list(req.getDBConnection());

        return Result.Ok.json(submissions);
    }


}
