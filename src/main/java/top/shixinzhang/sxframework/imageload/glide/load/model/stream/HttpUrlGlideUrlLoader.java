package top.shixinzhang.sxframework.imageload.glide.load.model.stream;

import android.content.Context;

import top.shixinzhang.sxframework.imageload.glide.load.data.DataFetcher;
import top.shixinzhang.sxframework.imageload.glide.load.data.HttpUrlFetcher;
import top.shixinzhang.sxframework.imageload.glide.load.model.GenericLoaderFactory;
import top.shixinzhang.sxframework.imageload.glide.load.model.GlideUrl;
import top.shixinzhang.sxframework.imageload.glide.load.model.ModelCache;
import top.shixinzhang.sxframework.imageload.glide.load.model.ModelLoader;
import top.shixinzhang.sxframework.imageload.glide.load.model.ModelLoaderFactory;

import java.io.InputStream;

/**
 * An {@link top.shixinzhang.sxframework.imageload.glide.load.model.ModelLoader} for translating {@link top.shixinzhang.sxframework.imageload.glide.load.model.GlideUrl}
 * (http/https URLS) into {@link InputStream} data.
 */
public class HttpUrlGlideUrlLoader implements ModelLoader<GlideUrl, InputStream> {

    private final ModelCache<GlideUrl, GlideUrl> modelCache;

    /**
     * The default factory for {@link top.shixinzhang.sxframework.imageload.glide.load.model.stream.HttpUrlGlideUrlLoader}s.
     */
    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
        private final ModelCache<GlideUrl, GlideUrl> modelCache = new ModelCache<GlideUrl, GlideUrl>(500);

        @Override
        public ModelLoader<GlideUrl, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new HttpUrlGlideUrlLoader(modelCache);
        }

        @Override
        public void teardown() {
            // Do nothing.
        }
    }

    public HttpUrlGlideUrlLoader() {
        this(null);
    }

    public HttpUrlGlideUrlLoader(ModelCache<GlideUrl, GlideUrl> modelCache) {
        this.modelCache = modelCache;
    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(GlideUrl model, int width, int height) {
        // GlideUrls memoize parsed URLs so caching them saves a few object instantiations and time spent parsing urls.
        GlideUrl url = model;
        if (modelCache != null) {
            url = modelCache.get(model, 0, 0);
            if (url == null) {
                modelCache.put(model, 0, 0, model);
                url = model;
            }
        }
        return new HttpUrlFetcher(url);
    }
}
