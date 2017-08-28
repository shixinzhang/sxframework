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

/**
 * Description:
 * <br> 事件类型
 * <p>
 * <br> Created by shixinzhang on 17/8/24.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <br> https://about.me/shixinzhang
 */

public class EventType {
    //app
    public static final String APP_START = "app_start"; //app 开始
    public static final String APP_ESC = "app_esc"; //退出 app
    public static final String APP_FRONT = "app_front"; //app 到了前台
    public static final String APP_BACKGROUND = "app_background";   //app 到了后台

    //页面
    public static final String PAGE_SHOW = "page_show"; //进入页面
    public static final String PAGE_ESC = "page_esc";   //离开页面

    //点击事件
    public static final String CLICK = "click"; //点击
    public static final String LONG_CLICK = "long_click";   //长按
    public static final String SWIPE = "swipe"; //滑动
}
