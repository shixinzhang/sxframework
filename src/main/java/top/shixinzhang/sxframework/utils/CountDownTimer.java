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

package top.shixinzhang.sxframework.utils;

import android.support.annotation.NonNull;

/**
 * description: 倒计时控件，底部也是 handler
 * <br/>
 * NOTICE: 记得在 onDestroy 里 调用 cancel 终止 handler ! 要不然可能导致空指针
 * <br/>
 * author: shixinzhang
 * <br/>
 * data: 9/27/2016
 */
public class CountDownTimer extends android.os.CountDownTimer {
    private OnTickListener mOnTickListener;
    private OnFinishListener mOnFinishListener;


    @NonNull
    public static CountDownTimer create(long millisInFuture, long countDownInterval) {
        return new CountDownTimer(millisInFuture, countDownInterval);
    }

    /**
     * @param millisInFuture    总共多久，单位毫秒
     * @param countDownInterval 计时间隔，一般一秒 1000ms
     */
    public CountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (mOnTickListener != null) {
            mOnTickListener.onTick(millisUntilFinished / 1000);
        }
    }

    @Override
    public void onFinish() {
        if (mOnFinishListener != null) {
            mOnFinishListener.onFinish();
        }
    }

    public OnTickListener getOnTickListener() {
        return mOnTickListener;
    }

    @NonNull
    public CountDownTimer setOnTickListener(OnTickListener onTickListener) {
        mOnTickListener = onTickListener;
        return this;
    }

    public OnFinishListener getOnFinishListener() {
        return mOnFinishListener;
    }

    @NonNull
    public CountDownTimer setOnFinishListener(OnFinishListener onFinishListener) {
        mOnFinishListener = onFinishListener;
        return this;
    }

    public interface OnTickListener {
        void onTick(long remainSeconds);
    }

    public interface OnFinishListener {
        void onFinish();
    }
}
