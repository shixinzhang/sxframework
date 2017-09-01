package top.shixinzhang.sxframework.imageload.glide.load.model.file_descriptor;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import top.shixinzhang.sxframework.imageload.glide.Glide;
import top.shixinzhang.sxframework.imageload.glide.load.data.DataFetcher;
import top.shixinzhang.sxframework.imageload.glide.load.data.FileDescriptorAssetPathFetcher;
import top.shixinzhang.sxframework.imageload.glide.load.data.FileDescriptorLocalUriFetcher;
import top.shixinzhang.sxframework.imageload.glide.load.model.GenericLoaderFactory;
import top.shixinzhang.sxframework.imageload.glide.load.model.GlideUrl;
import top.shixinzhang.sxframework.imageload.glide.load.model.ModelLoader;
import top.shixinzhang.sxframework.imageload.glide.load.model.ModelLoaderFactory;
import top.shixinzhang.sxframework.imageload.glide.load.model.UriLoader;

/**
 * A {@link ModelLoader} For translating {@link Uri} models for local uris into {@link ParcelFileDescriptor} data.
 */
public class FileDescriptorUriLoader extends UriLoader<ParcelFileDescriptor> implements FileDescriptorModelLoader<Uri> {

    /**
     * The default factory for {@link top.shixinzhang.sxframework.imageload.glide.load.model.file_descriptor.FileDescriptorUriLoader}s.
     */
    public static class Factory implements ModelLoaderFactory<Uri, ParcelFileDescriptor> {
        @Override
        public ModelLoader<Uri, ParcelFileDescriptor> build(Context context, GenericLoaderFactory factories) {
            return new FileDescriptorUriLoader(context, factories.buildModelLoader(GlideUrl.class,
                    ParcelFileDescriptor.class));
        }

        @Override
        public void teardown() {
            // Do nothing.
        }
    }

    public FileDescriptorUriLoader(Context context) {
        this(context, Glide.buildFileDescriptorModelLoader(GlideUrl.class, context));
    }

    public FileDescriptorUriLoader(Context context, ModelLoader<GlideUrl, ParcelFileDescriptor> urlLoader) {
        super(context, urlLoader);
    }

    @Override
    protected DataFetcher<ParcelFileDescriptor> getLocalUriFetcher(Context context, Uri uri) {
        return new FileDescriptorLocalUriFetcher(context, uri);
    }

    @Override
    protected DataFetcher<ParcelFileDescriptor> getAssetPathFetcher(Context context, String assetPath) {
        return new FileDescriptorAssetPathFetcher(context.getApplicationContext().getAssets(), assetPath);
    }
}
