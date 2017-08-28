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

package top.shixinzhang.sxframework.statistic.ubt;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import top.shixinzhang.sxframework.BuildConfig;
import top.shixinzhang.sxframework.eventsubscribe.third.eventbus.EventBus;
import top.shixinzhang.sxframework.eventsubscribe.third.eventbus.Subscribe;
import top.shixinzhang.sxframework.statistic.ubt.model.EventType;
import top.shixinzhang.sxframework.statistic.ubt.model.UBTAction;
import top.shixinzhang.sxframework.statistic.ubt.model.PageEventAllInfo;
import top.shixinzhang.utils.DateUtils;
import top.shixinzhang.utils.LogUtils;

/**
 * Description:
 * <br> 后台上报 UBT 数据的服务
 * <p>
 * <br> Created by shixinzhang on 17/8/24.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <br> https://about.me/shixinzhang
 */

public class UBTService extends Service {
    private final String TAG = this.getClass().getSimpleName();
    public static final String KEY_APP_RESUME = "app_in_front";

    private UBTUploadThread mUploadThread;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        mUploadThread = new UBTUploadThread();
        mUploadThread.start();
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        boolean appInFront = intent.getBooleanExtra(KEY_APP_RESUME, false);
        String startOrFrontEvent = appInFront ? EventType.APP_FRONT : EventType.APP_START;
        addUBTPageEvent(getPackageName(), startOrFrontEvent, startOrFrontEvent);

        return Service.START_NOT_STICKY;
    }

    /**
     * 添加事件到队列中
     *
     * @param pageId
     * @param action
     * @param name
     */
    private void addUBTPageEvent(@Nullable final String pageId, @Nullable final String action, @Nullable final String name) {
        PageEventAllInfo ubtPageEvent = new PageEventAllInfo();
        ubtPageEvent.setPageId(pageId);
        ubtPageEvent.setPageName(pageId);
//        ubtPageEvent.setUbtData(new ArrayList<>());

        UBTAction ubtAction = new UBTAction();
        ubtAction.setName(name);
        ubtAction.setAction(action);
        ubtAction.setTimeInMills(String.valueOf(System.currentTimeMillis()));
        ubtPageEvent.setUbtData(Collections.singletonList(ubtAction));
//        ubtPageEvent.getUbtData().add(ubtAction);

        eventEnqueue(ubtPageEvent);
    }

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    /**
     * 用 EventBus 监听事件，包装基本信息后入队
     *
     * @param event
     */
    @Subscribe
    public void eventEnqueue(@Nullable PageEventAllInfo event) {
        LogUtils.i(TAG, "onEventEnqueue " + event);
        if (event == null || mUploadThread == null) {
            return;
        }
        //要包装的信息很多，这里只列出几个
        event.setAppChannel(BuildConfig.FLAVOR)
                .setAppVersion(BuildConfig.VERSION_NAME)
                .setDeviceIMEI("XXX")
                .setUserAgent("321")
                .setRequestTime(DateUtils.getCurrentTime());

        mUploadThread.enqueueEvent(event);
    }

    /**
     * 退出 app 时才结束这个服务
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        addUBTPageEvent(getPackageName(), EventType.APP_ESC, EventType.APP_ESC);

        if (mUploadThread != null) {
            mUploadThread.shutdown();
            mUploadThread = null;
        }
    }
}
