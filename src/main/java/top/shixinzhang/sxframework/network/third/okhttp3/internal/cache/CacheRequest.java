package top.shixinzhang.sxframework.network.third.okhttp3.internal.cache;

import java.io.IOException;

import okio.Sink;

public interface CacheRequest {
  Sink body() throws IOException;

  void abort();
}
