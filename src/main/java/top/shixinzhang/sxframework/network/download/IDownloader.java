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

import top.shixinzhang.sxframework.network.download.model.DownloadInfoBean;

/**
 * Description:
 * <br> 文件下载器
 * <p>
 * <br> Created by shixinzhang on 17/5/24.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public interface IDownloader {
    /**
     * 开始下载
     *
     * @param downloadInfo
     * @param listener
     * @return 这个下载任务对应的 ID
     */
    long download(DownloadInfoBean downloadInfo, IDownloadListener listener);

    /**
     * 取消下载
     *
     * @param ids 下载任务 IDs
     */
    void cancel(long... ids);
}
