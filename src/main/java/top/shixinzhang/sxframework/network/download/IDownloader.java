package top.shixinzhang.sxframework.network.download;

import android.net.Uri;

/**
 * Description:
 * <br> 下载接口，定义规范
 * <p>
 * <br> Created by shixinzhang on 17/4/27.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public interface IDownloader {

    /**
     * 删除指定 id 的下载任务
     *
     * @param ids
     */
    void cancel(long... ids);

    String getDownloadPath(long id);

    Uri getDownloadUri(long id);

    int getDownloadStatus(long id);

    long getDownloadProgress();

    long startDownload();


    final class Status {

        /**
         * the download is waiting to start.
         */
        public final static int PENDING = 1;

        /**
         * the download is currently running.
         */
        public final static int RUNNING = 1 << 1;

        /**
         * the download is waiting to retry or resume.
         */
        public final static int PAUSED = 1 << 2;

        /**
         * the download has successfully completed.
         */
        public final static int SUCCESSFUL = 1 << 3;

        /**
         * the download has failed (and will not be retried).
         */
        public final static int FAILED = 1 << 4;

        /**
         * he download has completed with an error that doesn't fit under any other error code.
         */
        public final static int ERROR_UNKNOWN = 1000;

    }
}
