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

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import top.shixinzhang.sxframework.R;


/**
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @date 3/30/16
 * @description Edit it! Change it! Beat it! Whatever, just do it!
 */
public class GuideView extends RelativeLayout implements ViewPager.OnPageChangeListener {

    //历史引导页版本
    private final static String HISTORY_VERSION_KEY = "historyVersion";
    private final static String INTRO_VERSION = "intro-3.0.0";

    ViewPager mPager;
    View mBottomView;
    LinearLayout mDotLinear;
    TextView mBtnHome;

    private boolean mShowGuide;
    private OnGuideFinishedListener mGuideFinishedListener;
    private int[] mImages = {R.drawable.guide_0, R.drawable.guide_1, R.drawable.guide_2};
    @NonNull
    private List<View> mDotList = new ArrayList<>();
    private Animation mFadeInAnim;
    private Animation mFadeOutAnim;
    private Animation mBottomUPAnim;

    public interface OnGuideFinishedListener {
        void onGuideFinished();
    }

    /**
     * GuideGalleryView
     *
     * @param context context
     */
    public GuideView(@NonNull Context context) {
        super(context);
        init(context);
    }

    /**
     * GuideGalleryView
     *
     * @param context context
     * @param attrs   attrs
     */
    public GuideView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * GuideGalleryView
     *
     * @param context      context
     * @param attrs        attrs
     * @param defStyleAttr defStyleAttr
     */
    public GuideView(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(@NonNull Context context) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, -1);
        setLayoutParams(params);

        View.inflate(context, R.layout.guide_galley_view, this);

        mPager = (ViewPager) findViewById(R.id.pager);
        mDotLinear = (LinearLayout) findViewById(R.id.dotLinear);
        mBottomView = findViewById(R.id.bottomView);
        mBtnHome = (TextView) findViewById(R.id.btnHome);

        mBtnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (mGuideFinishedListener != null) {
                    mGuideFinishedListener.onGuideFinished();
                }
            }
        });

        SharedPreferences share = context.getSharedPreferences("splash", Context.MODE_PRIVATE);
        String version = share.getString(HISTORY_VERSION_KEY, "");
        mShowGuide = !INTRO_VERSION.equals(version);

        mPager.setAdapter(new BaseAdapter(context));
        mPager.setOnPageChangeListener(this);

        mPager.setVisibility(View.VISIBLE);
        mDotLinear.setVisibility(View.VISIBLE);
        mBtnHome.setVisibility(View.INVISIBLE);
        mBottomView.setVisibility(View.INVISIBLE);

        mFadeInAnim = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        mFadeOutAnim = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        mBottomUPAnim = AnimationUtils.loadAnimation(context, R.anim.translate_in);
    }

    public int[] getImages() {
        return mImages;
    }

    @NonNull
    public GuideView setImages(final int[] images) {
        mImages = images;
        return this;
    }

    /**
     * 设置引导页消失的回调
     *
     * @param guideHide guideHide
     */
    public void setGuideHideListener(OnGuideFinishedListener guideHide) {
        mGuideFinishedListener = guideHide;
    }

    /**
     * 呈现引导页
     *
     * @return boolean
     */
    public boolean showGuideGallery() {
        if (mShowGuide) {
            SharedPreferences share = getContext().getSharedPreferences("splash", Context.MODE_PRIVATE);
            share.edit().putString(HISTORY_VERSION_KEY, INTRO_VERSION).apply();
            startAnimation(mFadeInAnim);
            setVisibility(View.VISIBLE);
        }
        return mShowGuide;
    }

    /**
     * 隐藏引导页并添加动画
     *
     * @return boolean
     */
    public boolean hideGuideGallery() {
        boolean status = getVisibility() == View.GONE || getVisibility() == View.INVISIBLE;
        if (status) {
            this.startAnimation(mFadeOutAnim);
            setVisibility(View.INVISIBLE);
        }
        return status;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mDotList.size() == 0) {
            for (int i = 0; i < mDotLinear.getChildCount(); i++) {
                mDotList.add(mDotLinear.getChildAt(i));
            }
        }

        for (int i = 0; i < mDotList.size(); i++) {
            mDotList.get(i).setEnabled(i == position);
        }

        if (position == mImages.length - 1) {
            mBtnHome.startAnimation(mBottomUPAnim);
            mBtnHome.setVisibility(View.VISIBLE);
            mDotLinear.startAnimation(mFadeOutAnim);
            mDotLinear.setVisibility(View.INVISIBLE);
        } else {
            mBtnHome.setVisibility(View.INVISIBLE);
            if (mDotLinear.getVisibility() != View.VISIBLE) {
                mDotLinear.startAnimation(mFadeInAnim);
                mDotLinear.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * BaseAdapter
     */
    class BaseAdapter extends PagerAdapter {

        private Context context;

        /**
         * base adapter
         *
         * @param context context
         */
        public BaseAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return mImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView item = new ImageView(context);
            item.setScaleType(ImageView.ScaleType.CENTER_CROP);
            item.setImageResource(mImages[position]);
            container.addView(item);
            return item;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }
    }
}
