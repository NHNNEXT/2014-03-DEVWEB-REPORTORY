package models;

import util.JsonDataSerializable;

import java.sql.Timestamp;

/**
 * Created by infinitu on 14. 12. 25..
 */
public class Submisstion extends JsonDataSerializable {

    public int          uid         ;
    public int          lid         ;
    public int          aid         ;
    public int          revision    ;
    public String       description ;
    public Timestamp    create_date ;

}
