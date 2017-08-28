package top.shixinzhang.sxframework.network.third.okhttp3.internal.platform;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import javax.net.ssl.SSLSocket;

import top.shixinzhang.sxframework.network.third.okhttp3.Protocol;
import top.shixinzhang.sxframework.network.third.okhttp3.internal.Util;

/**
 * OpenJDK 7 or OpenJDK 8 with {@code org.mortbay.jetty.alpn/alpn-boot} in the boot class path.
 */
class JdkWithJettyBootPlatform extends Platform {
    private final Method putMethod;
    private final Method getMethod;
    private final Method removeMethod;
    private final Class<?> clientProviderClass;
    private final Class<?> serverProviderClass;

    public JdkWithJettyBootPlatform(Method putMethod, Method getMethod, Method removeMethod,
                                    Class<?> clientProviderClass, Class<?> serverProviderClass) {
        this.putMethod = putMethod;
        this.getMethod = getMethod;
        this.removeMethod = removeMethod;
        this.clientProviderClass = clientProviderClass;
        this.serverProviderClass = serverProviderClass;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void configureTlsExtensions(
            SSLSocket sslSocket, String hostname, List<Protocol> protocols) {
        List<String> names = alpnProtocolNames(protocols);

        try {
            Object provider = Proxy.newProxyInstance(okhttp3.internal.platform.Platform.class.getClassLoader(),
                    new Class[]{clientProviderClass, serverProviderClass}, new JettyNegoProvider(names));
            putMethod.invoke(null, sslSocket, provider);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void afterHandshake(SSLSocket sslSocket) {
        try {
            removeMethod.invoke(null, sslSocket);
        } catch (IllegalAccessException | InvocationTargetException ignored) {
            throw new AssertionError();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public String getSelectedProtocol(SSLSocket socket) {
        try {
            JettyNegoProvider provider =
                    (JettyNegoProvider) Proxy.getInvocationHandler(getMethod.invoke(null, socket));
            if (!provider.unsupported && provider.selected == null) {
                okhttp3.internal.platform.Platform.get().log(INFO, "ALPN callback dropped: SPDY and HTTP/2 are disabled. "
                        + "Is alpn-boot on the boot class path?", null);
                return null;
            }
            return provider.unsupported ? null : provider.selected;
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new AssertionError();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Platform buildIfSupported() {
        // Find Jetty's ALPN extension for OpenJDK.
        try {
            String negoClassName = "org.eclipse.jetty.alpn.ALPN";
            Class<?> negoClass = Class.forName(negoClassName);
            Class<?> providerClass = Class.forName(negoClassName + "$Provider");
            Class<?> clientProviderClass = Class.forName(negoClassName + "$ClientProvider");
            Class<?> serverProviderClass = Class.forName(negoClassName + "$ServerProvider");
            Method putMethod = negoClass.getMethod("put", SSLSocket.class, providerClass);
            Method getMethod = negoClass.getMethod("get", SSLSocket.class);
            Method removeMethod = negoClass.getMethod("remove", SSLSocket.class);
            return new JdkWithJettyBootPlatform(
                    putMethod, getMethod, removeMethod, clientProviderClass, serverProviderClass);
        } catch (ClassNotFoundException | NoSuchMethodException ignored) {
        }

        return null;
    }

    /**
     * Handle the methods of ALPN's ClientProvider and ServerProvider without a compile-time
     * dependency on those interfaces.
     */
    private static class JettyNegoProvider implements InvocationHandler {
        /**
         * This peer's supported protocols.
         */
        private final List<String> protocols;
        /**
         * Set when remote peer notifies ALPN is unsupported.
         */
        private boolean unsupported;
        /**
         * The protocol the server selected.
         */
        private String selected;

        public JettyNegoProvider(List<String> protocols) {
            this.protocols = protocols;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            Class<?> returnType = method.getReturnType();
            if (args == null) {
                args = Util.EMPTY_STRING_ARRAY;
            }
            if (methodName.equals("supports") && boolean.class == returnType) {
                return true; // ALPN is supported.
            } else if (methodName.equals("unsupported") && void.class == returnType) {
                this.unsupported = true; // Peer doesn't support ALPN.
                return null;
            } else if (methodName.equals("protocols") && args.length == 0) {
                return protocols; // Client advertises these protocols.
            } else if ((methodName.equals("selectProtocol") || methodName.equals("select"))
                    && String.class == returnType && args.length == 1 && args[0] instanceof List) {
                List<String> peerProtocols = (List) args[0];
                // Pick the first known protocol the peer advertises.
                for (int i = 0, size = peerProtocols.size(); i < size; i++) {
                    if (protocols.contains(peerProtocols.get(i))) {
                        return selected = peerProtocols.get(i);
                    }
                }
                return selected = protocols.get(0); // On no intersection, try peer's first protocol.
            } else if ((methodName.equals("protocolSelected") || methodName.equals("selected"))
                    && args.length == 1) {
                this.selected = (String) args[0]; // Server selected this protocol.
                return null;
            } else {
                return method.invoke(this, args);
            }
        }
    }
}