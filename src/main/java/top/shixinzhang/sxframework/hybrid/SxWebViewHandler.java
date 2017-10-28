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

package top.shixinzhang.sxframework.hybrid;

import android.content.Context;
import android.net.Uri;

import top.shixinzhang.sxframework.hybrid.handler.HybridFactory;
import top.shixinzhang.sxframework.hybrid.handler.internal.HybridEvent;
import top.shixinzhang.sxframework.hybrid.handler.internal.HybridHandlerResult;
import top.shixinzhang.utils.GsonUtils;

/**
 * <br/> Description: 总的 WebView 处理类，错误回调，拦截，什么的，都在这里
 * <p>
 * <br/> Created by shixinzhang on 16/12/24.
 * <p>
 * <br/> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class SxWebViewHandler {
    private final String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private HybridFactory mHybridFactory;

    public SxWebViewHandler(Context context) {
        mContext = context;
        mHybridFactory = HybridFactory.getInstance();
    }

    public HybridHandlerResult handleUrl(String url) {
        return handleUri(Uri.parse(url));
    }

    private HybridHandlerResult handleUri(Uri uri) {
        HybridHandlerResult handlerResult = HybridHandlerResult.HANDLE_NOT;
        if (uri == null || uri.getScheme() == null || uri.getAuthority() == null) {
            return handlerResult;
        }
        //约定的 uri 格式: shixinzhang://ui/toast?params={"data":{"message":"hello_hybrid"}}
        String scheme = uri.getScheme();
        String authority = uri.getAuthority();
        String path = uri.getPath();
        String params = uri.getQueryParameter("params");
        String appointedUri = String.format("%s://%s%s", scheme, authority, path);
        HybridEvent.HybridEventData hybridEventData = GsonUtils.fromJson(params, HybridEvent.HybridEventData.class);
        HybridEvent hybridEvent = new HybridEvent(mContext, hybridEventData);

        handlerResult = mHybridFactory.handle(appointedUri, hybridEvent);

        return handlerResult;
    }
}
