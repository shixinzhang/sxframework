package top.shixinzhang.sxframework.imageload.glide.load.resource;

import top.shixinzhang.sxframework.imageload.glide.load.ResourceEncoder;
import top.shixinzhang.sxframework.imageload.glide.load.engine.Resource;

import java.io.OutputStream;

/**
 * A simple {@link top.shixinzhang.sxframework.imageload.glide.load.ResourceEncoder} that never writes data.
 *
 * @param <T> The type of the resource that will always fail to be encoded.
 */
public class NullResourceEncoder<T> implements ResourceEncoder<T> {
    private static final NullResourceEncoder<?> NULL_ENCODER = new NullResourceEncoder<Object>();

    /**
     * Returns a NullResourceEncoder for the given type.
     *
     * @param <T> The type of data to be written (or in this case not written).
     */
    @SuppressWarnings("unchecked")
    public static <T> NullResourceEncoder<T> get() {
        return (NullResourceEncoder<T>) NULL_ENCODER;
    }

    @Override
    public boolean encode(Resource<T> data, OutputStream os) {
        return false;
    }

    @Override
    public String getId() {
        return "";
    }
}
