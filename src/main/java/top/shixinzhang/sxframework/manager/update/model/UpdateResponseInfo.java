package top.shixinzhang.sxframework.manager.update.model;

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

    private UpdateResponseInfo(Builder builder) {
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

    private boolean isNeedUpdate() {
        return needUpdate;
    }

    private void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }

    private String getAppName() {
        return appName;
    }

    private void setAppName(String appName) {
        this.appName = appName;
    }

    private String getAppVersion() {
        return appVersion;
    }

    private void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    private String getUpdateTitle() {
        return updateTitle;
    }

    private void setUpdateTitle(String updateTitle) {
        this.updateTitle = updateTitle;
    }

    private String getUpdateTips() {
        return updateTips;
    }

    private void setUpdateTips(String updateTips) {
        this.updateTips = updateTips;
    }

    private boolean isSilentDownload() {
        return silentDownload;
    }

    private void setSilentDownload(boolean silentDownload) {
        this.silentDownload = silentDownload;
    }

    private boolean isForceUpdate() {
        return forceUpdate;
    }

    private void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    private int getDownType() {
        return downType;
    }

    private void setDownType(int downType) {
        this.downType = downType;
    }

    private String getDownloadUrl() {
        return downloadUrl;
    }

    private void setDownloadUrl(String downloadUrl) {
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

        public Builder needUpdate(boolean needUpdate) {
            this.needUpdate = needUpdate;
            return this;
        }

        public Builder appName(String appName) {
            this.appName = appName;
            return this;
        }

        public Builder appVersion(String appVersion) {
            this.appVersion = appVersion;
            return this;
        }

        public Builder updateTitle(String updateTitle) {
            this.updateTitle = updateTitle;
            return this;
        }

        public Builder updateTips(String updateTips) {
            this.updateTips = updateTips;
            return this;
        }

        public Builder silentDownload(boolean silentDownload) {
            this.silentDownload = silentDownload;
            return this;
        }

        public Builder forceUpdate(boolean forceUpdate) {
            this.forceUpdate = forceUpdate;
            return this;
        }

        public Builder downType(int downType) {
            this.downType = downType;
            return this;
        }

        public Builder downloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
            return this;
        }

        public UpdateResponseInfo build() {
            return new UpdateResponseInfo(this);
        }
    }

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
