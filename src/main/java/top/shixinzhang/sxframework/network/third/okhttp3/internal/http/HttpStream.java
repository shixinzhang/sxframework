package top.shixinzhang.sxframework.network.third.okhttp3.internal.http;

import java.io.IOException;

import top.shixinzhang.sxframework.network.third.okhttp3.Request;
import top.shixinzhang.sxframework.network.third.okhttp3.Response;
import top.shixinzhang.sxframework.network.third.okhttp3.ResponseBody;
import okio.Sink;

public interface HttpStream {
  /**
   * The timeout to use while discarding a stream of input data. Since this is used for connection
   * reuse, this timeout should be significantly less than the time it takes to establish a new
   * connection.
   */
  int DISCARD_STREAM_TIMEOUT_MILLIS = 100;

  /** Returns an output stream where the request body can be streamed. */
  Sink createRequestBody(Request request, long contentLength);

  /** This should update the HTTP engine's sentRequestMillis field. */
  void writeRequestHeaders(Request request) throws IOException;

  /** Flush the request to the underlying socket. */
  void finishRequest() throws IOException;

  /** Read and return response headers. */
  Response.Builder readResponseHeaders() throws IOException;

  /** Returns a stream that reads the response body. */
  ResponseBody openResponseBody(Response response) throws IOException;

  /**
   * Cancel this stream. Resources held by this stream will be cleaned up, though not synchronously.
   * That may happen later by the connection pool thread.
   */
  void cancel();
}
