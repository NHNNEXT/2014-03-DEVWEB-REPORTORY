package models;

import util.JsonDataSerializable;

/**
 * Created by infinitu on 14. 12. 25..
 */
public class LectureRegistration extends JsonDataSerializable {

    public int      uid     ;
    public int      lid     ;
    public String   identity;
    public String   major   ;
    public String   stu_name;
    public boolean  accepted;

}
