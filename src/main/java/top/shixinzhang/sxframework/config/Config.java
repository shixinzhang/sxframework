package top.shixinzhang.sxframework.config;

import android.os.Environment;

import java.io.File;

/**
 * <br> Description:
 * <p> 配置信息
 * <p>
 * <br> Created by shixinzhang on 17/4/27.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class Config {
    public static String APP_FOLDER = "";
    public static String APP_DIR = Environment.getExternalStorageDirectory() + File.separator + APP_FOLDER;
}
