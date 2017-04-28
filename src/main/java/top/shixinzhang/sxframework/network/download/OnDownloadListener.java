package top.shixinzhang.sxframework.network.download;

import java.io.File;

/**
 * Description:
 * <br> 下载回调
 * <p>
 * <br> Created by shixinzhang on 17/4/28.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public interface OnDownloadListener {
    /**
     * 下载成功
     * @param downloadFile 下载到的文件
     */
    void onSuccess(File downloadFile);

    /**
     * 下载失败
     * @param e 失败原因
     */
    void onFail(Exception e);

    /**
     * 下载进度
     * @param process
     * @return
     */
    long onProcess(long process);
}
