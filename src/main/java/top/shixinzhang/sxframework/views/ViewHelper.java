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

package top.shixinzhang.sxframework.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

import top.shixinzhang.sxframework.utils.LogUtils;

/**
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @date 15/10/25
 * @description Edit it! Change it! Beat it! Whatever, just do it!
 */
public class ViewHelper {

    private static DisplayMetrics sDisplayMetrics;

    /**
     * 初始化操作
     *
     * @param context context
     */
    public static void init(@NonNull Context context) {
        sDisplayMetrics = context.getResources().getDisplayMetrics();
    }

    /**
     * 获取屏幕宽度 单位：像素
     *
     * @return 屏幕宽度
     */
    public static int getWidthPixels() {
        return sDisplayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度 单位：像素
     *
     * @return 屏幕高度
     */
    public static int getHeightPixels() {
        return sDisplayMetrics.heightPixels;
    }

    /**
     * sp2Px
     * @param context context
     * @param sp sp
     * @return float
     */
    public static float sp2Px(@NonNull Context context, int sp) {
      return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
              context.getResources().getDisplayMetrics());
    }

    /**
     * dp2Px
     * @param context context
     * @param dp dp
     * @return float
     */
    public static float dp2Px(@NonNull Context context, int dp) {
      return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
              context.getResources().getDisplayMetrics());
    }

    /**
     * px2Dp
     * @param context context
     * @param px px
     * @return float
     */
    public static float px2Dp(@NonNull Context context, int px) {
      return context.getResources().getDisplayMetrics().density * px;
    }


    /**
     * event事件是否发生在view内
     * @param view
     * @param event
     * @return
     */
    public static boolean eventInView(@NonNull View view, @NonNull MotionEvent event) {
        if(view.getVisibility() != View.VISIBLE)
            return false;

        int clickX = (int) event.getRawX();
        int clickY = (int) event.getRawY();
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        int width = view.getWidth();
        int height = view.getHeight();
        if (clickX > x && clickX < (x + width) &&
                clickY > y && clickY < (y + height)) {
            return true;
        }

        return false;
    }


    /**
     * 获取view的onClickListener
     * @param view
     * @return
     */
    @Nullable
    public static View.OnClickListener getOnClickListener(@Nullable View view) {
        if (view == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return getOnClickListenerV14(view);
        } else {
            return getOnClickListenerV(view);
        }
    }

    //Used for APIs lower than ICS (API 14)
    @Nullable
    private static View.OnClickListener getOnClickListenerV(View view) {
        View.OnClickListener retrievedListener = null;
        String viewStr = "android.view.View";
        Field field;

        try {
            field = Class.forName(viewStr).getDeclaredField("mOnClickListener");
            retrievedListener = (View.OnClickListener) field.get(view);
        } catch (NoSuchFieldException ex) {
            LogUtils.e("Reflection", getViewEntryName(view) + "No Such Field.");
        } catch (IllegalAccessException ex) {
            LogUtils.e("Reflection", getViewEntryName(view) + "Illegal Access.");
        } catch (ClassNotFoundException ex) {
            LogUtils.e("Reflection", getViewEntryName(view) + "Class Not Found.");
        }

        return retrievedListener;
    }

    //Used for new ListenerInfo class structure used beginning with API 14 (ICS)
    @Nullable
    private static View.OnClickListener getOnClickListenerV14(View view) {
        View.OnClickListener retrievedListener = null;
        String viewStr = "android.view.View";
        String lInfoStr = "android.view.View$ListenerInfo";

        try {
            Field listenerField = Class.forName(viewStr).getDeclaredField("mListenerInfo");
            Object listenerInfo = null;

            if (listenerField != null) {
                listenerField.setAccessible(true);
                listenerInfo = listenerField.get(view);
            }

            Field clickListenerField = Class.forName(lInfoStr).getDeclaredField("mOnClickListener");

            if (clickListenerField != null && listenerInfo != null) {
                retrievedListener = (View.OnClickListener) clickListenerField.get(listenerInfo);
            }
        } catch (NoSuchFieldException ex) {
            LogUtils.e("Reflection", getViewEntryName(view) + "No Such Field.");
        } catch (IllegalAccessException ex) {
            LogUtils.e("Reflection", getViewEntryName(view) + "Illegal Access.");
        } catch (ClassNotFoundException ex) {
            LogUtils.e("Reflection", getViewEntryName(view) + "Class Not Found.");
        }

        return retrievedListener;
    }

    /**
     * 获取view的id name
     * @param view
     * @return
     */
    @Nullable
    public static String getViewEntryName(@Nullable View view) {
        if(null == view || view.getId() == View.NO_ID)
            return null;

        return view.getResources().getResourceEntryName(view.getId());
    }

    /**
     * 获取view的text,view 为textView的子类
     * @param view
     * @return
     */
    @Nullable
    public static String getViewText(@Nullable View view) {
        if (null == view)
            return null;

        if (view instanceof TextView) {
            return ((TextView)view).getText().toString();
        }

        return null;
    }

    /**
     * show toast
     * @param context
     * @param tip
     */
    public static void showToast(Context context, String tip) {
        Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
    }

    @NonNull
    public static StateListDrawable createStateListDrawable(int normalColor, int activeColor) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{-android.R.attr.state_checked}, new ColorDrawable(normalColor));
        drawable.addState(new int[]{android.R.attr.state_checked}, new ColorDrawable(activeColor));
        return drawable;
    }

    @TargetApi(16)
    public static void setViewBgStateList(@NonNull View view, int normalColor, int activeColor) {
        view.setBackground(createStateListDrawable(normalColor, activeColor));
    }

    public static void setTextViewStateColor(@NonNull TextView textView, int normalColor, int activeColor) {
        int[][] states = new int[2][];
        states[0] = new int[] { android.R.attr.state_checked };
        states[1] = new int[] { -android.R.attr.state_checked };
        int[] colors = new int[] {activeColor, normalColor};
        textView.setTextColor(new ColorStateList(states, colors));
    }
}
