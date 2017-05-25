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

package top.shixinzhang.sxframework.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import top.shixinzhang.sxframework.AppInfo;

/**
 * <br> Description: 应用管理，启动、关闭...
 * <p>
 * <br> Created by shixinzhang on 17/4/26.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public final class ApplicationUtils {
    private static final String TAG = ApplicationUtils.class.getSimpleName();

    private ApplicationUtils() {
    }

    /**
     * 启动某个应用
     *
     * @param context
     * @param packageName
     */
    public static void startApplication(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntentForPackage != null) {
            launchIntentForPackage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(launchIntentForPackage);
        }
    }

    /**
     * 获取当前应用名称
     *
     * @param context
     * @return
     */
    public static String getForegroundAppName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfoList = am.getRunningAppProcesses();
        if (processInfoList == null) {
            return null;
        }

        for (ActivityManager.RunningAppProcessInfo processInfo : processInfoList) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE
                    || processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return processInfo.processName;
            }
        }
        return null;
    }

    /**
     * 获取前台 Activity 名称
     *
     * @param context
     * @return
     */
    public static String getForegroundActivity(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            return cpn.getClassName();
        }
        return null;
    }

    public static void killCurrentProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    /**
     * 获取当前应用信息
     *
     * @param context
     * @return
     */
    public static PackageInfo getCurrentAppInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取 apk 信息
     *
     * @param context
     * @param path
     * @return
     */
    public static PackageInfo getApkInfo(Context context, String path) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager != null) {
            return packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        }
        return null;
    }

    /**
     * 比较当前 package 和 指定 URI 路径文件的版本
     *
     * @param context
     * @param uri
     * @return 0 if version is same; lower than 0 if current package's version is lower; greater than 0 is current's version is greater
     */
    public static int compareVersion(Context context, Uri uri) {
        PackageInfo apkInfo = getApkInfo(context, uri.getPath());   //获取指定 uri 对应 apk 的信息
        if (apkInfo == null) {
            return 1;
        }
        PackageInfo currentAppInfo = getCurrentAppInfo(context);
        if (currentAppInfo != null) {
            if (currentAppInfo.packageName.equals(apkInfo.packageName)) {    //包名相同，比较版本
                return currentAppInfo.versionCode - apkInfo.versionCode;
            }
        }
        return 1;
    }

    /**
     * 安装应用
     *
     * @param context
     * @param apkPath
     */
    public static void installApp(Context context, String apkPath) {
        File file = new File(apkPath);
        if (!file.exists()) {
            LogUtils.e(TAG, apkPath + " apk file is not exists");
            return;
        }
        installApp(context, file);
    }

    public static void installApp(Context context, File file) {
        installPackage(context, Uri.fromFile(file));
    }


    public static void installPackage(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public static void autoInstallApp(String appName) throws IOException {

        String destDir = AppInfo.DIRECTORY_PATH + File.separator + "download" + File.separator + appName;
        File file = new File(destDir);
        if (!file.exists()) {
            return;
        }
//            try {
//                String command;
//                command = "adb install -r " + destDir;
//                Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", command });
//                proc.waitFor();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        Process su = Runtime.getRuntime().exec("su");
        DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
////        outputStream.writeBytes("chmod 777 " + );
////        outputStream.writeBytes("mount -o remount,rw " + dataDir + "\n");
        outputStream.writeBytes("adb install -r " + destDir);
        outputStream.writeBytes("sleep 1\n");
        outputStream.writeBytes("exit\n");
        outputStream.flush();
        outputStream.close();
    }
}
