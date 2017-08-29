package top.shixinzhang.sxframework.network.third.okhttp3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import top.shixinzhang.sxframework.network.third.okhttp3.internal.NamedRunnable;
import top.shixinzhang.sxframework.network.third.okhttp3.internal.cache.CacheInterceptor;
import top.shixinzhang.sxframework.network.third.okhttp3.internal.connection.ConnectInterceptor;
import top.shixinzhang.sxframework.network.third.okhttp3.internal.connection.StreamAllocation;
import top.shixinzhang.sxframework.network.third.okhttp3.internal.http.BridgeInterceptor;
import top.shixinzhang.sxframework.network.third.okhttp3.internal.http.CallServerInterceptor;
import top.shixinzhang.sxframework.network.third.okhttp3.internal.http.RealInterceptorChain;
import top.shixinzhang.sxframework.network.third.okhttp3.internal.http.RetryAndFollowUpInterceptor;
import top.shixinzhang.sxframework.network.third.okhttp3.internal.platform.Platform;

import static top.shixinzhang.sxframework.network.third.okhttp3.internal.platform.Platform.INFO;

final class RealCall implements Call {
    private final OkHttpClient client;
    private final RetryAndFollowUpInterceptor retryAndFollowUpInterceptor;

    // Guarded by this.
    private boolean executed;

    /**
     * The application's original request unadulterated by redirects or auth headers.
     */
    Request originalRequest;

    protected RealCall(OkHttpClient client, Request originalRequest) {
        this.client = client;
        this.originalRequest = originalRequest;
        this.retryAndFollowUpInterceptor = new RetryAndFollowUpInterceptor(client);
    }

    @Override
    public Request request() {
        return originalRequest;
    }

    @Override
    public Response execute() throws IOException {
        synchronized (this) {
            if (executed) throw new IllegalStateException("Already Executed");
            executed = true;
        }
        try {
            client.dispatcher().executed(this);
            Response result = getResponseWithInterceptorChain();
            if (result == null) throw new IOException("Canceled");
            return result;
        } finally {
            client.dispatcher().finished(this);
        }
    }

    synchronized void setForWebSocket() {
        if (executed) throw new IllegalStateException("Already Executed");
        this.retryAndFollowUpInterceptor.setForWebSocket(true);
    }

    @Override
    public void enqueue(Callback responseCallback) {
        synchronized (this) {
            if (executed) throw new IllegalStateException("Already Executed");
            executed = true;
        }
        client.dispatcher().enqueue(new AsyncCall(responseCallback));
    }

    @Override
    public void cancel() {
        retryAndFollowUpInterceptor.cancel();
    }

    @Override
    public synchronized boolean isExecuted() {
        return executed;
    }

    @Override
    public boolean isCanceled() {
        return retryAndFollowUpInterceptor.isCanceled();
    }

    StreamAllocation streamAllocation() {
        return retryAndFollowUpInterceptor.streamAllocation();
    }

    /**
     * RealCall 的内部类，异步任务执行，访问外部类的信息和方法
     */
    final class AsyncCall extends NamedRunnable {
        private final Callback responseCallback;

        private AsyncCall(Callback responseCallback) {
            super("OkHttp %s", redactedUrl().toString());
            this.responseCallback = responseCallback;
        }

        String host() {
            return originalRequest.url().host();
        }

        Request request() {
            return originalRequest;
        }

        RealCall get() {
            return RealCall.this;
        }

        /**
         * 异步任务执行
         */
        @Override
        protected void execute() {
            boolean signalledCallback = false;
            try {
                Response response = getResponseWithInterceptorChain();  //拿到结果
                if (retryAndFollowUpInterceptor.isCanceled()) {
                    signalledCallback = true;
                    responseCallback.onFailure(RealCall.this, new IOException("Canceled"));
                } else {
                    signalledCallback = true;
                    responseCallback.onResponse(RealCall.this, response);   //回调结果
                }
            } catch (IOException e) {
                if (signalledCallback) {
                    // Do not signal the callback twice!
                    Platform.get().log(INFO, "Callback failure for " + toLoggableString(), e);
                } else {
                    responseCallback.onFailure(RealCall.this, e);   //回调结果
                }
            } finally {
                client.dispatcher().finished(this);
            }
        }
    }

    /**
     * Returns a string that describes this call. Doesn't include a full URL as that might contain
     * sensitive information.
     */
    private String toLoggableString() {
        String string = retryAndFollowUpInterceptor.isCanceled() ? "canceled call" : "call";
        return string + " to " + redactedUrl();
    }

    HttpUrl redactedUrl() {
        return originalRequest.url().resolve("/...");
    }

    /**
     * 拦截链开始执行！
     *
     * @return
     * @throws IOException
     */
    private Response getResponseWithInterceptorChain() throws IOException {
        // Build a full stack of interceptors.
        //添加内置的拦截器
        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.addAll(client.interceptors());     //我们添加的拦截器，在开头
        interceptors.add(retryAndFollowUpInterceptor);
        interceptors.add(new BridgeInterceptor(client.cookieJar()));
        interceptors.add(new CacheInterceptor(client.internalCache()));     //去缓存找下有没有
        interceptors.add(new ConnectInterceptor(client));       //需要建立连接了
        if (!retryAndFollowUpInterceptor.isForWebSocket()) {
            interceptors.addAll(client.networkInterceptors());
        }
        interceptors.add(new CallServerInterceptor(
                retryAndFollowUpInterceptor.isForWebSocket())); //最后一个拦截，负责发出请求！

        Interceptor.Chain chain = new RealInterceptorChain(
                interceptors, null, null, null, 0, originalRequest);    //最终的执行者
        return chain.proceed(originalRequest);
    }
}
