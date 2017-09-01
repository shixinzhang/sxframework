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

package top.shixinzhang.sxframework.imageload.glide.load.engine.cache;

import com.bumptech.glide.load.Key;

import java.io.File;

/**
 * A simple class that returns null for all gets and ignores all writes.
 */
public class DiskCacheAdapter implements DiskCache {
    @Override
    public File get(Key key) {
        // no op, default for overriders
        return null;
    }

    @Override
    public void put(Key key, Writer writer) {
        // no op, default for overriders
    }

    @Override
    public void delete(Key key) {
        // no op, default for overriders
    }

    @Override
    public void clear() {
        // no op, default for overriders
    }
}
