package util;

/**
 * Created by infinitu on 14. 12. 31..
 */
public class Utils {

    private static String tempDir = null;

    public static String getTempDir() {
        if (tempDir != null)
            return tempDir;
        tempDir = System.getenv("TMPDIR");
        if (tempDir == null || tempDir.equals(""))
            tempDir = System.getenv("TEMP");
        if (tempDir == null || tempDir.equals(""))
            tempDir = "/temp/REPORTORY";
        return tempDir;
    }

    public static String byteArrToHex(byte[] arr) {
        StringBuilder sb = new StringBuilder(arr.length * 2);
        for (byte b : arr) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
