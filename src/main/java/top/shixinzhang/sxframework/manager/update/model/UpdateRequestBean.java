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

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description:
 * <br> app 信息
 * <p>
 * <br> Created by shixinzhang on 17/5/24.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class UpdateRequestBean implements Parcelable {
    private String appKey;
    private String appName;
    private String latestVersionCode;
    private String latestVersionName;
    private String latestBuildNumber;
    private String channelCode;
    private String channelName;
    private String deviceType;
    private String changeLog;

    private UpdateRequestBean(Parcel in) {
        appKey = in.readString();
        appName = in.readString();
        latestVersionCode = in.readString();
        latestVersionName = in.readString();
        latestBuildNumber = in.readString();
        channelCode = in.readString();
        channelName = in.readString();
        deviceType = in.readString();
        changeLog = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appKey);
        dest.writeString(appName);
        dest.writeString(latestVersionCode);
        dest.writeString(latestVersionName);
        dest.writeString(latestBuildNumber);
        dest.writeString(channelCode);
        dest.writeString(channelName);
        dest.writeString(deviceType);
        dest.writeString(changeLog);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UpdateRequestBean> CREATOR = new Creator<UpdateRequestBean>() {
        @Override
        public UpdateRequestBean createFromParcel(Parcel in) {
            return new UpdateRequestBean(in);
        }

        @Override
        public UpdateRequestBean[] newArray(int size) {
            return new UpdateRequestBean[size];
        }
    };

    public UpdateRequestBean() {
    }

    public String getAppKey() {
        return appKey;
    }

    public UpdateRequestBean setAppKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    public String getAppName() {
        return appName;
    }

    public UpdateRequestBean setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    public String getLatestVersionCode() {
        return latestVersionCode;
    }

    public UpdateRequestBean setLatestVersionCode(String latestVersionCode) {
        this.latestVersionCode = latestVersionCode;
        return this;
    }

    public String getLatestVersionName() {
        return latestVersionName;
    }

    public UpdateRequestBean setLatestVersionName(String latestVersionName) {
        this.latestVersionName = latestVersionName;
        return this;
    }

    public String getLatestBuildNumber() {
        return latestBuildNumber;
    }

    public UpdateRequestBean setLatestBuildNumber(String latestBuildNumber) {
        this.latestBuildNumber = latestBuildNumber;
        return this;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public UpdateRequestBean setChannelCode(String channelCode) {
        this.channelCode = channelCode;
        return this;
    }

    public String getChannelName() {
        return channelName;
    }

    public UpdateRequestBean setChannelName(String channelName) {
        this.channelName = channelName;
        return this;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public UpdateRequestBean setDeviceType(String deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public String getChangeLog() {
        return changeLog;
    }

    public UpdateRequestBean setChangeLog(String changeLog) {
        this.changeLog = changeLog;
        return this;
    }


}
