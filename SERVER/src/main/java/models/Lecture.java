package models;

import util.JsonDataSerializable;

import java.util.Date;

/**
 * Created by infinitu on 14. 12. 25..
 */
public class Lecture extends JsonDataSerializable {

    public int      lid ;
    public int      prof;
    public String   name;
    public Date     startDate;
    public Date     finishDate;

}
