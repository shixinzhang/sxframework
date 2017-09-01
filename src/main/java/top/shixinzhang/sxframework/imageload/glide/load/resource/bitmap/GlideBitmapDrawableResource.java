package top.shixinzhang.sxframework.imageload.glide.load.resource.bitmap;

import top.shixinzhang.sxframework.imageload.glide.load.engine.bitmap_recycle.BitmapPool;
import top.shixinzhang.sxframework.imageload.glide.load.resource.drawable.DrawableResource;
import top.shixinzhang.sxframework.imageload.glide.util.Util;

/**
 * A resource wrapper for {@link top.shixinzhang.sxframework.imageload.glide.load.resource.bitmap.GlideBitmapDrawable}.
 */
public class GlideBitmapDrawableResource extends DrawableResource<GlideBitmapDrawable> {
    private final BitmapPool bitmapPool;

    public GlideBitmapDrawableResource(GlideBitmapDrawable drawable, BitmapPool bitmapPool) {
        super(drawable);
        this.bitmapPool = bitmapPool;
    }

    @Override
    public int getSize() {
        return Util.getBitmapByteSize(drawable.getBitmap());
    }

    @Override
    public void recycle() {
        bitmapPool.put(drawable.getBitmap());
    }
}
