package top.shixinzhang.sxframework.imageload.glide.load.resource.gifbitmap;

import top.shixinzhang.sxframework.imageload.glide.load.ResourceDecoder;
import top.shixinzhang.sxframework.imageload.glide.load.engine.Resource;
import top.shixinzhang.sxframework.imageload.glide.load.model.ImageVideoWrapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * A {@link top.shixinzhang.sxframework.imageload.glide.load.ResourceDecoder} that can decode an
 * {@link top.shixinzhang.sxframework.imageload.glide.load.resource.gifbitmap.GifBitmapWrapper} from {@link InputStream} data.
 */
public class GifBitmapWrapperStreamResourceDecoder implements ResourceDecoder<InputStream, GifBitmapWrapper> {
    private final ResourceDecoder<ImageVideoWrapper, GifBitmapWrapper> gifBitmapDecoder;

    public GifBitmapWrapperStreamResourceDecoder(
            ResourceDecoder<ImageVideoWrapper, GifBitmapWrapper> gifBitmapDecoder) {
        this.gifBitmapDecoder = gifBitmapDecoder;
    }

    @Override
    public Resource<GifBitmapWrapper> decode(InputStream source, int width, int height) throws IOException {
        return gifBitmapDecoder.decode(new ImageVideoWrapper(source, null), width, height);
    }

    @Override
    public String getId() {
        return gifBitmapDecoder.getId();
    }
}
