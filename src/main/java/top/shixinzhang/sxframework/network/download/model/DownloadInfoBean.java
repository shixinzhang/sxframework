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

package top.shixinzhang.sxframework.network.download.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Description:
 * <br> 下载信息
 * <p>
 * <br> Created by shixinzhang on 17/5/24.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class DownloadInfoBean implements Serializable {
    private static final long serialVersionUID = -3175069492627972154L;

    private String mFilePath;   //文件保存位置
    private String mDownloadUrl;

    public String getFilePath() {
        return mFilePath;
    }

    @NonNull
    public DownloadInfoBean setFilePath(final String filePath) {
        mFilePath = filePath;
        return this;
    }

    public String getDownloadUrl() {
        return mDownloadUrl;
    }

    @NonNull
    public DownloadInfoBean setDownloadUrl(final String downloadUrl) {
        mDownloadUrl = downloadUrl;
        return this;
    }
}
