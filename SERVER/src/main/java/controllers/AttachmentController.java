package controllers;

import autumn.Request;
import autumn.Result;
import autumn.annotation.Controller;
import autumn.annotation.GET;
import autumn.annotation.INP;
import autumn.annotation.POST;
import autumn.header.session.SessionKeyIssuer;
import com.google.common.net.HttpHeaders;
import models.Attachment;
import models.User;
import models.tables.AttachmentsTable;
import reportroy.attachment.AttachmentStatus;
import reportroy.attachment.UploadRequestForm;
import util.Utils;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by infinitu on 14. 12. 26..
 */
@Controller
public class AttachmentController {
    private final static String AttachmentTempSubdirectory = "attachments/";
    private final static String DefaultUploadDirectory = System.getProperty("user.home")+"/REPORTORY/UPLOAD/";
    
    private final static Pattern pattern = Pattern.compile("bytes ([0-9]+)-([0-9]+)/([0-9]+)");
    private final static ConcurrentHashMap<String,AttachmentStatus> map = new ConcurrentHashMap<>(); //todo extract to another server. todo automatically remove expired entry.

    @GET("/attachment")
    public static Result uploadUITest(Request req){
        return Result.Ok.template("thymtest");
    }
    
    @POST("/attachment")
    public static Result uploadFile(Request req) throws NoSuchAlgorithmException {
        User user;
        if(UserController.isPrefessorUser(req))
            user = UserController.getProfLoginData(req).toUser();
        else if(UserController.isStudentUser(req))
            user = UserController.getStuLoginData(req).toUser();
        else
            return Result.Forbidden.plainText("only user can upload files");
        
        SessionKeyIssuer issuer = new SessionKeyIssuer("");
        UploadRequestForm info = req.body().asJson().mapping(UploadRequestForm.class);
        info.owner = user.uid;
        String tempKey;
        if(!info.isValidate())
            return Result.BadRequest.plainText("form is invalidated");
        try {
            tempKey = issuer.issueHEX();
            AttachmentStatus status = new AttachmentStatus(info, newPathStr(tempKey));
            map.put(tempKey,status);
        } catch (IOException e) {
            return Result.InternalServerError.plainText("error on creating");
        }
        
        return Result.Ok.plainText("/attachment/"+tempKey);
    }

    @POST("/attachment/{auth}")
    public static Result uploadFile(@INP("auth")String auth, Request req) throws Exception {
        
        AttachmentStatus obj = map.get(auth);
        if (obj == null)
            return Result.Forbidden.plainText("forbidden");

        String range = req.getHeader(HttpHeaders.CONTENT_RANGE);
        Matcher matcher = pattern.matcher(range);
        if(!matcher.find())
            return Result.BadRequest.plainText("invalidate header");
        long st = Long.parseLong(matcher.group(1));
        long ed = Long.parseLong(matcher.group(2));
        long am = Long.parseLong(matcher.group(3));
        long length = Long.parseLong(req.getHeader(HttpHeaders.CONTENT_LENGTH));



        if (st == ed && st == am) {
            map.remove(auth);
            String hash = finishUploade(req, obj);
            return Result.Ok.plainText(hash);
        }
        
        if(length != ed-st+1 || am != obj.getSize())
            return Result.Forbidden.plainText("forbidden");

        
        obj.writeFile(st,length,req.body().stream());

        return Result.Ok.plainText("ok.");
    }

    private static String finishUploade(Request req, AttachmentStatus status) throws Exception {
        File file = status.getFile();
        String hash = status.calcFileHash();
        String encodedName = URLEncoder.encode(status.getFilename(),"UTF-8");
        File dest = new File(DefaultUploadDirectory + hash + encodedName);
        dest.getParentFile().mkdirs();
        boolean success = file.renameTo(dest);
        if(!success)
            throw new Exception("Moving File is Failed");

        Attachment attachment = new Attachment();
        attachment.filenem = encodedName;
        attachment.directory = dest.getAbsolutePath();
        attachment.hashcode_id = hash;
        attachment.owner = status.getOwnerId();
        int res = AttachmentsTable.getQuery().insert(req.getDBConnection(),attachment);
        if(res < 1)
            throw new Exception("insertionException,");
        
        return hash;
    }

    private static String newPathStr(String key){
        return Utils.getTempDir() + AttachmentTempSubdirectory + key;
    }


}