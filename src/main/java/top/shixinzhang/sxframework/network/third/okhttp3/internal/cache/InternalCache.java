package top.shixinzhang.sxframework.network.third.okhttp3.internal.cache;

import java.io.IOException;

import top.shixinzhang.sxframework.network.third.okhttp3.Request;
import top.shixinzhang.sxframework.network.third.okhttp3.Response;

/**
 * OkHttp's internal cache interface. Applications shouldn't implement this: instead use {@link
 * top.shixinzhang.sxframework.network.third.okhttp3.Cache}.
 */
public interface InternalCache {
  Response get(Request request) throws IOException;

  CacheRequest put(Response response) throws IOException;

  /**
   * Remove any cache entries for the supplied {@code request}. This is invoked when the client
   * invalidates the cache, such as when making POST requests.
   */
  void remove(Request request) throws IOException;

  /**
   * Handles a conditional request hit by updating the stored cache response with the headers from
   * {@code network}. The cached response body is not updated. If the stored response has changed
   * since {@code cached} was returned, this does nothing.
   */
  void update(Response cached, Response network);

  /** Track an conditional GET that was satisfied by this cache. */
  void trackConditionalCacheHit();

  /** Track an HTTP response being satisfied with {@code cacheStrategy}. */
  void trackResponse(CacheStrategy cacheStrategy);
}
