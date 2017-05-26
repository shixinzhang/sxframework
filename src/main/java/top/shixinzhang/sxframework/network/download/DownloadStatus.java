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

/**
 * Description:
 * <br> 下载状态
 * <p>
 * <br> Created by shixinzhang on 17/5/26.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public final class DownloadStatus {

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
