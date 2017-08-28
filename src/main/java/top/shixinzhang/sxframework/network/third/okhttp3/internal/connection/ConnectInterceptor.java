package top.shixinzhang.sxframework.network.third.okhttp3.internal.connection;

import java.io.IOException;

import top.shixinzhang.sxframework.network.third.okhttp3.Interceptor;
import top.shixinzhang.sxframework.network.third.okhttp3.OkHttpClient;
import top.shixinzhang.sxframework.network.third.okhttp3.Request;
import top.shixinzhang.sxframework.network.third.okhttp3.Response;
import top.shixinzhang.sxframework.network.third.okhttp3.internal.http.HttpStream;
import top.shixinzhang.sxframework.network.third.okhttp3.internal.http.RealInterceptorChain;

/**
 * Opens a connection to the target server and proceeds to the next interceptor.
 */
public final class ConnectInterceptor implements Interceptor {
    public final OkHttpClient client;

    public ConnectInterceptor(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        RealInterceptorChain realChain = (RealInterceptorChain) chain;
        Request request = realChain.request();
        StreamAllocation streamAllocation = realChain.streamAllocation();

        // We need the network to satisfy this request. Possibly for validating a conditional GET.
        boolean doExtensiveHealthChecks = !request.method().equals("GET");
        HttpStream httpStream = streamAllocation.newStream(client, doExtensiveHealthChecks);
        RealConnection connection = streamAllocation.connection();

        return realChain.proceed(request, streamAllocation, httpStream, connection);
    }
}
