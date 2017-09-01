package top.shixinzhang.sxframework.imageload.glide.load.resource.gif;

import top.shixinzhang.sxframework.imageload.glide.Priority;
import top.shixinzhang.sxframework.imageload.glide.gifdecoder.GifDecoder;
import top.shixinzhang.sxframework.imageload.glide.load.data.DataFetcher;
import top.shixinzhang.sxframework.imageload.glide.load.model.ModelLoader;

class GifFrameModelLoader implements ModelLoader<GifDecoder, GifDecoder> {

    @Override
    public DataFetcher<GifDecoder> getResourceFetcher(GifDecoder model, int width, int height) {
        return new GifFrameDataFetcher(model);
    }

    private static class GifFrameDataFetcher implements DataFetcher<GifDecoder> {
        private final GifDecoder decoder;

        public GifFrameDataFetcher(GifDecoder decoder) {
            this.decoder = decoder;
        }

        @Override
        public GifDecoder loadData(Priority priority) {
            return decoder;
        }

        @Override
        public void cleanup() {
            // Do nothing. GifDecoder reads from an arbitrary InputStream, the caller will close that stream.
        }

        @Override
        public String getId() {
            return String.valueOf(decoder.getCurrentFrameIndex());
        }

        @Override
        public void cancel() {
            // Do nothing.
        }
    }
}
