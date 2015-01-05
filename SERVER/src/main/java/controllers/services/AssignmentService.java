package controllers.services;

import autumn.database.AbstractQuery;
import autumn.database.jdbc.DBConnection;
import controllers.models.AssignmentWithAttach;
import models.Assignment;
import models.AssignmentAttachment;
import models.ProfessorUser;
import models.StudentUser;
import models.tables.AssignmentAttachmentTable;
import models.tables.AssignmentTable;
import models.tables.joined.LectureAssignmentJoin;
import models.tables.joined.LectureRegistrationAssignmentJoin;
import util.exceptions.ForbiddenException;
import util.exceptions.InternalServerErrorException;
import util.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.List;

public class AssignmentService {

    public static AssignmentWithAttach getAssignment(Integer lectureId, Integer assignmentId, ProfessorUser user, DBConnection dbConnection) throws SQLException, NotFoundException {
        Assignment assignment = getUserAssignQuery(user, lectureId)
                .where((t) -> (t.aid) .isEqualTo (assignmentId))
                .first(dbConnection);

        if (assignment == null) {
            throw new NotFoundException("no_such_assignment");
        }

        return AssignmentService.getAssignmentWithAttach(assignment, dbConnection);
    }

    public static AssignmentWithAttach getAssignment(Integer lectureId, Integer assignmentId, StudentUser user, DBConnection dbConnection) throws SQLException, NotFoundException {
        Assignment assignment = getUserAssignQuery(user, lectureId)
                .where((t) -> (t.aid) .isEqualTo (assignmentId))
                .first(dbConnection);

        if (assignment == null) {
            throw new NotFoundException("no_such_assignment");
        }

        return AssignmentService.getAssignmentWithAttach(assignment, dbConnection);
    }

    public static List<Assignment> getAssignments(Integer lectureId, ProfessorUser user, DBConnection dbConnection) throws SQLException {
        return getUserAssignQuery(user, lectureId).list(dbConnection);
    }

    public static List<Assignment> getAssignments(Integer lectureId, StudentUser user, DBConnection dbConnection) throws SQLException {
        return getUserAssignQuery(user, lectureId).list(dbConnection);
    }

    public static Integer createAssignment(AssignmentWithAttach assignment, Integer userId, DBConnection dbConnection) throws SQLException, ForbiddenException, InternalServerErrorException {
        dbConnection.transaction();

        Integer assignmentId = AssignmentTable.getQuery().insertReturningGenKey(dbConnection, assignment);
        if(assignmentId == null) {
            throw new ForbiddenException("no_such_lecture");
        }

        if(assignment.attachments == null || assignment.attachments.length <= 0) {
            return assignmentId;
        }

        AssignmentAttachment[] attachments = new AssignmentAttachment[assignment.attachments.length];
        for (int i = 0; i < attachments.length; i++) {
            attachments[i] = new AssignmentAttachment();
            attachments[i].aid = assignmentId;
            attachments[i].owner = userId;
            attachments[i].hashcode = assignment.attachments[i];
        }

        Integer rowCount = AssignmentAttachmentTable.getQuery()
                .insert(dbConnection, attachments);

        if(rowCount < attachments.length) {
            dbConnection.rollBack();
            throw new InternalServerErrorException("internal_server_error");
        }

        return assignmentId;
    }

    private static AbstractQuery<LectureAssignmentJoin> getUserAssignQuery(ProfessorUser prof, int lectureId){
        return LectureAssignmentJoin.getQuery()
                .where((t) -> (t.left.prof).isEqualTo(prof.uid).and(
                        (t.left.lid).isEqualTo(lectureId)));
    }

    private static AbstractQuery<LectureRegistrationAssignmentJoin> getUserAssignQuery(StudentUser stu, int lectureId){
        return LectureRegistrationAssignmentJoin.getQuery()
                .where((t) -> (t.right.uid).isEqualTo(stu.uid).and(
                        (t.left.lid).isEqualTo(lectureId)));
    }

    private static AssignmentWithAttach getAssignmentWithAttach(Assignment assignment, DBConnection dbConnection) throws SQLException {
        AssignmentWithAttach assignmentWithAttach = new AssignmentWithAttach(assignment);

        List<AssignmentAttachment> attachments = AssignmentAttachmentTable.getQuery()
                .where((t) -> (t.aid).isEqualTo(assignment.aid))
                .list(dbConnection);

        if(attachments == null) {
            return assignmentWithAttach;
        }

        assignmentWithAttach.attachments = new String[attachments.size()];
        for (int i = 0; i < attachments.size(); i++) {
            assignmentWithAttach.attachments[i] = attachments.get(i).hashcode;
        }

        return assignmentWithAttach;
    }
}
