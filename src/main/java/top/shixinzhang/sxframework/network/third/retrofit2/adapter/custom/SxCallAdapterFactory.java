package top.shixinzhang.sxframework.network.third.retrofit2.adapter.custom;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import top.shixinzhang.sxframework.network.third.retrofit2.request.CallAdapter;
import top.shixinzhang.sxframework.network.third.retrofit2.request.Retrofit;

/**
 * Description:
 * <br> 向 Retrofit 注册转换器的工厂
 * <p>
 * <br> Created by shixinzhang on 17/5/3.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class SxCallAdapterFactory extends CallAdapter.Factory {
    public static SxCallAdapterFactory create() {
        return new SxCallAdapterFactory();
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Class<?> rawType = getRawType(returnType);
        if (rawType == SxCall.class && returnType instanceof ParameterizedType) {
            Type callReturnType = getParameterUpperBound(0, (ParameterizedType) returnType);
            return new SxCallAdapter(callReturnType);
        }
        return null;
    }
}
