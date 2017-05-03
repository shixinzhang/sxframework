package top.shixinzhang.sxframework.network.third.retrofit2.adapter.custom;

import java.lang.reflect.Type;

import top.shixinzhang.sxframework.network.third.retrofit2.request.Call;
import top.shixinzhang.sxframework.network.third.retrofit2.request.CallAdapter;

/**
 * Description:
 * <br> 请求转换适配器
 * <p>
 * <br> Created by shixinzhang on 17/5/3.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class SxCallAdapter implements CallAdapter<Object, SxCall<?>> {
    private final Type mResponseType;

    public SxCallAdapter(Type responseType) {
        mResponseType = responseType;
    }

    @Override
    public Type responseType() {
        return mResponseType;
    }

    @Override
    public SxCall<?> adapt(Call<Object> call) {
        return new SxCall<>(call);
    }

}
