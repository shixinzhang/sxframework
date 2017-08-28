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

package top.shixinzhang.sxframework.manager.update.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import top.shixinzhang.sxframework.manager.update.IUpdateChecker;
import top.shixinzhang.sxframework.manager.update.IUpdateListener;
import top.shixinzhang.sxframework.manager.update.api.UpdateApi;
import top.shixinzhang.sxframework.manager.update.model.UpdateRequestBean;
import top.shixinzhang.sxframework.manager.update.model.UpdateResponseInfo;
import top.shixinzhang.sxframework.network.third.retrofit2.adapter.rxjava.RxJava2CallAdapterFactory;
import top.shixinzhang.sxframework.network.third.retrofit2.converter.GsonConverterFactory;
import top.shixinzhang.sxframework.network.third.retrofit2.request.Retrofit;

/**
 * <br> Description:
 * 更新检查的一种实现
 * <p>
 * <br> Created by shixinzhang on 17/4/27.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class UpdateCheckerImpl implements IUpdateChecker {

    @NonNull
    public static UpdateCheckerImpl create(){
        return new UpdateCheckerImpl();
    }
    /**
     * 请求是否需要更新
     *
     * @param requestBean
     * @param listener
     */
    @Override
    public void check(@Nullable final UpdateRequestBean requestBean, @Nullable final IUpdateListener listener) {
        if (requestBean == null) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("www.shixinzhang.top")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        retrofit.create(UpdateApi.class)
                .checkUpdate(requestBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UpdateResponseInfo>() {
                    @Override
                    public void call(final UpdateResponseInfo responseInfo) {
                        //success
                        if (listener != null) {
                            listener.onUpdate(responseInfo);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(final Throwable throwable) {
                        //failed
                    }
                });
    }

}
