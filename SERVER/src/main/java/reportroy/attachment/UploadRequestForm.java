package reportroy.attachment;

/**
* Created by infinitu on 14. 12. 31..
*/
public class UploadRequestForm {
    public String name;
    public long size;
    public int owner;

    public boolean isValidate() {
        return !(this.name == null || name.isEmpty()) && size > 0;
    }
}
