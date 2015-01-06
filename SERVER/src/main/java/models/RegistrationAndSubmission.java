package models;

import util.JsonDataSerializable;

import java.sql.Timestamp;

/**
 * Created by infinitu on 14. 12. 25..
 */
public class RegistrationAndSubmission extends JsonDataSerializable {

    public int uid;
    public String identity;
    public String major;
    public String stu_name;

    public int sid;
    public int aid;
    public String description;
    public Timestamp create_date;

}
