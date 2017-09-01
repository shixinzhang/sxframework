package top.shixinzhang.sxframework.imageload.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.os.ParcelFileDescriptor;

import top.shixinzhang.sxframework.imageload.glide.load.DecodeFormat;
import top.shixinzhang.sxframework.imageload.glide.load.Encoder;
import top.shixinzhang.sxframework.imageload.glide.load.ResourceDecoder;
import top.shixinzhang.sxframework.imageload.glide.load.ResourceEncoder;
import top.shixinzhang.sxframework.imageload.glide.load.engine.bitmap_recycle.BitmapPool;
import top.shixinzhang.sxframework.imageload.glide.load.resource.NullEncoder;
import top.shixinzhang.sxframework.imageload.glide.load.resource.file.FileToStreamDecoder;
import top.shixinzhang.sxframework.imageload.glide.provider.DataLoadProvider;

import java.io.File;

/**
 * An {@link top.shixinzhang.sxframework.imageload.glide.provider.DataLoadProvider} that provides classes for decoding and encoding
 * {@link Bitmap}s from {@link ParcelFileDescriptor} data.
 */
public class FileDescriptorBitmapDataLoadProvider implements DataLoadProvider<ParcelFileDescriptor, Bitmap> {
    private final ResourceDecoder<File, Bitmap> cacheDecoder;
    private final FileDescriptorBitmapDecoder sourceDecoder;
    private final BitmapEncoder encoder;
    private final Encoder<ParcelFileDescriptor> sourceEncoder;

    public FileDescriptorBitmapDataLoadProvider(BitmapPool bitmapPool, DecodeFormat decodeFormat) {
        cacheDecoder = new FileToStreamDecoder<Bitmap>(new StreamBitmapDecoder(bitmapPool, decodeFormat));
        sourceDecoder = new FileDescriptorBitmapDecoder(bitmapPool, decodeFormat);
        encoder = new BitmapEncoder();
        sourceEncoder = NullEncoder.get();
    }

    @Override
    public ResourceDecoder<File, Bitmap> getCacheDecoder() {
        return cacheDecoder;
    }

    @Override
    public ResourceDecoder<ParcelFileDescriptor, Bitmap> getSourceDecoder() {
        return sourceDecoder;
    }

    @Override
    public Encoder<ParcelFileDescriptor> getSourceEncoder() {
        return sourceEncoder;
    }

    @Override
    public ResourceEncoder<Bitmap> getEncoder() {
        return encoder;
    }
}
