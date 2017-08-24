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

package top.shixinzhang.sxframework.manager.update;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import top.shixinzhang.sxframework.network.download.DownloadStatus;
import top.shixinzhang.sxframework.network.download.IAPKDownloader;
import top.shixinzhang.sxframework.network.download.imp.DefaultDownloader;
import top.shixinzhang.utils.ApplicationUtils;
import top.shixinzhang.utils.LogUtils;
import top.shixinzhang.utils.SpUtils;

/**
 * <br> Description: 测试的 apk 下载
 * <p>
 * <br> Created by shixinzhang on 17/4/27.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class APKDownloader {
    private final String TAG = this.getClass().getSimpleName();

    private Context mContext;
    private String mUrl;
    private String mTitle;
    private String mApkName;

    private IAPKDownloader mDownload;

    private APKDownloader(@NonNull Builder builder) {
        mContext = builder.mContext;
        mUrl = builder.mUrl;
        mTitle = builder.mTitle;
        mApkName = builder.mApkName;
        initDownload();
    }

    private void initDownload() {
        mDownload = DefaultDownloader.getInstance(mContext)
                .setUrl(mUrl)
                .setNotificationTitle(mTitle)
                .setNotificationDesc("shixinzhang 正在下载中...")
                .setFileName(mApkName)
                .prepare();
    }

    public void download() {
        checkArguments();

        long lastDownloadId = (long) SpUtils.getDataFromDefault(getContext(), DownloadManager.EXTRA_DOWNLOAD_ID, -1L);

        if (lastDownloadId != -1L) { //之前有下载任务
            int downloadStatus = mDownload.getDownloadStatus(lastDownloadId);
            if (downloadStatus == DownloadStatus.SUCCESSFUL) {        //下载成功
                Uri uri = mDownload.getDownloadUri(lastDownloadId);
                if (uri != null) {
                    if (compareVersion(getContext(), uri)) { //需要安装
                        installPackage(getContext(), uri);
                        return;
                    } else {     //文件不存在或者，已有的版本不当前版本高，删除
                        mDownload.cancel(lastDownloadId);
                    }
                }

            } else if (downloadStatus == DownloadStatus.RUNNING || downloadStatus == DownloadStatus.PENDING) {    //正在进行或者即将进行
                LogUtils.i(TAG, "Download task " + lastDownloadId + " is running...");
                return;
            }
        }

        startDownload();
    }

    private void startDownload() {
        long id = mDownload.download(null, null);
        SpUtils.saveDataInDefault(getContext(), DownloadManager.EXTRA_DOWNLOAD_ID, id);
        LogUtils.i(TAG, "apk start download , id is " + id);
    }

    private void installPackage(@NonNull Context context, Uri uri) {
        ApplicationUtils.installPackage(context, uri);
    }

    /**
     * 比较当前 Package 和指定 URI 的文件 version
     *
     * @param context
     * @param uri
     * @return 当前 pkg 比 uri 的文件版本小时返回 true
     */
    private boolean compareVersion(@NonNull Context context, @NonNull Uri uri) {
        int result = ApplicationUtils.compareVersion(context, uri);
        return result < 0;
    }


    private void checkArguments() {
        if (getContext() == null) {
            throw new IllegalArgumentException("Context must not be null for the download task !");
        }
        if (TextUtils.isEmpty(getUrl())) {
            throw new IllegalArgumentException("Apk url must not be null for the download task !");
        }
        if (TextUtils.isEmpty(getTitle())) {
            // TODO: 17/4/27 默认标题 ？
        }
        if (TextUtils.isEmpty(getApkName())) {
            // TODO: 17/4/27 默认应用名称 ？ app.apk
        }
    }

    private Context getContext() {
        return mContext;
    }

    private String getUrl() {
        return mUrl;
    }

    private String getTitle() {
        return mTitle;
    }

    private String getApkName() {
        return mApkName;
    }

    public static final class Builder {
        private Context mContext;
        private String mUrl;
        private String mTitle;
        private String mApkName;

        public Builder() {
        }

        @NonNull
        public Builder context(Context mContext) {
            this.mContext = mContext;
            return this;
        }

        @NonNull
        public Builder url(String mUrl) {
            this.mUrl = mUrl;
            return this;
        }

        @NonNull
        public Builder title(String mTitle) {
            this.mTitle = mTitle;
            return this;
        }

        @NonNull
        public Builder apkName(String mApkName) {
            this.mApkName = mApkName;
            return this;
        }

        @NonNull
        public APKDownloader build() {
            return new APKDownloader(this);
        }
    }

}
