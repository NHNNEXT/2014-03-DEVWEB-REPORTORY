package models;

import util.JsonDataSerializable;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by infinitu on 14. 12. 25..
 */
public class StudentUser extends JsonDataSerializable {

    public int      uid         ;
    public String   name        ;
    public String   email       ;
    public String   passwd      ;
    public String   third_auth  ;
    public Timestamp join_date   ;
    public String   defIdentity ;
    public String   defMajor    ;

    public Student toStudent(){
        Student stu = new Student();
        stu.uid = uid;
        stu.defIdentity = defIdentity;
        stu.defMajor = defMajor;
        return stu;
    }

    public User toUser(){
        User user = new User();
        user.uid = uid;
        user.name = name;
        user.email=email;
        user.passwd = passwd;
        user.third_auth = third_auth;
        user.join_date = join_date;
        return user;
    }

}
