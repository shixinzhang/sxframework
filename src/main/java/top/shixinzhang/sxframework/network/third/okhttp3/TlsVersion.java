package top.shixinzhang.sxframework.network.third.okhttp3;

/**
 * Versions of TLS that can be offered when negotiating a secure socket. See {@link
 * javax.net.ssl.SSLSocket#setEnabledProtocols}.
 */
public enum TlsVersion {
    TLS_1_2("TLSv1.2"), // 2008.
    TLS_1_1("TLSv1.1"), // 2006.
    TLS_1_0("TLSv1"),   // 1999.
    SSL_3_0("SSLv3"),   // 1996.
    ;

    final String javaName;

    TlsVersion(String javaName) {
        this.javaName = javaName;
    }

    public static TlsVersion forJavaName(String javaName) {
        switch (javaName) {
            case "TLSv1.2":
                return TLS_1_2;
            case "TLSv1.1":
                return TLS_1_1;
            case "TLSv1":
                return TLS_1_0;
            case "SSLv3":
                return SSL_3_0;
        }
        throw new IllegalArgumentException("Unexpected TLS version: " + javaName);
    }

    public String javaName() {
        return javaName;
    }
}
