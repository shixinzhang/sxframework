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

package top.shixinzhang.sxframework.imageload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import top.shixinzhang.sxframework.imageload.picasso.LruCache;
import top.shixinzhang.sxframework.imageload.picasso.OkHttpDownloader;
import top.shixinzhang.sxframework.imageload.picasso.Picasso;
import top.shixinzhang.sxframework.imageload.picasso.Target;
import top.shixinzhang.sxframework.utils.LogUtils;

/**
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @description Edit it! Change it! Beat it! Whatever, just do it!
 */
public class BitmapManager {

    private volatile static BitmapManager mInstance;
    private Picasso mPicasso;
    private Context mContext;
    private LruCache mLruCache;
    private Drawable mLoadingBitmap;
    private Drawable mErrorBitmap;

    private BitmapManager(@NonNull Context context) {
        mContext = context;
        mLruCache = new LruCache(mContext);

        OkHttpClient client = new OkHttpClient();
        File cacheDir = new File(context.getCacheDir(), "images");
        Cache cache = new Cache(cacheDir, 30 * 1024 * 1024);
        client.setCache(cache);

        mPicasso = new Picasso.Builder(context)
                .downloader(new OkHttpDownloader(client))
                .listener((picasso, uri, e) ->
                        LogUtils.e("Picasso error: "
                                , "%s %s %s"
                                , e.getMessage()
                                , "Failed to load image:"
                                , uri.toString()))
                .memoryCache(mLruCache)
                .build();

        Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        mErrorBitmap = new BitmapDrawable(bitmap);
        mLoadingBitmap = new BitmapDrawable(bitmap);
    }

    /**
     * get BitmapManager Instance
     * @param context context
     * @return instance
     */
    public static BitmapManager getInstance(@NonNull Context context) {
        if (mInstance == null) {
            mInstance = new BitmapManager(context);
        }
        return mInstance;
    }

    /**
     * picasso
     * @return
     */
    public Picasso getPicasso() {
        return mPicasso;
    }

    /**
     * default empty drawable
     * @param drawable drawable
     */
    public void setEmptyDrawable(Drawable drawable) {
        mLoadingBitmap = drawable;
    }

    /**
     * default fail drawable
     * @param drawable drawable
     */
    public void setFailDrawable(Drawable drawable) {
        mErrorBitmap = drawable;
    }

    /**
     * bindView
     * @param target target
     * @param url 图片路径
     */
    public void bindTarget(@Nullable Target target, String url){
        if (target == null) {
            return;
        }

        mPicasso.load(url)
                .error(mErrorBitmap)
                .placeholder(mLoadingBitmap)
                .into(target);
    }

    /**
     * 异步绑定一个View，让它显示Bitmap
     * @param view 绑定的view，如果是ImageView则设置src，否则设置background
     * @param url 图片路径
     */
    public void bindView(final ImageView view,final String url){
        bindView(view, mLoadingBitmap, mErrorBitmap, url);
    }

    /**
     * 异步绑定一个View，让它显示Bitmap
     * @param view 绑定的view，如果是ImageView则设置src，否则设置background
     * @param loadingDrawable 加载过程中view显示的bitmap,为空则显示透明
     * @param url 图片路径
     */
    public void bindView(final ImageView view, Drawable loadingDrawable, String url){
        bindView(view, loadingDrawable, mErrorBitmap, url);
    }

    /**
     * 异步绑定一个View，让它显示Bitmap
     * @param view 绑定的view，如果是ImageView则设置src，否则设置background
     * @param loadingDrawable 加载过程中view显示的bitmap,为空则显示透明
     * @param loadFailDrawable 如果加载图片失败显示的图片,为空则显示BitmapManager.Default_LoadingFailBitmap
     * @param url 图片路径
     */
    public void bindView(@Nullable final ImageView view, @Nullable Drawable loadingDrawable
            , Drawable loadFailDrawable, String url){

        if (view == null) {
            return;
        }

        if (loadingDrawable == null) {
            loadFailDrawable = mLoadingBitmap;
        }

        if (loadFailDrawable == null) {
            loadFailDrawable = mErrorBitmap;
        }

        mPicasso.load(url)
                .error(loadFailDrawable)
                .placeholder(loadingDrawable)
//                .fit()
                .into(view);
    }

    /**
     * 回收这个view绑定的Bitmap
     * @param view view
     */
    public void recycleWithView(View view){
        if(view instanceof ImageView){
            mPicasso.cancelRequest((ImageView) view);
        }
    }

    /**
     * 从view中获取bitmap，可能为null
     * @param view view
     */
    @Nullable
    public Bitmap getBitmapFromView(View view){
        try {
            if (view instanceof ImageView) {
                return ((BitmapDrawable) ((ImageView) view).getDrawable()).getBitmap();
            } else {
                return ((BitmapDrawable) view.getBackground()).getBitmap();
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将图片控制在限定高宽之内
     * @param in in
     * @param widthLimit widthLimit
     * @param heightLimit heightLimit
     */
    public Bitmap scaleToMiniBitmap(@NonNull Bitmap in, int widthLimit, int heightLimit){
        int inWidth = in.getWidth();
        int inHeight=in.getHeight();
        if(inWidth<=widthLimit && inHeight<=heightLimit) return in;
        float scale=Math.min(widthLimit*1f/inWidth,heightLimit*1f/inHeight);
        Bitmap re=Bitmap.createScaledBitmap(in, (int)(inWidth*scale), (int) (inHeight*scale), true);
        in.recycle();
        return re;
    }

    /**
     * 压缩Bitmap，返回压缩后的字节数组
     * @param bitmap bitmap
     * @param quality quality
     * @param isRecycle isRecycle
     * @return 压缩的字节
     */
    public byte[] compressBitmap(@NonNull Bitmap bitmap, int quality, boolean isRecycle){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        if(isRecycle) bitmap.recycle();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    /**
     * 清除图片内存缓存
     */
    public void clearCache()  {
        if (mLruCache != null) {
            mLruCache.clear();
        }
    }

}
