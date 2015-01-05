package models;

import util.JsonDataSerializable;

import java.sql.Timestamp;

/**
 * Created by infinitu on 14. 12. 25..
 */
public class User extends JsonDataSerializable {

    public int      uid;
    public String   name;
    public String   email;
    public String   passwd;
    public String   third_auth;
    public Timestamp join_date;

}
