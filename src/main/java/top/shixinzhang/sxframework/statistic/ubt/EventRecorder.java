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

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.WeakHashMap;

import top.shixinzhang.sxframework.common.base.BaseActivity;
import top.shixinzhang.sxframework.eventsubscribe.third.eventbus.EventBus;
import top.shixinzhang.sxframework.statistic.ubt.model.EventType;
import top.shixinzhang.sxframework.statistic.ubt.model.UBTAction;
import top.shixinzhang.sxframework.statistic.ubt.model.PageEventAllInfo;
import top.shixinzhang.utils.LogUtils;

/**
 * Description:
 * <br> 记录信息
 * <p>
 * <br> Created by shixinzhang on 17/8/24.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <br> https://about.me/shixinzhang
 */

public class EventRecorder {
    private static final String TAG = "EventRecorder";
    private static Application.ActivityLifecycleCallbacks mLifecycleCallbacks;
    private static boolean isAppInBackground = false;
    private static WeakHashMap<Class, PageEventInfo> mPageAnnotationCache;   //页面 Class 和对应注解的缓存池
    private static boolean isAutoRecord = false;    //是否自动打点 (即不设置注解也记录)

    public static void init(@NonNull Context context) {
        Intent intent = new Intent(context, UBTService.class);
        context.startService(intent);

        if (mLifecycleCallbacks == null) {
            context.registerComponentCallbacks(new StopUBTCallback(context));

            mLifecycleCallbacks = new ActivityLifecycleListener();
            Context app = context.getApplicationContext();
            if (app instanceof Application) {
                ((Application) app).registerActivityLifecycleCallbacks(mLifecycleCallbacks);
            }
        }

    }

    /**
     * 点击事件里调用
     *
     * @param activity
     * @param target
     */
    public static void onClickEvent(Activity activity, String target) {
        addUBTPageEvent(activity, target, EventType.CLICK);
    }

    private static void addUBTPageEvent(@Nullable final String pageId, @Nullable final String action) {
        addUBTPageEvent(pageId, action, action);
    }

    private static void addUBTPageEvent(@Nullable final Object obj, @Nullable final String action, @Nullable final String name) {
        if (obj == null) {
            return;
        }
        Class<?> pageClass = obj.getClass();
        addUBTPageEvent(pageClass.getName(), pageClass.getSimpleName(), action, name);
    }

    /**
     * 添加事件到队列中
     *
     * @param pageId
     * @param action
     * @param name
     */
    private static void addUBTPageEvent(@Nullable final String pageId, @Nullable final String action, @Nullable final String name) {
        addUBTPageEvent(pageId, pageId, action, name);
    }

    /**
     * 事件进一步信息
     *
     * @param pageId
     * @param pageName
     * @param action
     * @param name
     * @param params
     */
    private static void addUBTPageEvent(final String pageId, final String pageName, final String action, final String name, final String... params) {
        LogUtils.i(TAG, "receive event: ***********************" +
                "\npageId: " + pageId +
                "\npageName: " + pageName +
                "\naction: " + action +
                "\nname: " + name +
                "\nparams: " + Arrays.toString(params));

        if (TextUtils.isEmpty(pageName) || TextUtils.isEmpty(action)) {
            return;
        }

        PageEventAllInfo ubtPageEvent = new PageEventAllInfo();
        ubtPageEvent.setPageId(pageId);
        ubtPageEvent.setPageName(pageName);
        ubtPageEvent.setUbtData(new ArrayList<>());

        UBTAction ubtAction = new UBTAction();
        ubtAction.setName(name);
        ubtAction.setAction(action);
        ubtAction.setParams(Arrays.asList(params));
        ubtAction.setTimeInMills(String.valueOf(System.currentTimeMillis()));
        ubtPageEvent.getUbtData().add(ubtAction);

        postEventEnqueue(ubtPageEvent);
    }

    /**
     * 发送事件给 UBTService 中注册的监听，入队
     *
     * @param event
     */
    private static void postEventEnqueue(final PageEventAllInfo event) {
        if (event != null) {
            EventBus.getDefault().post(event);
        }
    }

    /**
     * 页面事件
     *
     * @param activity
     * @param action
     */
    private static void onPageEvent(@NonNull final Activity activity, @NonNull final String action) {
        PageEventInfo annotation = getPageAnnotation(activity);

        String pageParameters = null;
        if (activity instanceof BaseActivity) {
            pageParameters = ((BaseActivity) activity).getPageEventParam();
        }

        if (annotation == null || TextUtils.isEmpty(annotation.pageName())) { //没找到注解
            if (isAutoRecord()) {    //如果没设置注解也记录事件
                Class<? extends Activity> aClass = activity.getClass();
                addUBTPageEvent(aClass.getName(), aClass.getSimpleName(),
                        action, aClass.getName(), pageParameters);
            }
            return;
        }

        addUBTPageEvent(annotation.pageId(), annotation.pageName(),
                action, annotation.pageName(), pageParameters);

    }

    /**
     * 获取页面 Class 的注解信息
     *
     * @param page
     * @return
     */
    private static PageEventInfo getPageAnnotation(@NonNull final Object page) {
        if (page == null) {
            return null;
        }
        Class<?> pageClass = page.getClass();

        if (mPageAnnotationCache == null) {
            mPageAnnotationCache = new WeakHashMap<>();
        }

        PageEventInfo eventInfo = mPageAnnotationCache.get(pageClass);
        if (eventInfo == null) {
            eventInfo = pageClass.getAnnotation(PageEventInfo.class);
            mPageAnnotationCache.put(pageClass, eventInfo);
        }

        return eventInfo;
    }

    public static boolean isAutoRecord() {
        return isAutoRecord;
    }

    public static void setIsAutoRecord(final boolean autoRecord) {
        isAutoRecord = autoRecord;
    }

    /**
     * 监听清理内存回调，关闭 UBT 服务
     */
    private static class StopUBTCallback implements ComponentCallbacks2 {
        private Context mContext;

        public StopUBTCallback(@NonNull final Context context) {
            mContext = context;
        }

        @Override
        public void onTrimMemory(final int level) {
            LogUtils.d(TAG, "onTrimMemory: " + level);
            if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {   //UI 在后台，并且要清理内存了
                addUBTPageEvent(mContext.getPackageName(), EventType.APP_BACKGROUND);
                isAppInBackground = true;
                //关闭上传 UBT 服务
                mContext.stopService(new Intent(mContext, UBTService.class));
            }
        }

        @Override
        public void onConfigurationChanged(final Configuration newConfig) {

        }

        @Override
        public void onLowMemory() {
            mContext.stopService(new Intent(mContext, UBTService.class));
        }
    }

    /**
     * Activity 生命周期回调
     */
    private static class ActivityLifecycleListener implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(final Activity activity, final Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(final Activity activity) {

        }

        @Override
        public void onActivityResumed(final Activity activity) {
            if (activity == null) {
                return;
            }

            if (isAppInBackground) { //之前在后台
                isAppInBackground = false;
                Intent intent = new Intent(activity, UBTService.class);
                intent.putExtra(UBTService.KEY_APP_RESUME, true);   //传递 flag
                activity.startService(intent);
            }

            onPageEvent(activity, EventType.PAGE_SHOW);
        }

        @Override
        public void onActivityPaused(final Activity activity) {

        }

        @Override
        public void onActivityStopped(final Activity activity) {
            onPageEvent(activity, EventType.PAGE_ESC);
        }

        @Override
        public void onActivitySaveInstanceState(final Activity activity, final Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(final Activity activity) {

        }
    }
}
