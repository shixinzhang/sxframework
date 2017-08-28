package top.shixinzhang.sxframework.network.third.okhttp3.internal;

import java.net.MalformedURLException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;

import top.shixinzhang.sxframework.network.third.okhttp3.Address;
import top.shixinzhang.sxframework.network.third.okhttp3.Call;
import top.shixinzhang.sxframework.network.third.okhttp3.ConnectionPool;
import top.shixinzhang.sxframework.network.third.okhttp3.ConnectionSpec;
import top.shixinzhang.sxframework.network.third.okhttp3.Headers;
import top.shixinzhang.sxframework.network.third.okhttp3.HttpUrl;
import top.shixinzhang.sxframework.network.third.okhttp3.OkHttpClient;
import top.shixinzhang.sxframework.network.third.okhttp3.internal.cache.InternalCache;
import top.shixinzhang.sxframework.network.third.okhttp3.internal.connection.RealConnection;
import top.shixinzhang.sxframework.network.third.okhttp3.internal.connection.RouteDatabase;
import top.shixinzhang.sxframework.network.third.okhttp3.internal.connection.StreamAllocation;

/**
 * Escalate internal APIs in {@code top.shixinzhang.sxframework.network.third.okhttp3} so they can be used from OkHttp's implementation
 * packages. The only implementation of this interface is in {@link OkHttpClient}.
 */
public abstract class Internal {

    public static void initializeInstanceForTests() {
        // Needed in tests to ensure that the instance is actually pointing to something.
        new OkHttpClient();
    }

    public static top.shixinzhang.sxframework.network.third.okhttp3.internal.Internal instance;

    public abstract void addLenient(Headers.Builder builder, String line);

    public abstract void addLenient(Headers.Builder builder, String name, String value);

    public abstract void setCache(OkHttpClient.Builder builder, InternalCache internalCache);

    public abstract RealConnection get(
            ConnectionPool pool, Address address, StreamAllocation streamAllocation);

    public abstract void put(ConnectionPool pool, RealConnection connection);

    public abstract boolean connectionBecameIdle(ConnectionPool pool, RealConnection connection);

    public abstract RouteDatabase routeDatabase(ConnectionPool connectionPool);

    public abstract void apply(ConnectionSpec tlsConfiguration, SSLSocket sslSocket,
                               boolean isFallback);

    public abstract HttpUrl getHttpUrlChecked(String url)
            throws MalformedURLException, UnknownHostException;

    public abstract StreamAllocation callEngineGetStreamAllocation(Call call);

    public abstract void setCallWebSocket(Call call);
}
