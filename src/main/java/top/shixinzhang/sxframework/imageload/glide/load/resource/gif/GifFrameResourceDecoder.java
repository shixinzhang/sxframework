package top.shixinzhang.sxframework.imageload.glide.load.resource.gif;

import android.graphics.Bitmap;

import top.shixinzhang.sxframework.imageload.glide.gifdecoder.GifDecoder;
import top.shixinzhang.sxframework.imageload.glide.load.ResourceDecoder;
import top.shixinzhang.sxframework.imageload.glide.load.engine.Resource;
import top.shixinzhang.sxframework.imageload.glide.load.engine.bitmap_recycle.BitmapPool;
import top.shixinzhang.sxframework.imageload.glide.load.resource.bitmap.BitmapResource;

class GifFrameResourceDecoder implements ResourceDecoder<GifDecoder, Bitmap> {
    private final BitmapPool bitmapPool;

    public GifFrameResourceDecoder(BitmapPool bitmapPool) {
        this.bitmapPool = bitmapPool;
    }

    @Override
    public Resource<Bitmap> decode(GifDecoder source, int width, int height) {
        Bitmap bitmap = source.getNextFrame();
        return BitmapResource.obtain(bitmap, bitmapPool);
    }

    @Override
    public String getId() {
        return "GifFrameResourceDecoder.top.shixinzhang.sxframework.imageload.glide.load.resource.gif";
    }
}
