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

package top.shixinzhang.sxframework.network.download.imp;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import top.shixinzhang.sxframework.AppInfo;
import top.shixinzhang.sxframework.network.download.IAPKDownloader;
import top.shixinzhang.sxframework.network.download.IDownloadListener;
import top.shixinzhang.sxframework.network.download.model.DownloadInfoBean;

/**
 * Description:
 * <br> 使用安卓自带的 DownloadManager 实现下载器
 * <p>
 * <br> Created by shixinzhang on 17/4/27.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class DefaultDownloader implements IAPKDownloader {

    private volatile static DefaultDownloader mInstance;

    private final String DEFAULT_TITLE = "download_title";
    private final String DEFAULT_DESC = "downloading...";
    private final String DEFAULT_PATH = AppInfo.DOWNLOAD_PATH;

    private DownloadManager mDownloadManager;
    private DownloadManager.Request mRequest;
    private Context mContext;
    private String mUrl;
    private String mNotificationTitle = DEFAULT_TITLE;
    private String mNotificationDesc = DEFAULT_DESC;
    private String mDownloadFilePath = DEFAULT_PATH;
    private String mDownloadFileName;

    private DefaultDownloader(@NonNull Context context) {
        mContext = context.getApplicationContext() == null ? context : context.getApplicationContext();
        mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public static synchronized DefaultDownloader getInstance(@NonNull Context context) {
        if (mInstance == null) {
            mInstance = new DefaultDownloader(context);
        }
        return mInstance;
    }

    @NonNull
    public DefaultDownloader setUrl(String url) {
        mUrl = url;
        return this;
    }

    /**
     * 可以从外部配置 Request 然后传进来
     *
     * @param request
     * @return
     */
    @NonNull
    public DefaultDownloader setRequest(DownloadManager.Request request) {
        mRequest = request;
        return this;
    }

    @NonNull
    public DefaultDownloader setNotificationTitle(String title) {
        mNotificationTitle = title;
        return this;
    }

    @NonNull
    public DefaultDownloader setNotificationDesc(String desc) {
        mNotificationDesc = desc;
        return this;
    }

    @NonNull
    public DefaultDownloader setFilePath(String filePath) {
        mDownloadFilePath = filePath;
        return this;
    }

    @NonNull
    public DefaultDownloader setFileName(String fileName) {
        mDownloadFileName = fileName;
        return this;
    }

    @NonNull
    @Override
    public IAPKDownloader prepare() {
        mRequest = new DownloadManager.Request(Uri.parse(mUrl));
        mRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);   //默认设置只允许在 WIFI 情况下下载
        mRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //什么情况下通知栏提示
        mRequest.setTitle(mNotificationTitle);
        mRequest.setDescription(mNotificationDesc);
        mRequest.setAllowedOverRoaming(false);   //是否允许漫游下载
//        mRequest.setDestinationInExternalFilesDir(mContext, mDownloadFilePath, mDownloadFileName);
        mRequest.setDestinationInExternalPublicDir(mDownloadFilePath, mDownloadFileName);

//        String fileAbsolutePath = mDownloadFilePath + File.separator + mDownloadFileName;  //绝对路径
//        mRequest.setDestinationUri(Uri.parse(fileAbsolutePath));
        return this;
    }


    @Override
    public long download(final DownloadInfoBean downloadInfo, final IDownloadListener listener) {
        checkProperties();

        return mDownloadManager.enqueue(mRequest);
    }

    /**
     * 检查属性是否齐全
     */
    private void checkProperties() {
        if (TextUtils.isEmpty(mUrl)) {
            throw new IllegalArgumentException("Can't download because of empty url !");
        }
        if (TextUtils.isEmpty(mDownloadFileName)) {
            throw new IllegalArgumentException("Please set the name of download file !");
        }
    }

    private DownloadManager.Request getRequest() {
        return mRequest;
    }

    private DownloadManager getDownloadManager() {
        return mDownloadManager;
    }


    /**
     * 根据 id 查询保存文件的地址 URI
     *
     * @param id
     * @return
     */
    @Override
    public Uri getDownloadUri(long id) {
        return getDownloadManager().getUriForDownloadedFile(id);
    }

    /**
     * 根据 id 查询下载文件所在的路径
     *
     * @param id
     * @return
     */
    @Nullable
    @Override
    public String getDownloadPath(long id) {
        Cursor cursor = getCursor(id);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                }
            } finally { //确保最终释放
                cursor.close();
            }
        }

        return null;
    }

    /**
     * 根据 id 查询下载状态
     *
     * @param id
     * @return 下载状态
     * @see DownloadManager#STATUS_PENDING
     * @see DownloadManager#STATUS_PAUSED
     * @see DownloadManager#STATUS_RUNNING
     * @see DownloadManager#STATUS_SUCCESSFUL
     * @see DownloadManager#STATUS_FAILED
     */
    @Override
    public int getDownloadStatus(long id) {
        Cursor cursor = getCursor(id);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                }
            } finally {
                cursor.close();
            }
        }
        return -1;
    }

    @Override
    public long getDownloadProgress() {
        return 0;
    }

    @Override
    public void cancel(long... ids) {
        //取消任务，同时删除本地文件
        getDownloadManager().remove(ids);
    }

    /**
     * 查询某个 id 对应的游标
     *
     * @param id
     * @return
     */
    private Cursor getCursor(long id) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(id);
        return getDownloadManager().query(query);
    }

}
