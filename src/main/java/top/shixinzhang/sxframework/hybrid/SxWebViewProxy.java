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
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.util.Map;

/**
 * <br/> Description:
 * <p>
 * <br/> Created by shixinzhang on 16/12/23.
 * <p>
 * <br/> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class SxWebViewProxy extends WebView {

    /**
     * URL 替换监听器
     */
    public interface OnUrlReplaceListener {
        String onUrlReplace(String url);
    }

    /**
     * WebView UI 变化监听器
     */
    public interface OnWebViewUIChangedListener{
        void onTitleChanged(String title);
        void onProgressChanged(int progress);
    }

    private OnUrlReplaceListener mUrlReplaceListener;

    public SxWebViewProxy(Context context) {
        super(context);
        initView();
    }

    public SxWebViewProxy(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SxWebViewProxy(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    }

    @Override
    public void loadUrl(String url) {
        if (mUrlReplaceListener != null) {
            url = mUrlReplaceListener.onUrlReplace(url);
        }
        super.loadUrl(url);
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        if (mUrlReplaceListener != null) {
            url = mUrlReplaceListener.onUrlReplace(url);
        }
        super.loadUrl(url, additionalHttpHeaders);
    }

    public OnUrlReplaceListener getUrlReplaceListener() {
        return mUrlReplaceListener;
    }

    public void setUrlReplaceListener(OnUrlReplaceListener urlReplaceListener) {
        mUrlReplaceListener = urlReplaceListener;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (canGoBack()){
                goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
