package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.*;
import autumn.database.TableQuery;
import autumn.database.jdbc.DBConnection;
import autumn.header.Cookie;
import autumn.header.session.SessionData;
import model.MergeRule;
import model.MergeRuleTable;

import java.sql.SQLException;

/**
 * Created by infinitu on 14. 11. 18..
 */

@Controller
//@PATH("/user")
public class test {

    @GET("/{aaa}/:bbb")
    public static Result echo(@INP("aaa")String param,
                              @INP("bbb")String param2,
                              Request request){

        String cntStr = (String) request.getSession("cnt");

        if(cntStr==null) cntStr="0";
        int cnt = Integer.parseInt(cntStr);

        cnt++;

        return Result.Ok.plainText(param +"  "+ cnt + "th call")
                .contentType("text/html")
                .with(new Cookie("testCookie", "testHOHOHO"), new Cookie("testCookie", "testHOHOHO"),new Cookie("testCookie", "testHOHOHO"),new Cookie("testCookie", "testHOHOHO"))
                .with(new SessionData<>("cnt", cnt + ""));



    }



    @GET("/template/:message")
    public static Result templateTest(@INP("message")String message, Request request){

        return Result.Ok.template("thymtest")
                .withVariable("testtext", message);

    }

    @GET("/dbtest")
    public static Result dbTest(Request request) throws SQLException {

        DBConnection conn = request.getDBConnection()
                ;
        TableQuery<MergeRuleTable> mrQ = new TableQuery<>(MergeRuleTable.class);

        Object[] res = mrQ.where((t)->t.oid .isEqualTo (1)).list(conn);



        String str = "";
        for(Object o : res){
            str = str+o.toString()+"\n";
        }

        return Result.Ok.plainText(str);

    }
}