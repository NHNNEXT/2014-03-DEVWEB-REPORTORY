package models;

import util.JsonDataSerializable;

import java.sql.Timestamp;

/**
 * Created by infinitu on 14. 12. 25..
 */
public class Submission extends JsonDataSerializable {
    
    public int          sid         ;
    public int          uid         ;
    public int          aid         ;
    public String       description ;
    public Timestamp    create_date ;

}
