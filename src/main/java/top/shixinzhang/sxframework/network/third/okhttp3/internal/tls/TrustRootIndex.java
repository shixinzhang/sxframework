package top.shixinzhang.sxframework.network.third.okhttp3.internal.tls;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.PublicKey;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;

public abstract class TrustRootIndex {
    /**
     * Returns the trusted CA certificate that signed {@code cert}.
     */
    public abstract X509Certificate findByIssuerAndSignature(X509Certificate cert);

    public static okhttp3.internal.tls.TrustRootIndex get(X509TrustManager trustManager) {
        try {
            // From org.conscrypt.TrustManagerImpl, we want the method with this signature:
            // private TrustAnchor findTrustAnchorByIssuerAndSignature(X509Certificate lastCert);
            Method method = trustManager.getClass().getDeclaredMethod(
                    "findTrustAnchorByIssuerAndSignature", X509Certificate.class);
            method.setAccessible(true);
            return new AndroidTrustRootIndex(trustManager, method);
        } catch (NoSuchMethodException e) {
            return get(trustManager.getAcceptedIssuers());
        }
    }

    public static okhttp3.internal.tls.TrustRootIndex get(X509Certificate... caCerts) {
        return new BasicTrustRootIndex(caCerts);
    }

    /**
     * An index of trusted root certificates that exploits knowledge of Android implementation
     * details. This class is potentially much faster to initialize than {@link TrustRootIndex.BasicTrustRootIndex}
     * because it doesn't need to load and index trusted CA certificates.
     * <p>
     * <p>This class uses APIs added to Android in API 14 (Android 4.0, released October 2011). This
     * class shouldn't be used in Android API 17 or better because those releases are better served by
     * {AndroidPlatforæm.AndroidCertificateChainCleaner}.
     */
    static final class AndroidTrustRootIndex extends okhttp3.internal.tls.TrustRootIndex {
        private final X509TrustManager trustManager;
        private final Method findByIssuerAndSignatureMethod;

        AndroidTrustRootIndex(X509TrustManager trustManager, Method findByIssuerAndSignatureMethod) {
            this.findByIssuerAndSignatureMethod = findByIssuerAndSignatureMethod;
            this.trustManager = trustManager;
        }

        @Override
        public X509Certificate findByIssuerAndSignature(X509Certificate cert) {
            try {
                TrustAnchor trustAnchor = (TrustAnchor) findByIssuerAndSignatureMethod.invoke(
                        trustManager, cert);
                return trustAnchor != null
                        ? trustAnchor.getTrustedCert()
                        : null;
            } catch (IllegalAccessException e) {
                throw new AssertionError();
            } catch (InvocationTargetException e) {
                return null;
            }
        }
    }

    /**
     * A simple index that of trusted root certificates that have been loaded into memory.
     */
    static final class BasicTrustRootIndex extends okhttp3.internal.tls.TrustRootIndex {
        private final Map<X500Principal, List<X509Certificate>> subjectToCaCerts;

        public BasicTrustRootIndex(X509Certificate... caCerts) {
            subjectToCaCerts = new LinkedHashMap<>();
            for (X509Certificate caCert : caCerts) {
                X500Principal subject = caCert.getSubjectX500Principal();
                List<X509Certificate> subjectCaCerts = subjectToCaCerts.get(subject);
                if (subjectCaCerts == null) {
                    subjectCaCerts = new ArrayList<>(1);
                    subjectToCaCerts.put(subject, subjectCaCerts);
                }
                subjectCaCerts.add(caCert);
            }
        }

        @Override
        public X509Certificate findByIssuerAndSignature(X509Certificate cert) {
            X500Principal issuer = cert.getIssuerX500Principal();
            List<X509Certificate> subjectCaCerts = subjectToCaCerts.get(issuer);
            if (subjectCaCerts == null) return null;

            for (X509Certificate caCert : subjectCaCerts) {
                PublicKey publicKey = caCert.getPublicKey();
                try {
                    cert.verify(publicKey);
                    return caCert;
                } catch (Exception ignored) {
                }
            }

            return null;
        }
    }
}
