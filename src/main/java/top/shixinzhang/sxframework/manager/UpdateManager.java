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
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import top.shixinzhang.sxframework.manager.update.IUpdateChecker;
import top.shixinzhang.sxframework.manager.update.IUpdateListener;
import top.shixinzhang.sxframework.manager.update.impl.UpdateCheckerImpl;
import top.shixinzhang.sxframework.manager.update.model.UpdateRequestBean;
import top.shixinzhang.sxframework.manager.update.model.UpdateResponseInfo;
import top.shixinzhang.sxframework.utils.DateUtils;
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
    private static UpdateManager mInstance = new UpdateManager();

    private Looper mUpdateLooper;
    private IUpdateChecker mUpdateChecker;
    private UpdateHandler mUpdateHandler;

    private final class UpdateHandler extends Handler {
        private static final long DELAY_TIME = 10 * 6 * 1000;
        private final String TAG = this.getClass().getSimpleName();

        private UpdateHandler(Looper looper) {
            super(looper);
        }

        /**
         * 子线程中请求服务器或许是否更新信息
         *
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            if (msg != null && msg.obj != null) {
                LogUtils.d(TAG, "update handler thread name :" + Thread.currentThread().getName());

                mUpdateChecker.check((UpdateRequestBean) msg.obj, new IUpdateListener() {
                    @Override
                    public void onUpdate(final UpdateResponseInfo response) {
                        if (response != null) {
                            //拿到更新响应数据，判断显示
                            if (response.isSilentDownload()) {
                                //静默下载
                            }
                        }
                    }
                });

                //定时循环请求更新
//                Message newMsg = mUpdateHandler.obtainMessage();   //新创建一个消息
//                newMsg.obj = msg.obj;
//                mUpdateHandler.sendMessageDelayed(newMsg, DELAY_TIME);
            }
        }
    }


    private UpdateManager() {
        HandlerThread thread = new HandlerThread("Update[" + DateUtils.getDateString(System.currentTimeMillis()) + "]");
        thread.start();
        mUpdateLooper = thread.getLooper();
        mUpdateHandler = new UpdateHandler(mUpdateLooper);
        mUpdateChecker = UpdateCheckerImpl.create();
    }

    public static UpdateManager getInstance() {
        return mInstance;
    }

    /**
     * 发送请求
     *
     * @param requestBean
     */
    public void request(UpdateRequestBean requestBean) {
        Message message = mUpdateHandler.obtainMessage();
        message.obj = requestBean;
        mUpdateHandler.sendMessage(message);
    }

    public void stop() {
        if (mUpdateLooper != null) {
            mUpdateLooper.quit();
        }
    }
}
