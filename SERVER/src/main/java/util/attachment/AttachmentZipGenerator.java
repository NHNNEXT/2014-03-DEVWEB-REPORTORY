package util.attachment;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import util.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by infinitu on 15. 1. 6..
 */
public class AttachmentZipGenerator {
    private static final String DOWNLOAD_ZIP_FOLDER = "attachDownload";

    public static ZipOutputHolder createZip() {
        try {
            return new ZipOutputHolder();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class ZipOutputHolder {

        private final FileOutputStream fos;
        private final ZipOutputStream zos;
        private final String zipFilePath;

        private ZipOutputHolder() throws FileNotFoundException {
            zipFilePath = Utils.getTempDir() + DOWNLOAD_ZIP_FOLDER + "/" + System.currentTimeMillis() + ".zip";
            fos = new FileOutputStream(zipFilePath);
            zos = new ZipOutputStream(fos);
        }

        public ZipOutputHolder add(String EntryName, String filePath) throws IOException {
            FileInputStream fis = new FileInputStream(filePath);
            ZipEntry zipEntry = new ZipEntry(EntryName);
            zos.putNextEntry(zipEntry);
            IOUtils.copy(fis, zos);
            return this;
        }

        public void close() {
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getZip() {
            close();
            return this.zipFilePath;
        }
    }

}
