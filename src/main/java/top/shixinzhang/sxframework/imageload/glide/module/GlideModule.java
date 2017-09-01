package top.shixinzhang.sxframework.imageload.glide.module;

import android.content.Context;

import top.shixinzhang.sxframework.imageload.glide.Glide;
import top.shixinzhang.sxframework.imageload.glide.GlideBuilder;

/**
 * An interface allowing lazy configuration of Glide including setting options using
 * {@link top.shixinzhang.sxframework.imageload.glide.GlideBuilder} and registering
 * {@link top.shixinzhang.sxframework.imageload.glide.load.model.ModelLoader ModelLoaders}.
 *
 * <p>
 *     To use this interface:
 *     <ol>
 *         <li>
 *             Implement the GlideModule interface in a class with public visibility, calling
 *             {@link top.shixinzhang.sxframework.imageload.glide.Glide#register(Class, Class, top.shixinzhang.sxframework.imageload.glide.load.model.ModelLoaderFactory)}
 *             for each {@link top.shixinzhang.sxframework.imageload.glide.load.model.ModelLoader} you'd like to register:
 *             <pre>
 *                  <code>
 *                      public class FlickrGlideModule implements GlideModule {
 *                          {@literal @}Override
 *                          public void applyOptions(Context context, GlideBuilder builder) {
 *                              buidler.setDecodeFormat(DecodeFormat.ALWAYS_ARGB_8888);
 *                          }
 *
 *                          {@literal @}Override
 *                          public void registerComponents(Context context, Glide glide) {
 *                              glide.register(Model.class, Data.class, new MyModelLoader());
 *                          }
 *                      }
 *                  </code>
 *             </pre>
 *         </li>
 *         <li>
 *              Add your implementation to your list of keeps in your proguard.cfg file:
 *              <pre>
 *                  {@code
 *                      -keepnames class * top.shixinzhang.sxframework.imageload.glide.samples.flickr.FlickrGlideModule
 *                  }
 *              </pre>
 *         </li>
 *         <li>
 *             Add a metadata tag to your AndroidManifest.xml with your GlideModule implementation's fully qualified
 *             classname as the key, and {@code GlideModule} as the value:
 *             <pre>
 *                 {@code
 *                      <meta-data
 *                          android:name="top.shixinzhang.sxframework.imageload.glide.samples.flickr.FlickrGlideModule"
 *                          android:value="GlideModule" />
 *                 }
 *             </pre>
 *         </li>
 *     </ol>
 * </p>
 *
 * <p>
 *     All implementations must be publicly visible and contain only an empty constructor so they can be instantiated
 *     via reflection when Glide is lazily initialized.
 * </p>
 *
 * <p>
 *     There is no defined order in which modules are called, so projects should be careful to avoid applying
 *     conflicting settings in different modules. If an application depends on libraries that have conflicting
 *     modules, the application should consider avoiding the library modules and instead providing their required
 *     dependencies in a single application module.
 * </p>
 */
public interface GlideModule {

    /**
     * Lazily apply options to a {@link top.shixinzhang.sxframework.imageload.glide.GlideBuilder} immediately before the Glide singleton is
     * created.
     *
     * <p>
     *     This method will be called once and only once per implementation.
     * </p>
     *
     * @param context An Application {@link Context}.
     * @param builder The {@link top.shixinzhang.sxframework.imageload.glide.GlideBuilder} that will be used to create Glide.
     */
    void applyOptions(Context context, GlideBuilder builder);

    /**
     * Lazily register components immediately after the Glide singleton is created but before any requests can be
     * started.
     *
     * <p>
     *     This method will be called once and only once per implementation.
     * </p>
     *
     * @param context An Application {@link Context}.
     * @param glide The newly created Glide singleton.
     */
    void registerComponents(Context context, Glide glide);
}
