package models.tables.joined;

import autumn.database.Column;
import autumn.database.Condition;
import autumn.database.JoinQuery;
import autumn.database.JoinTable;
import models.Lecture;
import models.tables.LectureRegistrationTable;
import models.tables.LectureTable;

import java.sql.Date;

public class LectureRegistrationJoin extends JoinTable<LectureTable, LectureRegistrationTable, Lecture> {

    private static ThreadLocal<JoinQuery<LectureRegistrationJoin>> tQuery;
    public Column<Integer> lid = left.lid;
    public Column<Integer> prof = left.prof;
    public Column<String> name = left.name;
    public Column<Date> startDate = left.startDate;
    public Column<Date> finishDate = left.finishDate;

    public LectureRegistrationJoin() throws NoSuchFieldException {
        super(new LectureTable(), new LectureRegistrationTable(), Lecture.class);
    }

    public static JoinQuery<LectureRegistrationJoin> getQuery() {
        if (tQuery == null)
            tQuery = new ThreadLocal<JoinQuery<LectureRegistrationJoin>>() {
                @Override
                protected JoinQuery<LectureRegistrationJoin> initialValue() {
                    return new JoinQuery<>(LectureRegistrationJoin.class);
                }
            };
        return tQuery.get();
    }

    @Override
    public Condition on(LectureTable lectureTable, LectureRegistrationTable lectureRegistrationTable) {
        return lectureRegistrationTable.lid.isEqualTo(lectureTable.lid).and(
                lectureRegistrationTable.accepted.isEqualTo(true));
    }
}
