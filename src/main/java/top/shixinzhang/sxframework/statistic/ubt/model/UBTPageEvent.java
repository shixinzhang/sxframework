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

package top.shixinzhang.sxframework.statistic.ubt.model;

import java.io.Serializable;
import java.util.List;


/**
 * 上报给服务端的数据，内容很多
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @date 15/11/26
 * @description Edit it! Change it! Beat it! Whatever, just do it!
 */
public class UBTPageEvent implements Serializable {

    private String pageId;
    private String pageName;
    private String referPageId;
    private String referPageName;
    private String path;
    private String host;
    private String orderId;
    private String productId;
    private String pageSearchWord;
    private String channel;
    private String browser;
    private String parameters;
    private String refer;
    private String cityId;
    private String businessType = "1";

    private String cookieId;
    private String requestId;
    private String appVersion;
    private String gpsCityId;
    private String gpsApiType;
    private String guid;
    private String requestTime;
    private String gpsCoordinate;
    private String deviceType = "Android";
    private String pushType;
    private String pushToken;
    private String userToken;
    private String appType;
    private String appVersionCode;
    private String appChannel;
    private String deviceOSVersion;
    private String deviceIMEI;
    private String deviceBrand;
    private String deviceProduct;
    private String deviceCPU;
    private String deviceNetType;
    private String deviceResolution;
    private String userAgent;
    private List<UBTAction> ubtData;
}
