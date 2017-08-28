package top.shixinzhang.sxframework.network.third.okhttp3.internal.http;

import okio.BufferedSource;
import top.shixinzhang.sxframework.network.third.okhttp3.Headers;
import top.shixinzhang.sxframework.network.third.okhttp3.MediaType;
import top.shixinzhang.sxframework.network.third.okhttp3.ResponseBody;

public final class RealResponseBody extends ResponseBody {
  private final Headers headers;
  private final BufferedSource source;

  public RealResponseBody(Headers headers, BufferedSource source) {
    this.headers = headers;
    this.source = source;
  }

  @Override public MediaType contentType() {
    String contentType = headers.get("Content-Type");
    return contentType != null ? MediaType.parse(contentType) : null;
  }

  @Override public long contentLength() {
    return HttpHeaders.contentLength(headers);
  }

  @Override public BufferedSource source() {
    return source;
  }
}
