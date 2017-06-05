package top.shixinzhang.sxframework.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.Timer;
import java.util.TimerTask;

import top.shixinzhang.sxframework.R;
import top.shixinzhang.sxframework.utils.LogUtils;

/**
 * <header>
 * Description:
 * </header>
 * <p>
 * Author: shixinzhang
 * </p>
 * <p>
 * Create at: 6/1/2017
 * </p>
 * <p>
 * Update at: 6/1/2017
 * </p>
 * <p>
 * Related links: http://blog.csdn.net/u014369799/article/details/50337229
 * </p>
 */
public class MarqueeTextView extends TextSwitcher implements ViewSwitcher.ViewFactory {
    private final String TAG = "MarqueeTextView";
    private int index;
    private Context context;
    private String[] resources = {
            "震惊，最帅安卓开发张拭心竟然这样评价上海", "这个故事男人听了沉默，女人听了流泪",
            "一生劳碌的他在去世前竟然说出这样的话"
    };
    private Timer timer; //
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            updateText();  //更新TextSwitcher显示内容;
        }
    };

    public MarqueeTextView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void init() {
        if (timer == null)
            timer = new Timer();
        this.setFactory(this);
        setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_v));
        this.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_v));
    }

    public void setResources(String[] res) {
        this.resources = res;
    }

    public void setTextStillTime(long time) {
        if (timer == null) {
            timer = new Timer();
        } else {
            timer.scheduleAtFixedRate(new MyTask(), 1, time);//每3秒更新
        }
    }

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
        }
    }


    private void updateText() {
        this.setText(resources[next()]);
        LogUtils.d(TAG, "update marquee view");
    }

    private int next() {
        return index++ % resources.length;
    }

    @Override
    public View makeView() {
        return new TextView(context);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHandler != null) {
            mHandler.removeMessages(0);
        }
    }
}

