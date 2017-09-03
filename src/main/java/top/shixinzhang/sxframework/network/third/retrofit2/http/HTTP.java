package top.shixinzhang.sxframework.network.third.retrofit2.http;

import android.support.annotation.NonNull;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import top.shixinzhang.sxframework.network.third.okhttp3.HttpUrl;
import top.shixinzhang.sxframework.network.third.retrofit2.request.Retrofit;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Use a custom HTTP verb for a request.
 * <pre><code>
 * interface Service {
 *   &#064;HTTP(method = "CUSTOM", path = "custom/endpoint/")
 *   Call&lt;ResponseBody&gt; customEndpoint();
 * }
 * </code></pre>
 * This annotation can also used for sending {@code DELETE} with a request body:
 * <pre><code>
 * interface Service {
 *   &#064;HTTP(method = "DELETE", path = "remove/", hasBody = true)
 *   Call&lt;ResponseBody&gt; deleteObject(@Body RequestBody object);
 * }
 * </code></pre>
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface HTTP {
  @NonNull String method();
  /**
   * A relative or absolute path, or full URL of the endpoint. This value is optional if the first
   * parameter of the method is annotated with {@link Url @Url}.
   * <p>
   * See {@linkplain Retrofit.Builder#baseUrl(HttpUrl) base URL} for details of how
   * this is resolved against a base URL to create the full endpoint URL.
   */
  @NonNull String path() default "";
  boolean hasBody() default false;
}
