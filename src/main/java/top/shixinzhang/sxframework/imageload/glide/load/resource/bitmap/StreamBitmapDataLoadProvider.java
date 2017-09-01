package top.shixinzhang.sxframework.imageload.glide.load.resource.bitmap;

import android.graphics.Bitmap;

import top.shixinzhang.sxframework.imageload.glide.load.DecodeFormat;
import top.shixinzhang.sxframework.imageload.glide.load.Encoder;
import top.shixinzhang.sxframework.imageload.glide.load.ResourceDecoder;
import top.shixinzhang.sxframework.imageload.glide.load.ResourceEncoder;
import top.shixinzhang.sxframework.imageload.glide.load.engine.bitmap_recycle.BitmapPool;
import top.shixinzhang.sxframework.imageload.glide.load.model.StreamEncoder;
import top.shixinzhang.sxframework.imageload.glide.load.resource.file.FileToStreamDecoder;
import top.shixinzhang.sxframework.imageload.glide.provider.DataLoadProvider;

import java.io.File;
import java.io.InputStream;

/**
 * An {@link top.shixinzhang.sxframework.imageload.glide.provider.DataLoadProvider} that provides decoders and encoders for decoding and caching
 * {@link Bitmap}s using {@link InputStream} data.
 */
public class StreamBitmapDataLoadProvider implements DataLoadProvider<InputStream, Bitmap> {
    private final StreamBitmapDecoder decoder;
    private final BitmapEncoder encoder;
    private final StreamEncoder sourceEncoder;
    private final FileToStreamDecoder<Bitmap> cacheDecoder;

    public StreamBitmapDataLoadProvider(BitmapPool bitmapPool, DecodeFormat decodeFormat) {
        sourceEncoder = new StreamEncoder();
        decoder = new StreamBitmapDecoder(bitmapPool, decodeFormat);
        encoder = new BitmapEncoder();
        cacheDecoder = new FileToStreamDecoder<Bitmap>(decoder);
    }

    @Override
    public ResourceDecoder<File, Bitmap> getCacheDecoder() {
        return cacheDecoder;
    }

    @Override
    public ResourceDecoder<InputStream, Bitmap> getSourceDecoder() {
        return decoder;
    }

    @Override
    public Encoder<InputStream> getSourceEncoder() {
        return sourceEncoder;
    }

    @Override
    public ResourceEncoder<Bitmap> getEncoder() {
        return encoder;
    }
}
