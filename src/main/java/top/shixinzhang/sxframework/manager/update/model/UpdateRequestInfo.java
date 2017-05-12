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

/**
 * Description:
 * <br> 更新的请求信息
 * <p>
 * <br> Created by shixinzhang on 17/5/3.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class UpdateRequestInfo {
    private String appName;
    private String appVersion;  //要更新的版本
    private String channel; //渠道
    private String deviceType;  //设备类型

    private UpdateRequestInfo(Builder builder) {
        setAppName(builder.appName);
        setAppVersion(builder.appVersion);
        setChannel(builder.channel);
        setDeviceType(builder.deviceType);
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

    private String getChannel() {
        return channel;
    }

    private void setChannel(String channel) {
        this.channel = channel;
    }

    private String getDeviceType() {
        return deviceType;
    }

    private void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }


    public static final class Builder {
        private String appName;
        private String appVersion;
        private String channel;
        private String deviceType;

        public Builder() {
        }

        public Builder appName(String appName) {
            this.appName = appName;
            return this;
        }

        public Builder appVersion(String appVersion) {
            this.appVersion = appVersion;
            return this;
        }

        public Builder channel(String channel) {
            this.channel = channel;
            return this;
        }

        public Builder deviceType(String deviceType) {
            this.deviceType = deviceType;
            return this;
        }

        public UpdateRequestInfo build() {
            return new UpdateRequestInfo(this);
        }
    }

    @Override
    public String toString() {
        return "UpdateRequestInfo{" +
                "appName='" + appName + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", channel='" + channel + '\'' +
                ", deviceType='" + deviceType + '\'' +
                '}';
    }
}
