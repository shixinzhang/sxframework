package top.shixinzhang.sxframework.update.api;


import java.util.List;

import rx.Observable;
import top.shixinzhang.sxframework.network.third.retrofit2.http.Body;
import top.shixinzhang.sxframework.network.third.retrofit2.http.GET;
import top.shixinzhang.sxframework.network.third.retrofit2.http.HTTP;
import top.shixinzhang.sxframework.network.third.retrofit2.http.POST;
import top.shixinzhang.sxframework.network.third.retrofit2.http.Path;
import top.shixinzhang.sxframework.network.third.retrofit2.request.Call;
import top.shixinzhang.sxframework.update.model.UpdateRequestInfo;
import top.shixinzhang.sxframework.update.model.UpdateResponseInfo;

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
     * @param requestInfo
     * @return
     */
    @POST("update")
    Call<UpdateResponseInfo> checkUpdate(@Body UpdateRequestInfo requestInfo);  //被 Body 修饰的参数，会被 Gson 转换成 RequestBody 发送到服务器

    @GET("info/{id}")
    Call<UpdateRequestInfo> getUpdateInfo(@Path("id") int id);

    @HTTP(method = "GET", path = "info/{id}")
    Call<UpdateRequestInfo> getUpdateInfo2(@Path("id") int id);

    @POST("info/{ids}")
    Observable<List<UpdateRequestInfo>> getUpdateInfos(@Path("ids") int ids);
}
