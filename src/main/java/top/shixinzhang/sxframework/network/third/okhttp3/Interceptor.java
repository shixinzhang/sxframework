package top.shixinzhang.sxframework.network.third.okhttp3;

import java.io.IOException;

/**
 * 观察、修改、简化出去的请求和进来的结果
 * <p>
 * 比如添加、删除、转换请求/响应的 请求头
 * <p>
 * Observes, modifies, and potentially short-circuits requests going out and the corresponding
 * responses coming back in. Typically interceptors add, remove, or transform headers on the request
 * or response.
 */
public interface Interceptor {
    Response intercept(Chain chain) throws IOException;

    interface Chain {
        Request request();

        Response proceed(Request request) throws IOException;

        Connection connection();
    }
}
