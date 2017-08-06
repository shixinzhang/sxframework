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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;

import top.shixinzhang.sxframework.AppInfo;
import top.shixinzhang.sxframework.manager.update.IUpdateChecker;
import top.shixinzhang.sxframework.manager.update.IUpdateListener;
import top.shixinzhang.sxframework.manager.update.impl.UpdateCheckerImpl;
import top.shixinzhang.sxframework.manager.update.model.UpdateRequestBean;
import top.shixinzhang.sxframework.manager.update.model.UpdateResponseInfo;
import top.shixinzhang.sxframework.network.download.IDownloadListener;
import top.shixinzhang.sxframework.network.download.IDownloader;
import top.shixinzhang.sxframework.network.download.imp.OkHttpDownloader;
import top.shixinzhang.sxframework.network.download.model.DownloadInfoBean;
import top.shixinzhang.sxframework.utils.LogUtils;
import top.shixinzhang.sxframework.utils.NetworkUtils;

/**
 * Description:
 * <br> 更新管理器，感觉跟业务需求相关性太强，不适合放到 framework 中
 * <p>
 * <br> Created by shixinzhang on 17/5/12.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class UpdateManager {
    private final String TAG = this.getClass().getSimpleName();
    @NonNull
    private static UpdateManager mInstance = new UpdateManager();
    @Nullable
    private static Context mContext;

    private IDownloader mDownloader;
    private IUpdateChecker mUpdateChecker;
    @Nullable
    private UpdateResponseInfo mUpdateResponseInfo;
    private IDownloadListener mDownloadListener;

    private UpdateManager() {
        mUpdateChecker = UpdateCheckerImpl.create();
        mDownloader = OkHttpDownloader.getInstance();
    }

    @NonNull
    public static UpdateManager getInstance(@Nullable Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context can't be null");
        }
        mContext = context.getApplicationContext() != null ? context.getApplicationContext() : context;
        return mInstance;
    }

    /**
     * 发送请求
     *
     * @param requestBean
     */
    public void request(UpdateRequestBean requestBean) {
        mUpdateChecker.check(requestBean, new IUpdateListener() {
            @Override
            public void onUpdate(@Nullable final UpdateResponseInfo response) {
                if (response != null && response.isNeedUpdate()) {
                    mUpdateResponseInfo = response;
                    downloadOrInstall();
                }
            }
        });
    }

    private void downloadOrInstall() {
        if (mUpdateResponseInfo == null) {
            return;
        }

        //WIFI 环境下下载
        if (!NetworkUtils.isWifiConnect(mContext)) {
            LogUtils.d(TAG, "Not connect WIFI, can't download");
            return;
        }

        if (mUpdateResponseInfo.isSilentDownload()) {
            //静默下载
            downloadSilently(mUpdateResponseInfo.getDownloadUrl());
            return;
        }

    }

    /**
     * 静默下载
     *
     * @param downloadUrl
     */
    private void downloadSilently(final String downloadUrl) {
        DownloadInfoBean downloadInfoBean = new DownloadInfoBean();
        downloadInfoBean.setDownloadUrl(downloadUrl)
                .setFilePath(AppInfo.DOWNLOAD_APK_PATH);
        mDownloader.download(downloadInfoBean, mDownloadListener);
    }


    public IDownloadListener getDownloadListener() {
        return mDownloadListener;
    }

    @NonNull
    public UpdateManager setDownloadListener(final IDownloadListener downloadListener) {
        mDownloadListener = downloadListener;
        return this;
    }


    public void stop() {
    }
}
