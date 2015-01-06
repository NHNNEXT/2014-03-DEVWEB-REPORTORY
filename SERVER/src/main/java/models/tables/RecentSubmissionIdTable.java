package models.tables;

import autumn.annotation.Model;
import autumn.database.Column;
import autumn.database.Table;

/**
 * Created by infinitu on 15. 1. 4..
 */
@Model("Submissions")
public class RecentSubmissionIdTable extends Table<RecentSubmissionIdTable.SubmissionRecentId> {

    public Column<Integer> maxSid = intColumn("max_sid");
    public Column<Integer> count = intColumn("count");
    public Column<Integer> aid = intColumn("aid");
    public Column<Integer> uid = intColumn("uid");

    public RecentSubmissionIdTable() throws NoSuchFieldException {
        super(SubmissionRecentId.class);
    }

    @Override
    protected String toSQL() {
        return "(SELECT max(sid) AS max_sid, count(*) AS count, aid,uid FROM Submissions GROUP BY aid, uid) " + getTag();
    }

    public static class SubmissionRecentId {
        public int maxSid;
        public int count;
        public int aid;
        public int uid;
    }
}
