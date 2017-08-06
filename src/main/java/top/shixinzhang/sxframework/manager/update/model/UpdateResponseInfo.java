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

package top.shixinzhang.sxframework.manager.update.model;

import android.support.annotation.NonNull;

/**
 * Description:
 * <br> 服务器可能返回的更新信息
 * <p>
 * <br> Created by shixinzhang on 17/4/28.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class UpdateResponseInfo {
    private boolean needUpdate; //是否需要更新
    private String appName;
    private String appVersion;  //要更新的版本
    private String updateTitle; //更新标题
    private String updateTips;  //更新内容
    private boolean silentDownload;    // 是否静默下载
    private boolean forceUpdate;    //是否强制更新
    private int downType;   //如何下载：1:应用内更新 2：跳转到浏览器更新 3：应用市场更新
    private String downloadUrl; //apk 下载地址

    private UpdateResponseInfo(@NonNull Builder builder) {
        setNeedUpdate(builder.needUpdate);
        setAppName(builder.appName);
        setAppVersion(builder.appVersion);
        setUpdateTitle(builder.updateTitle);
        setUpdateTips(builder.updateTips);
        setSilentDownload(builder.silentDownload);
        setForceUpdate(builder.forceUpdate);
        setDownType(builder.downType);
        setDownloadUrl(builder.downloadUrl);
    }

    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getUpdateTitle() {
        return updateTitle;
    }

    public void setUpdateTitle(String updateTitle) {
        this.updateTitle = updateTitle;
    }

    public String getUpdateTips() {
        return updateTips;
    }

    public void setUpdateTips(String updateTips) {
        this.updateTips = updateTips;
    }

    public boolean isSilentDownload() {
        return silentDownload;
    }

    public void setSilentDownload(boolean silentDownload) {
        this.silentDownload = silentDownload;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public int getDownType() {
        return downType;
    }

    public void setDownType(int downType) {
        this.downType = downType;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public static final class Builder {
        private boolean needUpdate;
        private String appName;
        private String appVersion;
        private String updateTitle;
        private String updateTips;
        private boolean silentDownload;
        private boolean forceUpdate;
        private int downType;
        private String downloadUrl;

        public Builder() {
        }

        @NonNull
        public Builder needUpdate(boolean needUpdate) {
            this.needUpdate = needUpdate;
            return this;
        }

        @NonNull
        public Builder appName(String appName) {
            this.appName = appName;
            return this;
        }

        @NonNull
        public Builder appVersion(String appVersion) {
            this.appVersion = appVersion;
            return this;
        }

        @NonNull
        public Builder updateTitle(String updateTitle) {
            this.updateTitle = updateTitle;
            return this;
        }

        @NonNull
        public Builder updateTips(String updateTips) {
            this.updateTips = updateTips;
            return this;
        }

        @NonNull
        public Builder silentDownload(boolean silentDownload) {
            this.silentDownload = silentDownload;
            return this;
        }

        @NonNull
        public Builder forceUpdate(boolean forceUpdate) {
            this.forceUpdate = forceUpdate;
            return this;
        }

        @NonNull
        public Builder downType(int downType) {
            this.downType = downType;
            return this;
        }

        @NonNull
        public Builder downloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
            return this;
        }

        @NonNull
        public UpdateResponseInfo build() {
            return new UpdateResponseInfo(this);
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "UpdateResponseInfo{" +
                "needUpdate=" + needUpdate +
                ", appName='" + appName + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", updateTitle='" + updateTitle + '\'' +
                ", updateTips='" + updateTips + '\'' +
                ", silentDownload=" + silentDownload +
                ", forceUpdate=" + forceUpdate +
                ", downType=" + downType +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }
}
