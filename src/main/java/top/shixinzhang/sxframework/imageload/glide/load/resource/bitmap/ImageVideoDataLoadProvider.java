package top.shixinzhang.sxframework.imageload.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.os.ParcelFileDescriptor;

import top.shixinzhang.sxframework.imageload.glide.load.Encoder;
import top.shixinzhang.sxframework.imageload.glide.load.ResourceDecoder;
import top.shixinzhang.sxframework.imageload.glide.load.ResourceEncoder;
import top.shixinzhang.sxframework.imageload.glide.load.model.ImageVideoWrapper;
import top.shixinzhang.sxframework.imageload.glide.load.model.ImageVideoWrapperEncoder;
import top.shixinzhang.sxframework.imageload.glide.provider.DataLoadProvider;

import java.io.File;
import java.io.InputStream;

/**
 * A {@link top.shixinzhang.sxframework.imageload.glide.provider.DataLoadProvider} for loading either an {@link InputStream} or an
 * {@link ParcelFileDescriptor} as an {@link Bitmap}.
 */
public class ImageVideoDataLoadProvider implements DataLoadProvider<ImageVideoWrapper, Bitmap> {
    private final ImageVideoBitmapDecoder sourceDecoder;
    private final ResourceDecoder<File, Bitmap> cacheDecoder;
    private final ResourceEncoder<Bitmap> encoder;
    private final ImageVideoWrapperEncoder sourceEncoder;

    public ImageVideoDataLoadProvider(DataLoadProvider<InputStream, Bitmap> streamBitmapProvider,
            DataLoadProvider<ParcelFileDescriptor, Bitmap> fileDescriptorBitmapProvider) {
        encoder = streamBitmapProvider.getEncoder();
        sourceEncoder = new ImageVideoWrapperEncoder(streamBitmapProvider.getSourceEncoder(),
                fileDescriptorBitmapProvider.getSourceEncoder());
        cacheDecoder = streamBitmapProvider.getCacheDecoder();
        sourceDecoder = new ImageVideoBitmapDecoder(streamBitmapProvider.getSourceDecoder(),
                fileDescriptorBitmapProvider.getSourceDecoder());
    }

    @Override
    public ResourceDecoder<File, Bitmap> getCacheDecoder() {
        return cacheDecoder;
    }

    @Override
    public ResourceDecoder<ImageVideoWrapper, Bitmap> getSourceDecoder() {
        return sourceDecoder;
    }

    @Override
    public Encoder<ImageVideoWrapper> getSourceEncoder() {
        return sourceEncoder;
    }

    @Override
    public ResourceEncoder<Bitmap> getEncoder() {
        return encoder;
    }
}
