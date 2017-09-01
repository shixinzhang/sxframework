package top.shixinzhang.sxframework.imageload.glide.load.resource.file;

import top.shixinzhang.sxframework.imageload.glide.load.ResourceDecoder;
import top.shixinzhang.sxframework.imageload.glide.load.engine.Resource;

import java.io.File;

/**
 * A simple {@link top.shixinzhang.sxframework.imageload.glide.load.ResourceDecoder} that creates resource for a given {@link File}.
 */
public class FileDecoder implements ResourceDecoder<File, File> {

    @Override
    public Resource<File> decode(File source, int width, int height) {
        return new FileResource(source);
    }

    @Override
    public String getId() {
        return "";
    }
}
