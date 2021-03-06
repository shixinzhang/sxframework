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

package top.shixinzhang.sxframework.hybrid.handler;


import top.shixinzhang.sxframework.hybrid.handler.internal.HybridEvent;
import top.shixinzhang.sxframework.hybrid.handler.internal.HybridHandler;
import top.shixinzhang.sxframework.hybrid.handler.internal.HybridHandlerResult;
import top.shixinzhang.sxframework.hybrid.handler.internal.HybridMethodHandler;

/**
 * Description:
 * <br> 分享
 * <p>
 * <br> Created by shixinzhang on 17/8/31.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <br> https://about.me/shixinzhang
 */
@HybridHandler(scheme = "shixinzhang", authority = "share")
public class ShareHandler extends BaseHandler {

    @HybridMethodHandler(path = "#")
    @Override
    public HybridHandlerResult reactEvent(final HybridEvent event) {

        return HybridHandlerResult.HANDLE_NOT;
    }
}
