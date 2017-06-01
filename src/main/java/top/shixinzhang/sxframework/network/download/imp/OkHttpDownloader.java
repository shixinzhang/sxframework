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

package top.shixinzhang.sxframework.network.download.imp;


import java.io.File;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.shixinzhang.sxframework.network.download.IDownloadListener;
import top.shixinzhang.sxframework.network.download.IDownloader;
import top.shixinzhang.sxframework.network.download.model.DownloadInfoBean;
import top.shixinzhang.sxframework.utils.FileUtils;
import top.shixinzhang.sxframework.utils.LogUtils;

//import io.reactivex.Observable;
//import io.reactivex.ObservableEmitter;
//import io.reactivex.ObservableOnSubscribe;
//import io.reactivex.Observer;
//import io.reactivex.annotations.NonNull;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.schedulers.Schedulers;

/**
 * Description:
 * <br> 下载器的 okHttp3 实现
 * <p>
 * <br> Created by shixinzhang on 17/5/24.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class OkHttpDownloader implements IDownloader {

    private OkHttpClient mOkHttpClient;
    public static OkHttpDownloader mInstance = new OkHttpDownloader();

    private OkHttpDownloader() {
        mOkHttpClient = new OkHttpClient();
    }

    public static OkHttpDownloader getInstance() {
        return mInstance;
    }

    /**
     * 下载文件，最后回调切换到主线程
     *
     * @param downloadInfo
     * @param listener
     */
    @Override
    public long download(final DownloadInfoBean downloadInfo, final IDownloadListener listener) {
        if (downloadInfo == null) {
            return -1;
        }

        long currentTaskId = System.currentTimeMillis();
        Observable.create(new DownloadSubscribe(downloadInfo))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DownloadInfoBean>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(final Throwable e) {
                        if (listener != null) {
                            listener.onFail(e);
                        }
                    }

//                    @Override
//                    public void onSubscribe(@NonNull final Disposable d) {
//                        //取消操作的观察者？
//                    }

                    @Override
                    public void onNext(final DownloadInfoBean downloadInfoBean) {
                        if (listener != null) {
                            listener.onSuccess(new File(downloadInfoBean.getFilePath()));
                        }
                    }
                });

        return currentTaskId;
    }

    @Override
    public void cancel(final long... ids) {
    }

    /**
     * 下载执行
     */
    private class DownloadSubscribe implements Observable.OnSubscribe<DownloadInfoBean> {
        private DownloadInfoBean mDownloadInfoBean;

        public DownloadSubscribe(final DownloadInfoBean downloadInfo) {
            mDownloadInfoBean = downloadInfo;
        }

        @Override
        public void call(final Subscriber<? super DownloadInfoBean> subscriber) {
            download(subscriber);
        }

        private void download(final Subscriber<? super DownloadInfoBean> subscriber) {
            if (mDownloadInfoBean == null) {
                return;
            }

            File file = new File(mDownloadInfoBean.getFilePath());
//            file.getParentFile().mkdirs();

            Request.Builder builder = new Request.Builder()
                    .url(mDownloadInfoBean.getDownloadUrl())
                    .addHeader("Cache-Control", "no-cache");

            try {
                LogUtils.d("UpdateService", "Download thread name :" + Thread.currentThread().getName());
                Response response = mOkHttpClient.newCall(builder.build()).execute();

                if (response == null || !response.isSuccessful()) {
                    subscriber.onError(new Throwable("下载失败"));
                    return;
                }

                LogUtils.d("UpdateService", "开始写入:" + Thread.currentThread().getId());
                boolean writeFileSuccess = FileUtils.writeFile(file, response.body().byteStream());
                if (writeFileSuccess) {
                    subscriber.onNext(mDownloadInfoBean);
                } else {
                    if (file.exists()) {
                        file.delete();
                    }
                    subscriber.onError(new Throwable("文件写入失败"));
                }

            } catch (IOException e) {
                e.printStackTrace();
                subscriber.onError(e);
            }

        }
    }
}
