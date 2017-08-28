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

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import top.shixinzhang.sxframework.BaseApplication;
import top.shixinzhang.sxframework.statistic.ubt.model.PageEventAllInfo;
import top.shixinzhang.utils.FileUtils;
import top.shixinzhang.utils.GsonUtils;
import top.shixinzhang.utils.LogUtils;
import top.shixinzhang.utils.NetworkUtils;

/**
 * Description:
 * <br> 上传 UBT 数据的线程，先处理队列中的事件为文件、然后压缩、上传
 * <p>
 * <br> Created by shixinzhang on 17/8/24.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <br> https://about.me/shixinzhang
 */
@SuppressWarnings("FieldCanBeLocal")
public class UBTUploadThread extends Thread {
    private final String TAG = this.getClass().getSimpleName();
    private final int EMPTY_UPLOAD_PAUSE_TIME = 3 * 1000;
    private final int ZIP_THRESHOLD_SIZE = 10 * 1024;   //文件达到 10K 以上就需要压缩了
    private final int MAX_UPLOAD_FILE_SIZE = 3 * 1024 * 1024;   //文件最大尺寸，3M
    private final long MIN_UPLOAD_GAP_TIME = 20 * 1000; //最短 20 秒上传一次

    private final String ZIP_FILE_NAME = "/file.zip";
    private final String DATA_FILE_NAME = "/ubt.json";
    private final AtomicBoolean mRunning;
    private final ConcurrentLinkedQueue<PageEventAllInfo> mEventQueue;

    private String mLogDirectoryPath;
    private File mZipFile;
    private File mDataFile;
    private long mLastUploadTime;

    UBTUploadThread() {
        mLogDirectoryPath = getContext().getExternalCacheDir() + "/log";
        mZipFile = new File(mLogDirectoryPath + ZIP_FILE_NAME);
        mDataFile = new File(mLogDirectoryPath + DATA_FILE_NAME);
        mEventQueue = new ConcurrentLinkedQueue<>();
        mRunning = new AtomicBoolean(true);
        mLastUploadTime = System.currentTimeMillis();
    }

    @NonNull
    Context getContext() {
        return BaseApplication.getAppContext();
    }

    /**
     * 事件入队
     *
     * @param event
     */
    public synchronized void enqueueEvent(@NonNull PageEventAllInfo event) {
        mEventQueue.add(event);
    }

    @Override
    public void run() {
        File directory = new File(mLogDirectoryPath);

        while (isRunning()) {
            if (!directory.exists()) {
                directory.mkdirs();
            }

            if (mEventQueue.isEmpty()) {
                SystemClock.sleep(EMPTY_UPLOAD_PAUSE_TIME);
            }

            PageEventAllInfo ubtPageEvent = mEventQueue.poll();
            if (ubtPageEvent == null) {
                continue;
            }

            String eventJson = GsonUtils.toJson(ubtPageEvent);
            if (TextUtils.isEmpty(eventJson)) {
                continue;
            }

            LogUtils.d(TAG, "record:" + eventJson);

            writeJsonData2File(eventJson, mDataFile);
            zipAndUpload(mZipFile, mDataFile);
        }
    }

    /**
     * 先压缩后上传
     *
     * @param zipFile  可能为空，也可能不为空，那就先上传这个
     * @param dataFile 一定不能为空
     */
    private void zipAndUpload(final File zipFile, final File dataFile) {
        if (zipFile == null || dataFile == null) {
            return;
        }

        if (!dataFile.exists() || !isRunning()) {
            return;
        }

        if (zipFile.exists()) {  //先上传压缩文件
            uploadZipFile(zipFile);
        }

        if (dataFile.length() < 0) {
            return;
        }

        if (dataFile.length() > MAX_UPLOAD_FILE_SIZE) {
            FileUtils.deleteFile(zipFile.getPath());        //为什么删除的是压缩的？？
        }

        final boolean longEnoughToUploadAgain = (System.currentTimeMillis() - mLastUploadTime) > MIN_UPLOAD_GAP_TIME;
        final boolean fileBigEnoughToZip = dataFile.length() > ZIP_THRESHOLD_SIZE;

        if (longEnoughToUploadAgain || fileBigEnoughToZip) if (!zipFile.exists()) {
            zipFile(dataFile.getPath(), zipFile.getPath());
            uploadZipFile(zipFile);
        }

    }

    /**
     * 将 JSON 数据写到文件中
     *
     * @param eventJson
     * @param dataFile
     */
    private void writeJsonData2File(final String eventJson, final File dataFile) {
        //do work
    }

    /**
     * 上传压缩后的文件
     *
     * @param zipFile
     */
    private void uploadZipFile(File zipFile) {
        if (zipFile == null || !zipFile.exists() || !isRunning()) {
            return;
        }

        if (!NetworkUtils.isWifiConnect(getContext())) {   //只在 WIFI 下上传
            return;
        }

        mLastUploadTime = System.currentTimeMillis();  //上传后更新时间

        //上传时的重试
        int retryCount = 0;
        while (retryCount < 3 && isRunning()) {
            // upload code here
            //上传成功就 break

            //否则重试
            retryCount++;
        }

    }

    /**
     * 压缩文件
     *
     * @param filePath 要操作的文件的路径
     * @param zipPath  压缩后的路径
     */
    private void zipFile(String filePath, String zipPath) {
        ZipArchiveOutputStream zos = null;
        FileInputStream inputStream = null;

        if (!isRunning()) {
            return;
        }

        try {
            zos = (ZipArchiveOutputStream) new ArchiveStreamFactory()
                    .createArchiveOutputStream("zip", new FileOutputStream(zipPath));
            zos.setEncoding("utf-8");
            zos.putArchiveEntry(new ZipArchiveEntry("event.json"));

            inputStream = new FileInputStream(filePath);
            IOUtils.copy(inputStream, zos);

            //close
            zos.closeArchiveEntry();
            zos.close();
            inputStream.close();
            //删除压缩前的
            FileUtils.deleteFile(filePath);

        } catch (ArchiveException | IOException e) {
            e.printStackTrace();
            try {
                if (zos != null) {
                    zos.closeArchiveEntry();
                    zos.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    private boolean isRunning() {
        return mRunning.get();
    }

    /**
     * 关闭上传线程
     */
    public void shutdown() {
        mRunning.set(false);
    }
}
