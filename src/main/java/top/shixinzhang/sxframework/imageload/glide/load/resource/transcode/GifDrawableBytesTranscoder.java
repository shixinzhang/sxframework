package top.shixinzhang.sxframework.imageload.glide.load.resource.transcode;

import top.shixinzhang.sxframework.imageload.glide.load.engine.Resource;
import top.shixinzhang.sxframework.imageload.glide.load.resource.bytes.BytesResource;
import top.shixinzhang.sxframework.imageload.glide.load.resource.gif.GifDrawable;

/**
 * An {@link top.shixinzhang.sxframework.imageload.glide.load.resource.transcode.ResourceTranscoder} that converts
 * {@link top.shixinzhang.sxframework.imageload.glide.load.resource.gif.GifDrawable} into bytes by obtaining the original bytes of the GIF from
 * the {@link top.shixinzhang.sxframework.imageload.glide.load.resource.gif.GifDrawable}.
 */
public class GifDrawableBytesTranscoder implements ResourceTranscoder<GifDrawable, byte[]> {
    @Override
    public Resource<byte[]> transcode(Resource<GifDrawable> toTranscode) {
        GifDrawable gifData = toTranscode.get();
        return new BytesResource(gifData.getData());
    }

    @Override
    public String getId() {
        return "GifDrawableBytesTranscoder.top.shixinzhang.sxframework.imageload.glide.load.resource.transcode";
    }
}
