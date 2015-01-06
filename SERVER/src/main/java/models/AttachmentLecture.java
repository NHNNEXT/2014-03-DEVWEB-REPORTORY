package models;

import com.google.gson.annotations.Expose;
import util.JsonDataSerializable;

/**
 * Created by infinitu on 14. 12. 25..
 */
public class AttachmentLecture extends JsonDataSerializable {
    
    public String   hashcode_id ;
    @Expose public String   directory   ;
    public String filename;
    @Expose public String   type        ;
    @Expose public int      owner       ;
    public int prof;
}
