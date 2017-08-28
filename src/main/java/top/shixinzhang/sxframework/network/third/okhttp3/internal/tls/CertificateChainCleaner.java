package top.shixinzhang.sxframework.network.third.okhttp3.internal.tls;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.X509TrustManager;

import top.shixinzhang.sxframework.network.third.okhttp3.internal.platform.Platform;

/**
 * Computes the effective certificate chain from the raw array returned by Java's built in TLS APIs.
 * Cleaning a chain returns a list of certificates where the first element is {@code chain[0]}, each
 * certificate is signed by the certificate that follows, and the last certificate is a trusted CA
 * certificate.
 * <p>
 * <p>Use of the chain cleaner is necessary to omit unexpected certificates that aren't relevant to
 * the TLS handshake and to extract the trusted CA certificate for the benefit of certificate
 * pinning.
 */
public abstract class CertificateChainCleaner {
    public abstract List<Certificate> clean(List<Certificate> chain, String hostname)
            throws SSLPeerUnverifiedException;

    public static CertificateChainCleaner get(X509TrustManager trustManager) {
        return Platform.get().buildCertificateChainCleaner(trustManager);
    }

    public static top.shixinzhang.sxframework.network.third.okhttp3.internal.tls.CertificateChainCleaner get(X509Certificate... caCerts) {
        return new BasicCertificateChainCleaner(TrustRootIndex.get(caCerts));
    }
}
