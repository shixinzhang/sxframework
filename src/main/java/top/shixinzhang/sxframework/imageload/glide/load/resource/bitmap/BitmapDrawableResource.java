package top.shixinzhang.sxframework.imageload.glide.load.resource.bitmap;

import android.graphics.drawable.BitmapDrawable;

import top.shixinzhang.sxframework.imageload.glide.load.engine.bitmap_recycle.BitmapPool;
import top.shixinzhang.sxframework.imageload.glide.load.resource.drawable.DrawableResource;
import top.shixinzhang.sxframework.imageload.glide.util.Util;

/**
 * A {@link top.shixinzhang.sxframework.imageload.glide.load.engine.Resource} that wraps an {@link BitmapDrawable}
 * <p>
 *     This class ensures that every call to {@link #get()}} always returns a new
 *     {@link BitmapDrawable} to avoid rendering issues if used in multiple views and
 *     is also responsible for returning the underlying {@link android.graphics.Bitmap} to the given
 *     {@link top.shixinzhang.sxframework.imageload.glide.load.engine.bitmap_recycle.BitmapPool} when the resource is recycled.
 * </p>
 */
public class BitmapDrawableResource extends DrawableResource<BitmapDrawable> {
    private final BitmapPool bitmapPool;

    public BitmapDrawableResource(BitmapDrawable drawable, BitmapPool bitmapPool) {
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
