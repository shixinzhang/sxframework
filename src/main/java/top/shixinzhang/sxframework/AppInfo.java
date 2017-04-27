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
    //文件下载目录
    public final static String DOWNLOAD_PATH = Environment.DIRECTORY_DOWNLOADS + File.separator + "download";   //storage/emulated/0/Android/data/your-package/files/IDownloader/
}
