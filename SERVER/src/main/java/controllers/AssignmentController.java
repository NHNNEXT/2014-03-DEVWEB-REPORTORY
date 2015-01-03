package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.GET;
import autumn.annotation.INP;
import autumn.annotation.POST;
import autumn.database.AbstractQuery;
import autumn.database.JoinQuery;
import autumn.database.jdbc.DBConnection;
import models.*;
import models.tables.AssignmentAttachmentTable;
import models.tables.AssignmentTable;
import models.tables.AttachmentsTable;
import models.tables.joined.LectureAssignmentJoin;
import models.tables.joined.LectureRegistrationAssignmentJoin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by infinitu on 14. 12. 26..
 */

@Controller
public class AssignmentController {
    
    public static class AssignmentWithAttach extends Assignment{
        public AssignmentWithAttach() {
        }

        public AssignmentWithAttach(Assignment assignment) {
            this.aid = assignment.aid;
            this.lid = assignment.lid;
            this.createTime = assignment.createTime;
            this.dueTime = assignment.dueTime;
            this.title = assignment.title;
            this.text = assignment.text;
        }

        String[] attachments;
    }
    
    @POST("/{lid}/assignments")
    public static Result createAssignments(@INP("lid")String lidStr, Request req) throws SQLException {
        if(!UserController.isPrefessorUser(req))
            return Result.Forbidden.plainText("only prof can create assignment");

        ProfessorUser user = UserController.getProfLoginData(req);

        AssignmentWithAttach assign = req.body().asJson().mapping(AssignmentWithAttach.class);
        assign.lid = Integer.parseInt(lidStr);

        DBConnection db = req.getDBConnection();
        db.transaction();
        
        Integer aid = AssignmentTable.getQuery().insertReturningGenKey(db, assign);
        if(aid == null)
            return Result.Forbidden.plainText("no such lectures");
        if(assign.attachments!=null && assign.attachments.length>0) {
            AssignmentAttachment[] atts = new AssignmentAttachment[assign.attachments.length];
            for (int i = 0; i < atts.length; i++) {
                atts[i] = new AssignmentAttachment();
                atts[i].aid = aid;
                atts[i].hashcode = assign.attachments[i];
                atts[i].owner = user.uid;
            }
            
            int rows = AssignmentAttachmentTable.getQuery().insert(db, atts);
            if (rows < atts.length) {
                db.rollBack();
                return Result.BadRequest.plainText("bad attachments.");
            }
        }
        return Result.Ok.plainText("successfully");
    }
    
    @GET("/{lid}/assignments")
    public static Result assignmentList(@INP("lid")String lidStr, Request req) throws SQLException {
        int lid = Integer.parseInt(lidStr);
        if(UserController.isPrefessorUser(req))
            return getProfessorAssignment(req,lid);
        else if (UserController.isStudentUser(req))
            return getStudentAssignment(req,lid);
        return Result.Forbidden.plainText("only user can request it.");
    }
    
    private static AbstractQuery<LectureAssignmentJoin> getProfAssignQuery(ProfessorUser prof, int lid){
        return LectureAssignmentJoin.getQuery().where((t)->
                (t.left.prof) .isEqualTo (prof.uid) .and(
                        (t.lid) .isEqualTo (lid)
                ));
    }
    
    private static AbstractQuery<LectureRegistrationAssignmentJoin> getStuAssignQuery(StudentUser stu, int lid){
        return LectureRegistrationAssignmentJoin.getQuery().where((t)->
                (t.left.uid).isEqualTo(stu.uid) .and(
                        (t.left.lid) .isEqualTo (lid)
                ));
    }

    private static Result getProfessorAssignment(Request req, int lid) throws SQLException {
        ProfessorUser prof = UserController.getProfLoginData(req);
        List<Assignment> assignments =getProfAssignQuery(prof, lid).list(req.getDBConnection());
        return Result.Ok.json(assignments);
    }

    private static Result getStudentAssignment(Request req, int lid) throws SQLException {
        StudentUser stu = UserController.getStuLoginData(req);
        List<Assignment> assignments = getStuAssignQuery(stu,lid).list(req.getDBConnection());
        return Result.Ok.json(assignments);
    }

    @GET("/{lid}/{aid}")
    public static Result assignmentInfo(@INP("lid")String lidStr, @INP("aid")String aidStr, Request req) throws SQLException {
        int lid = Integer.parseInt(lidStr);
        int aid = Integer.parseInt(aidStr);
        if(UserController.isPrefessorUser(req))
            return getFristProfessorAssignment(req,lid,aid);
        else if (UserController.isStudentUser(req))
            return getFirstStudentAssignment(req,lid,aid);
        return Result.Forbidden.plainText("only user can request it.");
    }



    private static Result getFristProfessorAssignment(Request req, int lid, int aid) throws SQLException {
        ProfessorUser prof = UserController.getProfLoginData(req);
        Assignment assign =getProfAssignQuery(prof, lid).where((t)->
                (t.aid) .isEqualTo (aid)).first(req.getDBConnection());
        if(assign==null)
            return Result.Forbidden.plainText("not found");
        
        return Result.Ok.json(addAttachments(req,assign));
    }

    private static Result getFirstStudentAssignment(Request req, int lid, int aid) throws SQLException {
        StudentUser stu = UserController.getStuLoginData(req);
        
        Assignment assign =getStuAssignQuery(stu, lid).where((t)->
                (t.aid) .isEqualTo (aid)).first(req.getDBConnection());
        if(assign==null)
            return Result.Forbidden.plainText("not found");

        return Result.Ok.json(addAttachments(req,assign));
    }
    
    private static AssignmentWithAttach addAttachments(Request req, Assignment assign) throws SQLException {
        AssignmentWithAttach assignment = new AssignmentWithAttach(assign);
        List<AssignmentAttachment> attches =
                AssignmentAttachmentTable.getQuery().where((t) ->
                        (t.aid).isEqualTo(assign.aid))
                        .list(req.getDBConnection());
        assignment.attachments = (String[]) attches.stream().map((a)->a.hashcode).toArray();
        return assignment;
    }

}
