package models.tables;

import autumn.annotation.Model;
import autumn.database.Column;
import autumn.database.Table;
import autumn.database.TableQuery;
import models.Professor;

/**
 * Created by infinitu on 14. 12. 25..
 */

@Model("Professors")
public class ProfessorTable extends Table<Professor> {

    public Column<Integer>  uid     = intColumn("uid");
    public Column<String>   major   = stringColumn("major");

    public ProfessorTable() throws NoSuchFieldException {
        super(Professor.class);
    }

    private static TableQuery<ProfessorTable> tQuery;
    public static TableQuery<ProfessorTable> getQuery() {
        if(tQuery==null)
            tQuery = new TableQuery<>(ProfessorTable.class);
        return tQuery;
    }

}
