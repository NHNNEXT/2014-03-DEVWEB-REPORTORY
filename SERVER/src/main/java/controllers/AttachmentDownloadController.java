package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.GET;
import autumn.annotation.INP;
import autumn.database.jdbc.DBConnection;
import controllers.services.UserService;
import models.*;
import models.tables.AttachmentsTable;
import models.tables.joined.AttachmentLectureJoin;
import models.tables.joined.AttachmentRecentSubmissionRegistrationJoin;
import models.tables.joined.AttachmentRegistrationJoin;
import util.attachment.AttachmentZipGenerator;
import util.exceptions.ForbiddenException;
import util.exceptions.NotFoundException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by infinitu on 15. 1. 6..
 */
@Controller
public class AttachmentDownloadController {
    
    @GET("/attachment/{attId}")
    public static Result getAttachment(Request req, @INP("attId")String attId) throws SQLException, ForbiddenException, NotFoundException {
        User user = UserService.getUserLoginData(req);
        if(user == null)
            throw new ForbiddenException("permission denied");

        Attachment att = AttachmentsTable.getQuery().where((t)->
                        (t.owner) .isEqualTo (user.uid) .and(
                                (t.hashcode_id) .isEqualTo (attId)
                        ))
                .first(req.getDBConnection());
        
        if(att ==null) {
            if (UserService.isProfessorUser(req))
                return getStudentSubmission(req.getDBConnection(),attId, user.uid);
            else
                return getProfessorAssignment(req.getDBConnection(),attId, user.uid);
                
        }
        return Result.Ok.sendFile(att.directory).withFileName(att.filename);
    }

    private static Result getStudentSubmission(DBConnection dbConnection, String attId, int uid) throws SQLException, NotFoundException {
        AttachmentLecture att = 
                AttachmentLectureJoin.getQuery().where((t)->
                        (t.prof) .isEqualTo (uid) .and(
                                (t.hashcode_id) .isEqualTo (attId)))
                .first(dbConnection);
        
        if(att == null)
            throw new NotFoundException("no such attachments");
        
        return Result.Ok.sendFile(att.directory).withFileName(att.filename);
    }

    private static Result getProfessorAssignment(DBConnection dbConnection, String attId, int uid) throws SQLException, ForbiddenException {

        AttachmentRegistration att =
                AttachmentRegistrationJoin.getQuery().where((t)->
                        (t.hashcode_id) .isEqualTo(attId).and(
                                (t.listener) .isEqualTo (uid) .and(
                                        (t.accepted) .isEqualTo (true))))
                .first(dbConnection);

        if(att == null)
            throw new ForbiddenException("permission_denied");
        
        return Result.Ok.sendFile(att.directory).withFileName(att.filename);
    }
    
    protected static Result makeZip(DBConnection dbConnection, int assignmentId, int profId) throws SQLException, IOException {
        List<RegistrationAndAttachment> list =
                AttachmentRecentSubmissionRegistrationJoin.getQuery().where((t)->
                                (t.aid).isEqualTo(assignmentId).and(
                                        (t.left.left.left.left.left.left.prof) . isEqualTo (profId)))
                .list(dbConnection);
        AttachmentZipGenerator.ZipOutputHolder holder = AttachmentZipGenerator.createZip();
        for(RegistrationAndAttachment att : list){
            holder.add(att.directory,att.identity+"_"+att.stu_name+"/"+att.filename);
        }
        return Result.Ok.sendFile(holder.getZip()).withFileName("submissions.zip");
    }

}
