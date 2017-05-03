package top.shixinzhang.sxframework.network.third.retrofit2.converter.custom;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import top.shixinzhang.sxframework.network.third.retrofit2.converter.Converter;
import top.shixinzhang.sxframework.network.third.retrofit2.request.Retrofit;

/**
 * Description:
 * <br> 向 Retrofit 中注册转换器的工厂
 * <p>
 * <br> Created by shixinzhang on 17/5/3.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class StringConverterFactory extends Converter.Factory {

    public static StringConverterFactory create(){
        return new StringConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == String.class){
            return new StringConverter();
        }
        return super.responseBodyConverter(type, annotations, retrofit);
    }
}
