package top.shixinzhang.sxframework.utils.encrypt;

import android.text.TextUtils;

import org.apache.commons.codec.binary.Base64;

/**
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @date 15/11/9
 * @description Edit it! Change it! Beat it! Whatever, just do it!
 */
public class Base64Utils {

    private static Base64 mBase64 = new Base64();

    /**
     * encode
     * @param source source
     * @return String
     */
    public static String encode(String source) {
        if (!TextUtils.isEmpty(source)) {
            return new String(mBase64.encode(source.getBytes()));
        }
        return "";
    }

    /**
     * decode
     * @param source source
     * @return String
     */
    public static String decode(String source) {
        if (!TextUtils.isEmpty(source)) {
            return new String(mBase64.decode(source.getBytes()));
        }
        return "";
    }

    /**
     * encode
     * @param source source
     * @return String
     */
    public static String encodeByte(byte[] source) {
        if (source != null) {
            return new String(mBase64.encode(source));
        }
        return "";
    }

    /**
     * encode
     * @param source source
     * @return String
     */
    public static byte[] encodeToByte(byte[] source) {
        if (source != null) {
            return mBase64.encode(source);
        }
        return null;
    }

    /**
     * decode
     * @param source source
     * @return String
     */
    public static byte[] decodeByte(String source) {
        if (!TextUtils.isEmpty(source)) {
            return mBase64.decode(source.getBytes());
        }
        return null;
    }
}
