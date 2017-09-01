/*
 * Copyright (c) 2017. shixinzhang (shixinzhang2016@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.shixinzhang.sxframework.imageload.glide.load.data;

import top.shixinzhang.sxframework.imageload.glide.Priority;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * A simple resource fetcher to convert byte arrays into input stream. Requires an id to be passed in to identify the
 * data in the byte array because there is no cheap/simple way to obtain a useful id from the data itself.
 */
public class ByteArrayFetcher implements DataFetcher<InputStream> {
    private final byte[] bytes;
    private final String id;

    public ByteArrayFetcher(byte[] bytes, String id) {
        this.bytes = bytes;
        this.id = id;
    }

    @Override
    public InputStream loadData(Priority priority) {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public void cleanup() {
        // Do nothing. It's safe to leave a ByteArrayInputStream open.
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void cancel() {
        // Do nothing.
    }
}
