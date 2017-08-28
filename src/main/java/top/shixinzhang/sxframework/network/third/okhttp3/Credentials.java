package top.shixinzhang.sxframework.network.third.okhttp3;

import java.io.UnsupportedEncodingException;

import okio.ByteString;

/**
 * Factory for HTTP authorization credentials.
 */
public final class Credentials {
    private Credentials() {
    }

    /**
     * Returns an auth credential for the Basic scheme.
     */
    public static String basic(String userName, String password) {
        try {
            String usernameAndPassword = userName + ":" + password;
            byte[] bytes = usernameAndPassword.getBytes("ISO-8859-1");
            String encoded = ByteString.of(bytes).base64();
            return "Basic " + encoded;
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError();
        }
    }
}
