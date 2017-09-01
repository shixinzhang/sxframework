package top.shixinzhang.sxframework.imageload.glide.load.model.stream;

import android.content.Context;
import android.net.Uri;

import top.shixinzhang.sxframework.imageload.glide.Glide;
import top.shixinzhang.sxframework.imageload.glide.load.model.FileLoader;
import top.shixinzhang.sxframework.imageload.glide.load.model.GenericLoaderFactory;
import top.shixinzhang.sxframework.imageload.glide.load.model.ModelLoader;
import top.shixinzhang.sxframework.imageload.glide.load.model.ModelLoaderFactory;

import java.io.File;
import java.io.InputStream;

/**
 * A {@link ModelLoader} For translating {@link File} models for local uris into {@link InputStream} data.
 */
public class StreamFileLoader extends FileLoader<InputStream> implements StreamModelLoader<File> {

    /**
     * The default factory for {@link top.shixinzhang.sxframework.imageload.glide.load.model.stream.StreamFileLoader}s.
     */
    public static class Factory implements ModelLoaderFactory<File, InputStream> {
        @Override
        public ModelLoader<File, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new StreamFileLoader(factories.buildModelLoader(Uri.class, InputStream.class));
        }

        @Override
        public void teardown() {
            // Do nothing.
        }
    }

    public StreamFileLoader(Context context) {
        this(Glide.buildStreamModelLoader(Uri.class, context));
    }

    public StreamFileLoader(ModelLoader<Uri, InputStream> uriLoader) {
        super(uriLoader);
    }

}
