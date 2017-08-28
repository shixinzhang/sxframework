package top.shixinzhang.sxframework.network.third.okhttp3.internal.framed;

import java.io.IOException;


/** Thrown when an HTTP/2 stream is canceled without damage to the socket that carries it. */
public final class StreamResetException extends IOException {
  public final ErrorCode errorCode;

  public StreamResetException(ErrorCode errorCode) {
    super("stream was reset: " + errorCode);
    this.errorCode = errorCode;
  }
}
