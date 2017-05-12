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

package top.shixinzhang.sxframework.network.third.retrofit2.adapter.custom;

import java.io.IOException;

import top.shixinzhang.sxframework.network.third.retrofit2.request.Call;

/**
 * Description:
 * <br> 自定义一个 Call，简单包装 Call
 * <p>
 * <br> Created by shixinzhang on 17/5/3.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class SxCall<R> {
    public final Call<R> mCall;

    public SxCall(Call<R> call) {
        mCall = call;
    }

    public R get() throws IOException {
        System.out.println("before call ...");
        return mCall.execute().body();
    }
}
