package models;

import util.JsonDataSerializable;

import java.sql.Timestamp;

/**
 * Created by infinitu on 14. 12. 25..
 */
public class ProfessorUser extends JsonDataSerializable {

    public int uid;
    public String name;
    public String email;
    public String passwd;
    public String third_auth;
    public Timestamp join_date;
    public String major;


    public ProfessorUser() {
    }

    public ProfessorUser(int uid, String name, String email, String passwd, String third_auth, Timestamp join_date, String major) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.passwd = passwd;
        this.third_auth = third_auth;
        this.join_date = join_date;
        this.major = major;
    }

    public Professor toProfessor() {
        Professor prof = new Professor();
        prof.major = major;
        prof.uid = uid;
        return prof;
    }

    public User toUser() {
        User user = new User();
        user.uid = uid;
        user.name = name;
        user.email = email;
        user.passwd = passwd;
        user.third_auth = third_auth;
        user.join_date = join_date;
        return user;
    }
}
