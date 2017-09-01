package top.shixinzhang.sxframework.imageload.glide.load.resource.transcode;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

import top.shixinzhang.sxframework.imageload.glide.Glide;
import top.shixinzhang.sxframework.imageload.glide.load.engine.Resource;
import top.shixinzhang.sxframework.imageload.glide.load.engine.bitmap_recycle.BitmapPool;
import top.shixinzhang.sxframework.imageload.glide.load.resource.bitmap.GlideBitmapDrawable;
import top.shixinzhang.sxframework.imageload.glide.load.resource.bitmap.GlideBitmapDrawableResource;

/**
 * An {@link top.shixinzhang.sxframework.imageload.glide.load.resource.transcode.ResourceTranscoder} that converts
 * {@link Bitmap}s into {@link android.graphics.drawable.BitmapDrawable}s.
 */
public class GlideBitmapDrawableTranscoder implements ResourceTranscoder<Bitmap, GlideBitmapDrawable> {
    private final Resources resources;
    private final BitmapPool bitmapPool;

    public GlideBitmapDrawableTranscoder(Context context) {
        this(context.getResources(), Glide.get(context).getBitmapPool());
    }

    public GlideBitmapDrawableTranscoder(Resources resources, BitmapPool bitmapPool) {
        this.resources = resources;
        this.bitmapPool = bitmapPool;
    }

    @Override
    public Resource<GlideBitmapDrawable> transcode(Resource<Bitmap> toTranscode) {
        GlideBitmapDrawable drawable = new GlideBitmapDrawable(resources, toTranscode.get());
        return new GlideBitmapDrawableResource(drawable, bitmapPool);
    }

    @Override
    public String getId() {
        return "GlideBitmapDrawableTranscoder.top.shixinzhang.sxframework.imageload.glide.load.resource.transcode";
    }
}
