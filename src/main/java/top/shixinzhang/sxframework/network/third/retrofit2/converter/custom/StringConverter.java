package top.shixinzhang.sxframework.network.third.retrofit2.converter.custom;

import java.io.IOException;

import okhttp3.ResponseBody;
import top.shixinzhang.sxframework.network.third.retrofit2.converter.Converter;

/**
 * Description:
 * <br> 自定义一个转换器
 * <p>
 * <br> Created by shixinzhang on 17/5/3.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class StringConverter implements Converter<ResponseBody, String> {
    @Override
    public String convert(ResponseBody value) throws IOException {
        return value.string();
    }
}
