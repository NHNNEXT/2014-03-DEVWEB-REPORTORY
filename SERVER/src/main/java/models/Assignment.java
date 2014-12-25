package models;

import util.JsonDataSerializable;

import java.sql.Timestamp;

/**
 * Created by infinitu on 14. 12. 25..
 */
public class Assignment extends JsonDataSerializable {

    public int aid;
    public int lid;
    public Timestamp createTime;
    public Timestamp dueTime;
    public String title;
    public String text;

}
