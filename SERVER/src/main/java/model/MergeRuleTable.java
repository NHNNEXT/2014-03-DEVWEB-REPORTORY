package model;

import autumn.annotation.Model;
import autumn.database.Column;
import autumn.database.Table;

/**
 * Created by infinitu on 14. 12. 18..
 */
@Model("merge_rules")
public class MergeRuleTable extends Table<MergeRule>{

    public MergeRuleTable() throws NoSuchFieldException {
        super(MergeRule.class);
    }

    public Column<String> hash = stringColumn("content_hash");
    public Column<Integer> oid = intColumn("optimizeid");
    public Column<Integer> idx = intColumn("merge_idx");
}
