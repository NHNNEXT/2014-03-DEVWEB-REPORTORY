package controllers.services;

import autumn.database.jdbc.DBConnection;
import controllers.models.SubmissionWithAttach;
import models.ProfessorUser;
import models.StudentUser;
import models.Submission;
import models.SubmissionAttachment;
import models.tables.AssignmentAttachmentTable;
import models.tables.SubmissionAttachmentsTable;
import models.tables.SubmissionTable;
import models.tables.joined.RecentSubmissionRegistrationJoin;
import models.tables.joined.SubmissionLectureJoin;
import models.tables.joined.SubmissionRegistrationJoin;
import util.exceptions.ForbiddenException;
import util.exceptions.InternalServerErrorException;

import java.sql.SQLException;
import java.util.List;

public class SubmissionService {

    public static SubmissionWithAttach getSubmission(Integer lectureId, Integer assignmentId, Integer submissionId, StudentUser user, DBConnection dbConnection) throws SQLException, ForbiddenException {

        Submission submission = SubmissionRegistrationJoin.getQuery()
                .where((t) ->
                        (t.left.lid).isEqualTo(lectureId).and(
                                (t.aid).isEqualTo(assignmentId).and(
                                        (t.sid).isEqualTo(submissionId).and(
                                                (t.uid).isEqualTo(user.uid)))))
                .first(dbConnection);

        if(submission == null)
            throw new ForbiddenException("permission_denied");

        return addAttachment(submission, assignmentId, submissionId, user.uid, dbConnection);
    }

    public static SubmissionWithAttach getSubmission(Integer lectureId, Integer assignmentId, Integer submissionId, ProfessorUser user, DBConnection dbConnection) throws SQLException, ForbiddenException {
        Submission submission = SubmissionLectureJoin.getQuery()
                .where((t) ->
                        (t.left.lid).isEqualTo(lectureId).and(
                                (t.aid).isEqualTo(assignmentId).and(
                                        (t.sid).isEqualTo(submissionId).and(
                                                (t.left.left.prof).isEqualTo(user.uid)))))
                .first(dbConnection);

        if(submission == null)
            throw new ForbiddenException("permission_denied");

        return addAttachment(submission, assignmentId, submissionId, user.uid, dbConnection);
    }


    public static List<Submission> getSubmissions(Integer lectureId, Integer assignmentId, StudentUser user, DBConnection dbConnection) throws ForbiddenException, SQLException {
        List<Submission> submissions = SubmissionLectureJoin.getQuery()
                .where((t) ->
                        (t.left.lid).isEqualTo(lectureId).and(
                                (t.aid).isEqualTo(assignmentId).and(
                                        (t.right.uid).isEqualTo(user.uid))))
                .list(dbConnection);

        return submissions;

    }

    public static List<Submission> getSubmissions(Integer lectureId, Integer assignmentId, ProfessorUser user, DBConnection dbConnection) throws ForbiddenException, SQLException {
        List<Submission> submissions = RecentSubmissionRegistrationJoin.getQuery()
                .where((t) ->
                        (t.left.left.lid).isEqualTo(lectureId).and(
                                (t.left.aid).isEqualTo(assignmentId).and(
                                        (t.left.left.left.left.prof).isEqualTo(user.uid))))
                .list(dbConnection);

        if(submissions == null || submissions.size() ==0){
            throw new ForbiddenException("no_such_submission");
        }

        return submissions;
    }

    public static Integer createSubmission(SubmissionWithAttach submission, Integer assignmentId, Integer userId, DBConnection dbConnection) throws SQLException, ForbiddenException, InternalServerErrorException {
        dbConnection.transaction();

        Integer submissionId = SubmissionTable.getQuery().insertReturningGenKey(dbConnection,submission);
        if(submissionId == null) {
            throw new ForbiddenException("no_such_assignment");
        }

        if(submission.attachments == null || submission.attachments.length <= 0) {
            return submissionId;
        }

        SubmissionAttachment[] attachments = new SubmissionAttachment[submission.attachments.length];
        for (int i = 0; i < attachments.length; i++) {
            attachments[i] = new SubmissionAttachment();
            attachments[i].aid = assignmentId;
            attachments[i].hashcode_id = submission.attachments[i];
            attachments[i].sid = submissionId;
            attachments[i].owner = userId;
        }

        int rowCount = AssignmentAttachmentTable.getQuery()
                .insert(dbConnection, attachments);

        if (rowCount < attachments.length) {
            dbConnection.rollBack();
            throw new InternalServerErrorException("internal_server_error");
        }

        dbConnection.commit();

        return submissionId;
    }

    private static SubmissionWithAttach addAttachment(Submission submission, Integer assignmentId, Integer submissionId, Integer userId, DBConnection dbConnection) throws SQLException {
        SubmissionWithAttach submissionWithAttach = new SubmissionWithAttach(submission);

        List<SubmissionAttachment> attachments =
                SubmissionAttachmentsTable.getQuery()
                        .where((t) ->
                                t.aid.isEqualTo(assignmentId).and(
                                        t.owner.isEqualTo(submission.uid).and(
                                                t.sid.isEqualTo(submissionId))))
                        .list(dbConnection);

        if(attachments == null) {
            return submissionWithAttach;
        }

        submissionWithAttach.attachments = new String[attachments.size()];
        for(int i = 0; i < attachments.size(); i++) {
            submissionWithAttach.attachments[i] = attachments.get(i).hashcode_id;
        }

        return submissionWithAttach;
    }
}
