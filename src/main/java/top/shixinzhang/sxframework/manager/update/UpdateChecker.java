package top.shixinzhang.sxframework.manager.update;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.shixinzhang.sxframework.network.third.retrofit2.adapter.RxJava2CallAdapterFactory;
import top.shixinzhang.sxframework.network.third.retrofit2.adapter.custom.SxCallAdapterFactory;
import top.shixinzhang.sxframework.network.third.retrofit2.converter.GsonConverterFactory;
import top.shixinzhang.sxframework.network.third.retrofit2.converter.custom.StringConverterFactory;
import top.shixinzhang.sxframework.network.third.retrofit2.request.Call;
import top.shixinzhang.sxframework.network.third.retrofit2.request.Callback;
import top.shixinzhang.sxframework.network.third.retrofit2.request.Response;
import top.shixinzhang.sxframework.network.third.retrofit2.request.Retrofit;
import top.shixinzhang.sxframework.manager.update.api.UpdateApi;
import top.shixinzhang.sxframework.manager.update.model.UpdateRequestInfo;
import top.shixinzhang.sxframework.manager.update.model.UpdateResponseInfo;

/**
 * <br> Description:
 * <p>
 * <br> Created by shixinzhang on 17/4/27.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class UpdateChecker {
    public interface OnUpdateCheckListener {
        void onUpdate();
    }

    private UpdateRequestInfo mRequestInfo;
    private UpdateResponseInfo mResponseInfo;

    private UpdateChecker(Builder builder) {
        mRequestInfo = builder.mRequestInfo;
        mResponseInfo = builder.mResponseInfo;
        check();
    }

    private void check() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("www.test.com/")
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(SxCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        UpdateApi updateApi = retrofit.create(UpdateApi.class);

        final Call<UpdateResponseInfo> updateInfo = updateApi.checkUpdate(mRequestInfo);

        updateInfo.enqueue(new Callback<UpdateResponseInfo>() {
            @Override
            public void onResponse(Call<UpdateResponseInfo> call, Response<UpdateResponseInfo> response) {
                System.out.println(response.body().toString());
            }

            @Override
            public void onFailure(Call<UpdateResponseInfo> call, Throwable t) {
                t.printStackTrace();
            }
        });

        updateApi.getUpdateInfos(3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UpdateRequestInfo>>() {

                    @Override
                    public void onCompleted() {
                        //finished
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<UpdateRequestInfo> updateRequestInfos) {
                        if (updateRequestInfos != null && !updateRequestInfos.isEmpty()) {

                        }
                    }
                });
    }

    private UpdateRequestInfo getRequestInfo() {
        return mRequestInfo;
    }

    private UpdateResponseInfo getResponseInfo() {
        return mResponseInfo;
    }

    public static final class Builder {
        private UpdateRequestInfo mRequestInfo;
        private UpdateResponseInfo mResponseInfo;

        public Builder() {
        }

        public Builder mRequestInfo(UpdateRequestInfo mRequestInfo) {
            this.mRequestInfo = mRequestInfo;
            return this;
        }

        public Builder mResponseInfo(UpdateResponseInfo mResponseInfo) {
            this.mResponseInfo = mResponseInfo;
            return this;
        }

        public UpdateChecker check() {
            return new UpdateChecker(this);
        }
    }
}
