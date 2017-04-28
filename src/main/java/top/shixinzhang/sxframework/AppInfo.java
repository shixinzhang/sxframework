package top.shixinzhang.sxframework;

import android.os.Environment;

import java.io.File;

/**
 * <br> Description: 保存一些手机、设备的基本信息
 * <p>
 * <br> Created by shixinzhang on 17/4/24.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class AppInfo {
    public final static String DIRECTORY_NAME = "shixinzhang";

    public final static String DIRECTORY_PATH = Environment.getExternalStorageDirectory() + File.separator + DIRECTORY_NAME;

    //文件下载目录
    public final static String DOWNLOAD_PATH = DIRECTORY_NAME + File.separator + "download";

    public final static String DOWNLOAD_URI = "file:///" + DIRECTORY_PATH + File.separator + "download";

}
