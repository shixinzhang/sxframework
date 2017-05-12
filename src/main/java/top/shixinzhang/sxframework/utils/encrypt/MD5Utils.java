package top.shixinzhang.sxframework.utils.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class MD5Utils {

    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * transfer
     * @param b b
     * @return
     */
    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * get MD5
     * @param s s
     * @return
     */
    public static String md5(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes("UTF-8"));
            return toHexString(digest.digest());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("md5 exception");
        }
    }

    /**
     * get File md5 check
     * @param file file
     * @return
     */
    public static String getFileMD5(File file) {
        byte[] buffer = new byte[8192];
        try {
            InputStream is = new FileInputStream(file);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            int len;
            while ((len = is.read(buffer)) != -1) {
                md5.update(buffer, 0, len);
            }
            is.close();
            return toHexString(md5.digest());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("md5 exception");
        }
    }

}
