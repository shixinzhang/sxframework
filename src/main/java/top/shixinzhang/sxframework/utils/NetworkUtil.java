package top.shixinzhang.sxframework.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * <br> Description: 网络状态工具类
 * <p>
 * <br> Created by shixinzhang on 17/4/26.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public final class NetworkUtil {
    private NetworkUtil() {
    }

    public static int TYPE_INT_DISCONNECT = -1;

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo = getNetworkInfo(context);
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    public static int getNetworkType(Context context) {
        NetworkInfo activeNetworkInfo = getNetworkInfo(context);
        return activeNetworkInfo == null ? TYPE_INT_DISCONNECT : activeNetworkInfo.getType();
    }

    public static String getNetworkTypeString(Context context) {
        return "";
    }

    private static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            return connectivityManager.getActiveNetworkInfo();
        }
        return null;
    }

}
