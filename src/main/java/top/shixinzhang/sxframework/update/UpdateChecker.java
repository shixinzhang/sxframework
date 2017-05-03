package top.shixinzhang.sxframework.update;

import top.shixinzhang.sxframework.network.third.retrofit2.adapter.RxJavaCallAdapterFactory;
import top.shixinzhang.sxframework.network.third.retrofit2.converter.GsonConverterFactory;
import top.shixinzhang.sxframework.network.third.retrofit2.request.Call;
import top.shixinzhang.sxframework.network.third.retrofit2.request.Callback;
import top.shixinzhang.sxframework.network.third.retrofit2.request.Response;
import top.shixinzhang.sxframework.network.third.retrofit2.request.Retrofit;
import top.shixinzhang.sxframework.update.api.UpdateApi;
import top.shixinzhang.sxframework.update.model.UpdateRequestInfo;
import top.shixinzhang.sxframework.update.model.UpdateResponseInfo;

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
    public interface OnUpdateCheckListener{
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
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        Call<UpdateResponseInfo> updateInfo = retrofit.create(UpdateApi.class)
                .checkUpdate(mRequestInfo);

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
