package top.shixinzhang.sxframework.network.third.retrofit2.converter;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import top.shixinzhang.sxframework.network.third.okhttp3.RequestBody;
import top.shixinzhang.sxframework.network.third.okhttp3.ResponseBody;
import top.shixinzhang.sxframework.network.third.retrofit2.http.Streaming;
import top.shixinzhang.sxframework.network.third.retrofit2.request.Retrofit;
import top.shixinzhang.sxframework.network.third.retrofit2.request.Utils;

public final class BuiltInConverters extends Converter.Factory {
  @Override
  public Converter<ResponseBody, ?> responseBodyConverter(Type type, @NonNull Annotation[] annotations,
                                                          Retrofit retrofit) {
    if (type == ResponseBody.class) {
      if (Utils.isAnnotationPresent(annotations, Streaming.class)) {
        return StreamingResponseBodyConverter.INSTANCE;
      }
      return BufferingResponseBodyConverter.INSTANCE;
    }
    if (type == Void.class) {
      return VoidResponseBodyConverter.INSTANCE;
    }
    return null;
  }

  @Override
  public Converter<?, RequestBody> requestBodyConverter(Type type,
      Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
    if (RequestBody.class.isAssignableFrom(Utils.getRawType(type))) {
      return RequestBodyConverter.INSTANCE;
    }
    return null;
  }

  @Override public Converter<?, String> stringConverter(Type type, Annotation[] annotations,
      Retrofit retrofit) {
    if (type == String.class) {
      return StringConverter.INSTANCE;
    }
    return null;
  }

  static final class StringConverter implements Converter<String, String> {
    static final StringConverter INSTANCE = new StringConverter();

    @Override public String convert(String value) throws IOException {
      return value;
    }
  }

  static final class VoidResponseBodyConverter implements Converter<ResponseBody, Void> {
    static final VoidResponseBodyConverter INSTANCE = new VoidResponseBodyConverter();

    @Override public Void convert(@NonNull ResponseBody value) throws IOException {
      value.close();
      return null;
    }
  }

  static final class RequestBodyConverter implements Converter<RequestBody, RequestBody> {
    static final RequestBodyConverter INSTANCE = new RequestBodyConverter();

    @Override public RequestBody convert(RequestBody value) throws IOException {
      return value;
    }
  }

  static final class StreamingResponseBodyConverter
      implements Converter<ResponseBody, ResponseBody> {
    static final StreamingResponseBodyConverter INSTANCE = new StreamingResponseBodyConverter();

    @Override public ResponseBody convert(ResponseBody value) throws IOException {
      return value;
    }
  }

  static final class BufferingResponseBodyConverter
      implements Converter<ResponseBody, ResponseBody> {
    static final BufferingResponseBodyConverter INSTANCE = new BufferingResponseBodyConverter();

    @Override public ResponseBody convert(@NonNull ResponseBody value) throws IOException {
      try {
        // Buffer the entire body to avoid future I/O.
        return Utils.buffer(value);
      } finally {
        value.close();
      }
    }
  }

  public static final class ToStringConverter implements Converter<Object, String> {
    public static final ToStringConverter INSTANCE = new ToStringConverter();

    @Override public String convert(@NonNull Object value) {
      return value.toString();
    }
  }
}
