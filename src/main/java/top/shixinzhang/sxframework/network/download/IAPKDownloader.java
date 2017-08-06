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

package top.shixinzhang.sxframework.network.download;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Description:
 * <br> APK 下载接口，定义规范
 * <p>
 * <br> Created by shixinzhang on 17/4/27.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public interface IAPKDownloader extends IDownloader {

    @Nullable
    String getDownloadPath(long id);

    Uri getDownloadUri(long id);

    int getDownloadStatus(long id);

    long getDownloadProgress();

    @NonNull
    IAPKDownloader setUrl(String url);

    @NonNull
    IAPKDownloader setNotificationTitle(String title);

    @NonNull
    IAPKDownloader setNotificationDesc(String desc);

    @NonNull
    IAPKDownloader setFilePath(String filePath);

    @NonNull
    IAPKDownloader setFileName(String fileName);

    /**
     * 最后准备工作，不是必须调用，取决于具体实现
     * （但是在有些实现里，需要调用这个后才可以调用 download）
     *
     * @return
     */
    @NonNull
    IAPKDownloader prepare();


}
