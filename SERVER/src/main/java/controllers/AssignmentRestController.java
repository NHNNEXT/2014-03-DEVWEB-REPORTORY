package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.GET;
import autumn.annotation.INP;
import autumn.annotation.POST;
import autumn.database.AbstractQuery;
import autumn.database.jdbc.DBConnection;
import controllers.services.UserService;
import models.Assignment;
import models.AssignmentAttachment;
import models.ProfessorUser;
import models.StudentUser;
import models.tables.AssignmentAttachmentTable;
import models.tables.AssignmentTable;
import models.tables.joined.LectureAssignmentJoin;
import models.tables.joined.LectureRegistrationAssignmentJoin;

import java.sql.SQLException;
import java.util.List;

@Controller
public class AssignmentRestController {

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

    @POST("/lectures/{lid}/assignments")
    public static Result createAssignment(@INP("lid")String lidStr, Request req) throws SQLException {
        if(!UserService.isProfessorUser(req))
            return Result.Forbidden.plainText("only prof can create assignment");

        ProfessorUser user = UserService.getProfLoginData(req);

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

    @GET("/lectures/{lid}/assignments")
    public static Result listAssignment(@INP("lid")String lidStr, Request req) throws SQLException {
        int lid = Integer.parseInt(lidStr);
        if(UserService.isProfessorUser(req))
            return getProfessorAssignment(req,lid);
        else if (UserService.isStudentUser(req))
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
        ProfessorUser prof = UserService.getProfLoginData(req);
        List<Assignment> assignments =getProfAssignQuery(prof, lid).list(req.getDBConnection());
        return Result.Ok.json(assignments);
    }

    private static Result getStudentAssignment(Request req, int lid) throws SQLException {
        StudentUser stu = UserService.getStuLoginData(req);
        List<Assignment> assignments = getStuAssignQuery(stu,lid).list(req.getDBConnection());
        return Result.Ok.json(assignments);
    }

    @GET("/lectures/{lid}/assignments/{aid}")
    public static Result viewAssignment(@INP("lid")String lidStr, @INP("aid")String aidStr, Request req) throws SQLException {
        int lid = Integer.parseInt(lidStr);
        int aid = Integer.parseInt(aidStr);
        if(UserService.isProfessorUser(req))
            return getFristProfessorAssignment(req,lid,aid);
        else if (UserService.isStudentUser(req))
            return getFirstStudentAssignment(req,lid,aid);
        return Result.Forbidden.plainText("only user can request it.");
    }

    private static Result getFristProfessorAssignment(Request req, int lid, int aid) throws SQLException {
        ProfessorUser prof = UserService.getProfLoginData(req);
        Assignment assign =getProfAssignQuery(prof, lid).where((t)->
                (t.aid) .isEqualTo (aid)).first(req.getDBConnection());
        if(assign==null)
            return Result.Forbidden.plainText("not found");

        return Result.Ok.json(addAttachments(req,assign));
    }

    private static Result getFirstStudentAssignment(Request req, int lid, int aid) throws SQLException {
        StudentUser stu = UserService.getStuLoginData(req);

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
        assignment.attachments = (String[]) attches.stream().map((a) -> a.hashcode).toArray();
        return assignment;
    }
}
