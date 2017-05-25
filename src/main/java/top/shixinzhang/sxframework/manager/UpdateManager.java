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

package top.shixinzhang.sxframework.manager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import top.shixinzhang.sxframework.utils.LogUtils;

/**
 * Description:
 * <br>
 * <p>
 * <br> Created by shixinzhang on 17/5/12.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class UpdateManager {
    private final String TAG = this.getClass().getSimpleName();

    private final static class UpdateHandler extends Handler {
        private final String TAG = this.getClass().getSimpleName();

        private UpdateHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg != null && msg.obj != null) {
//                SystemClock.sleep(10 * 1000);
                LogUtils.d(TAG, "update handler thread name :" + Thread.currentThread().getName());

                mUpdateChecker.check((AppInfoBean) msg.obj, new IUpdateListener() {
                    @Override
                    public void onUpdate(final UpdateResponseBean response) {
                        if (response != null && response.isSilentUpdate()) {
                            AlertUtil.toastShort(UpdateService.this, "需要更新");
                            downloadAndInstall(response.getApkDownloadUrl());
                        }
                    }
                });

                Message newMsg = mServiceHandler.obtainMessage();   //新创建一个消息
                newMsg.obj = msg.obj;
                mServiceHandler.sendMessageDelayed(newMsg, DELAY_TIME);
            }
        }
    }

}
