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

package top.shixinzhang.sxframework.imageload.glide.load.engine;

import top.shixinzhang.sxframework.imageload.glide.load.Encoder;
import top.shixinzhang.sxframework.imageload.glide.load.Key;
import top.shixinzhang.sxframework.imageload.glide.load.ResourceDecoder;
import top.shixinzhang.sxframework.imageload.glide.load.ResourceEncoder;
import top.shixinzhang.sxframework.imageload.glide.load.Transformation;
import top.shixinzhang.sxframework.imageload.glide.load.resource.transcode.ResourceTranscoder;

class EngineKeyFactory {

    @SuppressWarnings("rawtypes")
    public EngineKey buildKey(String id, Key signature, int width, int height, ResourceDecoder cacheDecoder,
            ResourceDecoder sourceDecoder, Transformation transformation, ResourceEncoder encoder,
            ResourceTranscoder transcoder, Encoder sourceEncoder) {
        return new EngineKey(id, signature, width, height, cacheDecoder, sourceDecoder, transformation, encoder,
                transcoder, sourceEncoder);
    }

}
