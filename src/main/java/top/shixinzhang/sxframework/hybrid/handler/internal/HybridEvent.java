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

package top.shixinzhang.sxframework.hybrid.handler.internal;

import android.content.Context;

import java.util.HashMap;

/**
 * <br/> Description: 消息事件模型
 * <p>
 * <br/> Created by shixinzhang on 16/12/24.
 * <p>
 * <br/> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class HybridEvent {
    private Context mEventContext;
    private HybridEventData mEventData;

    public HybridEvent(Context eventContext, HybridEventData eventData) {
        mEventContext = eventContext;
        mEventData = eventData;
    }

    public HybridEventData getEventData() {
        return mEventData;
    }

    public void setEventData(HybridEventData eventData) {
        mEventData = eventData;
    }

    public Context getEventContext() {
        return mEventContext;
    }

    public void setEventContext(Context eventContext) {
        mEventContext = eventContext;
    }

    public static class HybridEventData{
        // url 中的 param 都是键值对
        private HashMap data;

        public HashMap getData() {
            return data;
        }

        public void setData(HashMap data) {
            this.data = data;
        }
    }
}
