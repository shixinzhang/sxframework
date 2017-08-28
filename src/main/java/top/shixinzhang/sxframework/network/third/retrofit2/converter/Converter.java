package top.shixinzhang.sxframework.network.third.retrofit2.converter;

import android.support.annotation.Nullable;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import top.shixinzhang.sxframework.network.third.okhttp3.RequestBody;
import top.shixinzhang.sxframework.network.third.okhttp3.ResponseBody;
import top.shixinzhang.sxframework.network.third.retrofit2.http.*;
import top.shixinzhang.sxframework.network.third.retrofit2.request.Retrofit;

/**
 * 从 F 到 T 的转换
 * <p>
 * Convert objects to and from their representation in HTTP. Instances are created by {@linkplain
 * Factory a factory} which is Retrofit.Builder#addConverterFactory(Factory) installed
 * into the {@link Retrofit} instance.
 */
public interface Converter<F, T> {
    @Nullable
    T convert(F value) throws IOException;

    /**
     * 提供转换器的工厂
     * <p>
     * Creates {@link Converter} instances based on a type and target usage.
     */
    abstract class Factory {
        /**
         * Returns a {@link Converter} for converting an HTTP response body to {@code type}, or null if
         * {@code type} cannot be handled by this factory. This is used to create converters for
         * response types such as {@code SimpleResponse} from a {@code Call<SimpleResponse>}
         * declaration.
         */
        @Nullable
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                                Retrofit retrofit) {
            return null;
        }

        /**
         * Returns a {@link Converter} for converting {@code type} to an HTTP request body, or null if
         * {@code type} cannot be handled by this factory. This is used to create converters for types
         * specified by {@link Body @Body}, {@link Part @Part}, and {@link PartMap @PartMap}
         * values.
         */
        @Nullable
        public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                              Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
            return null;
        }

        /**
         * 这里用于对Field、FieldMap、Header、Path、Query、QueryMap注解的处理
         * Retrofit 对于上面的几个注解默认使用的是调用toString方法
         * <p>
         * Returns a {@link Converter} for converting {@code type} to a {@link String}, or null if
         * {@code type} cannot be handled by this factory. This is used to create converters for types
         * specified by {@link Field @Field}, {@link FieldMap @FieldMap} values,
         * {@link Header @Header}, {@link HeaderMap @HeaderMap}, {@link Path @Path},
         * {@link Query @Query}, and {@link QueryMap @QueryMap} values.
         */
        @Nullable
        public Converter<?, String> stringConverter(Type type, Annotation[] annotations,
                                                    Retrofit retrofit) {
            return null;
        }
    }
}
