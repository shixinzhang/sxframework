package top.shixinzhang.sxframework.network.third.retrofit2.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import top.shixinzhang.sxframework.network.third.okhttp3.MediaType;
import top.shixinzhang.sxframework.network.third.okhttp3.Request;
import top.shixinzhang.sxframework.network.third.okhttp3.ResponseBody;

/**
 * 使用 OkHttp 实现的请求
 * @param <T>
 */
final class OkHttpCall<T> implements Call<T> {
    private final ServiceMethod<T> serviceMethod;
    private final Object[] args;

    private volatile boolean canceled;

    // All guarded by this.
    private top.shixinzhang.sxframework.network.third.okhttp3.Call rawCall;
    private Throwable creationFailure; // Either a RuntimeException or IOException.
    private boolean executed;

    OkHttpCall(ServiceMethod<T> serviceMethod, Object[] args) {
        this.serviceMethod = serviceMethod;
        this.args = args;
    }

    @NonNull
    @SuppressWarnings("CloneDoesntCallSuperClone")
    // We are a final type & this saves clearing state.
    @Override
    public OkHttpCall<T> clone() {
        return new OkHttpCall<>(serviceMethod, args);
    }

    @Override
    public synchronized Request request() {
        top.shixinzhang.sxframework.network.third.okhttp3.Call call = rawCall;
        if (call != null) {
            return call.request();
        }
        if (creationFailure != null) {
            if (creationFailure instanceof IOException) {
                throw new RuntimeException("Unable to create request.", creationFailure);
            } else {
                throw (RuntimeException) creationFailure;
            }
        }
        try {
            return (rawCall = createRawCall()).request();
        } catch (RuntimeException e) {
            creationFailure = e;
            throw e;
        } catch (IOException e) {
            creationFailure = e;
            throw new RuntimeException("Unable to create request.", e);
        }
    }

    @Override
    public void enqueue(@Nullable final Callback<T> callback) {
        if (callback == null) throw new NullPointerException("callback == null");

        top.shixinzhang.sxframework.network.third.okhttp3.Call call;
        Throwable failure;

        synchronized (this) {
            if (executed) throw new IllegalStateException("Already executed.");
            executed = true;

            call = rawCall;
            failure = creationFailure;
            if (call == null && failure == null) {
                try {
                    call = rawCall = createRawCall();
                } catch (Throwable t) {
                    failure = creationFailure = t;
                }
            }
        }

        if (failure != null) {
            callback.onFailure(this, failure);
            return;
        }

        if (canceled) {
            call.cancel();
        }

        //请求入队，异步执行
        call.enqueue(new top.shixinzhang.sxframework.network.third.okhttp3.Callback() {
            @Override
            public void onResponse(top.shixinzhang.sxframework.network.third.okhttp3.Call call, @NonNull top.shixinzhang.sxframework.network.third.okhttp3.Response rawResponse) {
                Response<T> response;
                try {
                    response = parseResponse(rawResponse);
                } catch (Throwable e) {
                    callFailure(e);
                    return;
                }
                callSuccess(response);
            }

            @Override
            public void onFailure(top.shixinzhang.sxframework.network.third.okhttp3.Call call, IOException e) {
                try {
                    callback.onFailure(OkHttpCall.this, e);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

            private void callFailure(Throwable e) {
                try {
                    callback.onFailure(OkHttpCall.this, e);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

            private void callSuccess(Response<T> response) {
                try {
                    callback.onResponse(OkHttpCall.this, response);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        });
    }

    @Override
    public synchronized boolean isExecuted() {
        return executed;
    }

    @Nullable
    @Override
    public Response<T> execute() throws IOException {
        top.shixinzhang.sxframework.network.third.okhttp3.Call call;

        synchronized (this) {
            if (executed) throw new IllegalStateException("Already executed.");
            executed = true;

            if (creationFailure != null) {
                if (creationFailure instanceof IOException) {
                    throw (IOException) creationFailure;
                } else {
                    throw (RuntimeException) creationFailure;
                }
            }

            call = rawCall;
            if (call == null) {
                try {
                    call = rawCall = createRawCall();
                } catch (@NonNull IOException | RuntimeException e) {
                    creationFailure = e;
                    throw e;
                }
            }
        }

        if (canceled) {
            call.cancel();
        }

        return parseResponse(call.execute());
    }

    private top.shixinzhang.sxframework.network.third.okhttp3.Call createRawCall() throws IOException {
        Request request = serviceMethod.toRequest(args);
        top.shixinzhang.sxframework.network.third.okhttp3.Call call = serviceMethod.callFactory.newCall(request);
        if (call == null) {
            throw new NullPointerException("Call.Factory returned null.");
        }
        return call;
    }

    /**
     * 解析结果
     * @param rawResponse
     * @return
     * @throws IOException
     */
    @Nullable
    Response<T> parseResponse(@NonNull top.shixinzhang.sxframework.network.third.okhttp3.Response rawResponse) throws IOException {
        ResponseBody rawBody = rawResponse.body();

        // Remove the body's source (the only stateful object) so we can pass the response along.
        rawResponse = rawResponse.newBuilder()
                .body(new NoContentResponseBody(rawBody.contentType(), rawBody.contentLength()))
                .build();

        int code = rawResponse.code();
        if (code < 200 || code >= 300) {    //执行错误
            try {
                // Buffer the entire body to avoid future I/O.
                ResponseBody bufferedBody = Utils.buffer(rawBody);
                return Response.error(bufferedBody, rawResponse);
            } finally {
                rawBody.close();
            }
        }

        if (code == 204 || code == 205) {   //请求执行成功，但是没有数据
            return Response.success(null, rawResponse);
        }

        ExceptionCatchingRequestBody catchingBody = new ExceptionCatchingRequestBody(rawBody);
        try {
            //交给上层解析
            T body = serviceMethod.toResponse(catchingBody);
            return Response.success(body, rawResponse);
        } catch (RuntimeException e) {
            // If the underlying source threw an exception, propagate that rather than indicating it was
            // a runtime exception.
            catchingBody.throwIfCaught();
            throw e;
        }
    }

    public void cancel() {
        canceled = true;

        top.shixinzhang.sxframework.network.third.okhttp3.Call call;
        synchronized (this) {
            call = rawCall;
        }
        if (call != null) {
            call.cancel();
        }
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    static final class NoContentResponseBody extends ResponseBody {
        private final MediaType contentType;
        private final long contentLength;

        NoContentResponseBody(MediaType contentType, long contentLength) {
            this.contentType = contentType;
            this.contentLength = contentLength;
        }

        @Override
        public MediaType contentType() {
            return contentType;
        }

        @Override
        public long contentLength() {
            return contentLength;
        }

        @NonNull
        @Override
        public BufferedSource source() {
            throw new IllegalStateException("Cannot read raw response body of a converted body.");
        }
    }

    static final class ExceptionCatchingRequestBody extends ResponseBody {
        private final ResponseBody delegate;
        IOException thrownException;

        ExceptionCatchingRequestBody(ResponseBody delegate) {
            this.delegate = delegate;
        }

        @Override
        public MediaType contentType() {
            return delegate.contentType();
        }

        @Override
        public long contentLength() {
            return delegate.contentLength();
        }

        @Override
        public BufferedSource source() {
            return Okio.buffer(new ForwardingSource(delegate.source()) {
                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    try {
                        return super.read(sink, byteCount);
                    } catch (IOException e) {
                        thrownException = e;
                        throw e;
                    }
                }
            });
        }

        @Override
        public void close() {
            delegate.close();
        }

        void throwIfCaught() throws IOException {
            if (thrownException != null) {
                throw thrownException;
            }
        }
    }
}
