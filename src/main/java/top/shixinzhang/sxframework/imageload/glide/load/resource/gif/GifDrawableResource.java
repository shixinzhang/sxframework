package top.shixinzhang.sxframework.imageload.glide.load.resource.gif;

import top.shixinzhang.sxframework.imageload.glide.load.resource.drawable.DrawableResource;
import top.shixinzhang.sxframework.imageload.glide.util.Util;

/**
 * A resource wrapping an {@link top.shixinzhang.sxframework.imageload.glide.load.resource.gif.GifDrawable}.
 */
public class GifDrawableResource extends DrawableResource<GifDrawable> {
    public GifDrawableResource(GifDrawable drawable) {
        super(drawable);
    }

    @Override
    public int getSize() {
        return drawable.getData().length + Util.getBitmapByteSize(drawable.getFirstFrame());
    }

    @Override
    public void recycle() {
        drawable.stop();
        drawable.recycle();
    }
}
