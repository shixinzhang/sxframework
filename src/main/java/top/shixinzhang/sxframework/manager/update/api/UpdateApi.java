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

package top.shixinzhang.sxframework.manager.update.api;


import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;
import top.shixinzhang.sxframework.manager.update.model.UpdateRequestBean;
import top.shixinzhang.sxframework.network.third.retrofit2.http.Body;
import top.shixinzhang.sxframework.network.third.retrofit2.http.GET;
import top.shixinzhang.sxframework.network.third.retrofit2.http.HTTP;
import top.shixinzhang.sxframework.network.third.retrofit2.http.POST;
import top.shixinzhang.sxframework.network.third.retrofit2.http.Path;
import top.shixinzhang.sxframework.network.third.retrofit2.request.Call;
import top.shixinzhang.sxframework.manager.update.model.UpdateResponseInfo;

/**
 * Description:
 * <br> 模拟更新的 API 接口
 * <p>
 * <br> Created by shixinzhang on 17/5/3.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public interface UpdateApi {
    /**
     * 注解中的 URL 前缀 / 无用，BaseUrl 要以 / 结尾；如果注解中的 URL 为完整路径（http://www.XXX.com），将取代 BaseUrl
     *
     * @param requestInfo
     * @return
     */
    @NonNull
    @POST("update")
    Call<UpdateResponseInfo> checkUpdate2(@Body UpdateRequestBean requestInfo);  //被 Body 修饰的参数，会被 Gson 转换成 RequestBody 发送到服务器

    @NonNull
    @GET("info/{id}")
    Observable<UpdateResponseInfo> checkUpdate(@Body UpdateRequestBean requestBean);

    @NonNull
    @HTTP(method = "GET", path = "info/{id}")
    Call<UpdateResponseInfo> getUpdateInfo2(@Path("id") int id);

    @NonNull
    @POST("info/{ids}")
    Observable<List<UpdateResponseInfo>> getUpdateInfos(@Path("ids") int ids);
}
