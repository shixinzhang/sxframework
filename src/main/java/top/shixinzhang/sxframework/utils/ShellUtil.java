package top.shixinzhang.sxframework.utils;

import android.content.Context;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.OutputStream;

/**
 * Description:
 * <br> ROOT 下执行 adb 命令的工具类
 * <p>
 * <br> Created by shixinzhang on 17/5/5.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class ShellUtil {

    private static final String TAG = ShellUtil.class.getSimpleName();

    public static void execCmd(String cmd) {
        try {
            // 申请获取root权限，这一步很重要，不然会没有作用
            Process process = Runtime.getRuntime().exec("su");
            // 获取输出流
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(
                    outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void testRoot(Context context) {
        try {
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
            outputStream.writeBytes("exit\n");
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "请允许ROOT权限", Toast.LENGTH_LONG).show();
        }
    }
}
