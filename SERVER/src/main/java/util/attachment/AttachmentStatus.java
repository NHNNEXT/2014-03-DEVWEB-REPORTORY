package util.attachment;

import util.Utils;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.file.StandardOpenOption.WRITE;

/**
* Created by infinitu on 14. 12. 31..
*/ //todo extract.
public class AttachmentStatus {

    private long expire;
    private UploadRequestForm info;
    private static final long MAX_AGE = 3600000L;
    private static final int MAX_BUF_SIZE = 1024;
    private static final byte[] TMP_BUF= new byte[MAX_BUF_SIZE];
    
    Path path;

    public AttachmentStatus(UploadRequestForm form, String path) throws IOException {
        if(!form.isValidate())
            throw new IOException("not validate input");
        this.path = Paths.get(path);
        this.info = form;
        File f = new File(path);
        File p = f.getParentFile();
        if(!p.exists())
            p.mkdirs();
        if (!f.createNewFile())
            throw new IOException("can't not create file.");
        RandomAccessFile nFile = new RandomAccessFile(f,"rw");
        nFile.setLength(info.size);
        expire = System.currentTimeMillis()+MAX_AGE;
    }

    public boolean isExpired(){
        return this.expire < System.currentTimeMillis();
    }

    public long getSize(){
        return info.size;
    }
    
    public File getFile(){
        return path.toFile();
    }
    
    public String getFilename(){
        return info.name;
    }
    
    public int getOwnerId(){
        return info.owner;
    }

    public void writeFile(long offset, long length, InputStream inputStream) throws Exception {
        if(isExpired()){
            throw new Exception("expired request");
        }

        if(offset+length>info.size)
            throw new Exception("length is too long");

        FileChannel channel = FileChannel.open(path, WRITE);
        ReadableByteChannel inChannel = Channels.newChannel(inputStream);

        channel.position(offset);

        channel.transferFrom(inChannel, offset, length);
        channel.close();
    }
    
    public String calcFileHash(){
        String ret = null;
        DigestInputStream inStream = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            inStream = new DigestInputStream(new FileInputStream(path.toFile()),md);
            while(inStream.read(TMP_BUF)!=-1);
            ret = Utils.byteArrToHex(md.digest());
        } catch (NoSuchAlgorithmException|IOException e) {
            e.printStackTrace();
        } finally {
            if(inStream!=null)
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return ret;
    }
}
