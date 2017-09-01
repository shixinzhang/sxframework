package top.shixinzhang.sxframework.imageload.glide.load.resource.transcode;

import android.graphics.Bitmap;

import top.shixinzhang.sxframework.imageload.glide.load.engine.Resource;
import top.shixinzhang.sxframework.imageload.glide.load.resource.bitmap.GlideBitmapDrawable;
import top.shixinzhang.sxframework.imageload.glide.load.resource.drawable.GlideDrawable;
import top.shixinzhang.sxframework.imageload.glide.load.resource.gifbitmap.GifBitmapWrapper;

/**
 * An {@link top.shixinzhang.sxframework.imageload.glide.load.resource.transcode.ResourceTranscoder} that can transcode either an
 * {@link Bitmap} or an {@link top.shixinzhang.sxframework.imageload.glide.load.resource.gif.GifDrawable} into an
 * {@link android.graphics.drawable.Drawable}.
 */
public class GifBitmapWrapperDrawableTranscoder implements ResourceTranscoder<GifBitmapWrapper, GlideDrawable> {
    private final ResourceTranscoder<Bitmap, GlideBitmapDrawable> bitmapDrawableResourceTranscoder;

    public GifBitmapWrapperDrawableTranscoder(
            ResourceTranscoder<Bitmap, GlideBitmapDrawable> bitmapDrawableResourceTranscoder) {
        this.bitmapDrawableResourceTranscoder = bitmapDrawableResourceTranscoder;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Resource<GlideDrawable> transcode(Resource<GifBitmapWrapper> toTranscode) {
        GifBitmapWrapper gifBitmap = toTranscode.get();
        Resource<Bitmap> bitmapResource = gifBitmap.getBitmapResource();

        final Resource<? extends GlideDrawable> result;
        if (bitmapResource != null) {
            result = bitmapDrawableResourceTranscoder.transcode(bitmapResource);
        } else {
            result = gifBitmap.getGifResource();
        }
        // This is unchecked but always safe, anything that extends a Drawable can be safely cast to a Drawable.
        return (Resource<GlideDrawable>) result;
    }

    @Override
    public String getId() {
        return "GifBitmapWrapperDrawableTranscoder.top.shixinzhang.sxframework.imageload.glide.load.resource.transcode";
    }
}
