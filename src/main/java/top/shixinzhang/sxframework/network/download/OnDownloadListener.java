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
