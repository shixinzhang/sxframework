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

import android.text.TextUtils;
import android.widget.Toast;

import java.util.HashMap;

import top.shixinzhang.sxframework.hybrid.handler.internal.HybridEvent;
import top.shixinzhang.sxframework.hybrid.handler.internal.HybridHandler;
import top.shixinzhang.sxframework.hybrid.handler.internal.HybridHandlerResult;
import top.shixinzhang.sxframework.hybrid.handler.internal.HybridMethodHandler;

/**
 * <br/> Description: H5 触发原生组件中的一种：UI 操作
 * <p>
 * <br/> Created by shixinzhang on 16/12/24.
 * <p>
 * <br/> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

@HybridHandler(scheme = "shixinzhang", authority = "ui")
public class UIHandler extends BaseHandler{

    @HybridMethodHandler(path = "/toast")
    public HybridHandlerResult showToast(HybridEvent hybridEvent) {
        if (hybridEvent != null && hybridEvent.getEventContext() != null && hybridEvent.getEventData() != null) {
            HashMap dataMap = hybridEvent.getEventData().getData();
            String message = (String) dataMap.get("message");
            if (!TextUtils.isEmpty(message)) {
                Toast.makeText(hybridEvent.getEventContext(), message, Toast.LENGTH_SHORT).show();
                return HybridHandlerResult.HANDLE_DONE;
            }
        }
        return HybridHandlerResult.HANDLE_NOT;

    }

    @Override
    public HybridHandlerResult reactEvent(final HybridEvent event) {
        return null;
    }
}
