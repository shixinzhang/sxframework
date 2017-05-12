package top.shixinzhang.sxframework.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * <br/> Description:
 * <p> 系统功能检查
 * <p>
 * <br/> Created by shixinzhang on 17/3/31.
 * <p>
 * <br/> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class SettingUtils {
    /**
     * 检查是否开启悬浮窗权限
     * @param activity
     * @param packageName
     */
    public static void checkOverlayPermission(Activity activity, String packageName) {
        if (activity == null || TextUtils.isEmpty(packageName)) {
            throw new IllegalArgumentException("[SettingUtils] Arguments can't be null!");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(activity)) {
                activity.startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + packageName)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0
                );
                Toast.makeText(activity, "请先授予本应用悬浮窗权限", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 检查是否开启辅助服务
     *
     * @param context
     * @param ServiceClass
     * @return
     */
    public static boolean checkAccessibilityOpen(Context context, Class ServiceClass) {
        int enable = 0;
        final String service = context.getPackageName() + "/" + ServiceClass.getCanonicalName();
        try {
            enable = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        TextUtils.SimpleStringSplitter simpleStringSplitter = new TextUtils.SimpleStringSplitter(':');
        if (enable == 1) {
            String enabledSettingValues = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (!TextUtils.isEmpty(enabledSettingValues)) {
                simpleStringSplitter.setString(enabledSettingValues);
                while (simpleStringSplitter.hasNext()) {
                    String enabledValue = simpleStringSplitter.next();
                    if (enabledValue.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static void jumpToSettingAccessibility(Context context) {
        Toast.makeText(context, "请点击应用名称开启辅助功能", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        context.startActivity(intent);
    }
}
