package top.shixinzhang.sxframework.network.third.retrofit2.adapter;

import android.support.annotation.Nullable;

import java.io.IOException;

import top.shixinzhang.sxframework.network.third.retrofit2.request.Response;

/** The result of executing an HTTP request. */
public final class Result<T> {
  @Nullable
  public static <T> Result<T> error(@Nullable Throwable error) {
    if (error == null) throw new NullPointerException("error == null");
    return new Result<>(null, error);
  }

  @Nullable
  public static <T> Result<T> response(@Nullable Response<T> response) {
    if (response == null) throw new NullPointerException("response == null");
    return new Result<>(response, null);
  }

  private final Response<T> response;
  private final Throwable error;

  private Result(Response<T> response, Throwable error) {
    this.response = response;
    this.error = error;
  }

  /**
   * The response received from executing an HTTP request. Only present when {@link #isError()} is
   * false, null otherwise.
   */
  public Response<T> response() {
    return response;
  }

  /**
   * The error experienced while attempting to execute an HTTP request. Only present when {@link
   * #isError()} is true, null otherwise.
   * <p>
   * If the error is an {@link IOException} then there was a problem with the transport to the
   * remote server. Any other exception type indicates an unexpected failure and should be
   * considered fatal (configuration error, programming error, etc.).
   */
  public Throwable error() {
    return error;
  }

  /** {@code true} if the request resulted in an error. See {@link #error()} for the cause. */
  public boolean isError() {
    return error != null;
  }
}
