package top.shixinzhang.sxframework.network.third.retrofit2.adapter.custom;

import java.io.IOException;

import top.shixinzhang.sxframework.network.third.retrofit2.request.Call;

/**
 * Description:
 * <br> 自定义一个 Call，简单包装 Call
 * <p>
 * <br> Created by shixinzhang on 17/5/3.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class SxCall<R> {
    public final Call<R> mCall;

    public SxCall(Call<R> call) {
        mCall = call;
    }

    public R get() throws IOException {
        System.out.println("before call ...");
        return mCall.execute().body();
    }
}
