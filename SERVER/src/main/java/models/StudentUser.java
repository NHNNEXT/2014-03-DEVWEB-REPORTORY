package models;

import java.sql.Date;

/**
 * Created by infinitu on 14. 12. 25..
 */
public class StudentUser {

    public int      uid         ;
    public String   name        ;
    public String   email       ;
    public String   passwd      ;
    public String   third_auth  ;
    public Date     join_date   ;
    public String   defIdentity ;
    public String   defMajor    ;

}
