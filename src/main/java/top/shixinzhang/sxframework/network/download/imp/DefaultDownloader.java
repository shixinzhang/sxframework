package top.shixinzhang.sxframework.network.download.imp;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import top.shixinzhang.sxframework.network.download.IDownloader;

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

public class DefaultDownloader implements IDownloader {

    private volatile static DefaultDownloader mInstance;

    private final String DEFAULT_TITLE = "download_title";
    private final String DEFAULT_DESC = "downloading...";

    private DownloadManager mDownloadManager;
    private DownloadManager.Request mRequest;
    private Context mContext;
    private String mUrl;
    private String mNotificationTitle = DEFAULT_TITLE;
    private String mNotificationDesc = DEFAULT_DESC;
    private String mDownloadFilePath;
    private String mDownloadFileName;

    private DefaultDownloader(Context context) {
        mContext = context.getApplicationContext();
        mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public static synchronized DefaultDownloader getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DefaultDownloader(context.getApplicationContext());
        }
        return mInstance;
    }

    public DefaultDownloader setUrl(String url) {
        mUrl = url;
        return this;
    }

    /**
     * 可以从外部配置 Request 然后传进来
     * @param request
     * @return
     */
    public DefaultDownloader setRequeset(DownloadManager.Request request) {
        mRequest = request;
        return this;
    }

    public DefaultDownloader setNotificationTitle(String title) {
        mNotificationTitle = title;
        return this;
    }

    public DefaultDownloader setNotificationDesc(String desc) {
        mNotificationDesc = desc;
        return this;
    }

    public DefaultDownloader setFilePath(String filePath) {
        mDownloadFilePath = filePath;
        return this;
    }

    public DefaultDownloader setFileName(String fileName) {
        mDownloadFileName = fileName;
        return this;
    }


    /**
     * 开始下载
     *
     * @return 这个下载任务的编号
     */
    @Override
    public long startDownload() {
        checkProperties();

        if (getRequest() == null) {
            initDefaultRequest();
        }

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

    /**
     * 初始化默认 request
     *
     */
    private void initDefaultRequest() {
        mRequest = new DownloadManager.Request(Uri.parse(mUrl));
        mRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);   //默认设置只允许在 WIFI 情况下下载
        mRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //什么情况下通知栏提示
        mRequest.setTitle(mNotificationTitle);
        mRequest.setDescription(mNotificationDesc);
        mRequest.setAllowedOverRoaming(false);   //是否允许漫游下载
        mRequest.setDestinationInExternalFilesDir(mContext, mDownloadFilePath, mDownloadFileName);
//        mRequest.setDestinationUri(Uri.parse(savePath));
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
