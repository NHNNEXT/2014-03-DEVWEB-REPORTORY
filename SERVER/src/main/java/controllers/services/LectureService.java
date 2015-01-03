package controllers.services;

import autumn.database.jdbc.DBConnection;
import models.Lecture;
import models.LectureDetail;
import models.LectureRegistration;
import models.tables.LectureRegistrationTable;
import models.tables.LectureTable;
import models.tables.joined.LectureProfessorJoin;
import util.exceptions.BadRequestException;
import util.exceptions.InternalServerErrorException;
import util.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.List;

public class LectureService {

    public static LectureDetail getLecture(Integer lectureId, DBConnection dbConnection) throws SQLException, NotFoundException {
        LectureDetail lecture = LectureProfessorJoin.getQuery()
                .where((t) -> (t.lid).isEqualTo(lectureId))
                .first(dbConnection);

        if (lecture == null) {
            throw new NotFoundException("no_such_lecture");
        }

        return lecture;
    }

    public static List<Lecture> getLecturesByProfessor(Integer professorId, DBConnection dbConnection) throws SQLException {
        List<Lecture> lectures = LectureTable.getQuery()
                .where((t) ->
                        (t.prof).isEqualTo(professorId))
                .list(dbConnection);

        return lectures;
    }

    public static List<LectureDetail> getLecturesByQuery(String query, DBConnection dbConnection) throws SQLException {
        List<LectureDetail> lectures = LectureProfessorJoin.getQuery()
                .where((t)->(t.lecturename).isLike(query))
                .list(dbConnection);

        return lectures;
    }

    public static Integer createLecture(Lecture lecture, DBConnection dbConnection) throws SQLException, InternalServerErrorException {
        Integer lectureId = LectureTable.getQuery()
                .insertReturningGenKey(dbConnection, lecture);

        if (lectureId == null) {
            throw new InternalServerErrorException("create_failed");
        }

        return lectureId;
    }

    public static void deleteLecture(Integer lectureId, Integer professorId, DBConnection dbConnection) throws SQLException, BadRequestException {
        Integer rowCount = LectureTable.getQuery()
                .where((t) -> (t.lid).isEqualTo(lectureId).and(
                        (t.prof).isEqualTo(professorId)))
                .delete(dbConnection);

        if (rowCount < 1) {
            throw new BadRequestException("no_such_lecture");
        }
    }

    public static void joinLecture(LectureRegistration lectureRegistration, DBConnection dbConnection) throws SQLException, BadRequestException {
        Integer rowCount = LectureTable.getQuery()
                .insert(dbConnection, lectureRegistration);

        if (rowCount < 1) {
            throw new BadRequestException("invalid_request");
        }
    }

    public static void leaveLecture(Integer lectureId, Integer studentId, DBConnection dbConnection) throws SQLException, BadRequestException {
        Integer rowCount = LectureRegistrationTable.getQuery()
                .where((t) -> (t.lid).isEqualTo(lectureId).and(
                        (t.uid).isEqualTo(studentId)))
                .delete(dbConnection);

        if (rowCount < 1) {
            throw new BadRequestException("invalid_request");
        }
    }
}
