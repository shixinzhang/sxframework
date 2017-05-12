package top.shixinzhang.sxframework.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Description:
 * <br> 提示工具类
 * <p>
 * <br> Created by shixinzhang on 17/5/9.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class AlertUtils {
    public static void toastShort(Context context, String msg) {
        toast(context, msg, Toast.LENGTH_SHORT);
    }

    public static void toastLong(Context context, String msg) {
        toast(context, msg, Toast.LENGTH_LONG);
    }

    /**
     * 普通的吐司提示
     *
     * @param context
     * @param msg
     * @param duration
     */
    private static void toast(Context context, String msg, int duration) {
        if (context == null) {
            throw new IllegalArgumentException("[AlertUtils] Context can't be null!");
        }
        if (TextUtils.isEmpty(msg)) {
            return;
        }
//        duration = duration == Toast.LENGTH_SHORT ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;

        Toast.makeText(context, msg, duration).show();
    }
}
