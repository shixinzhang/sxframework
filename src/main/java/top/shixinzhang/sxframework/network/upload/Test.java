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

package top.shixinzhang.sxframework.network.upload;

import java.io.File;
import java.io.IOException;

import top.shixinzhang.sxframework.network.third.okhttp3.Call;
import top.shixinzhang.sxframework.network.third.okhttp3.Callback;
import top.shixinzhang.sxframework.network.third.okhttp3.Response;


/**
 * <br> Description:
 * <p>
 * <br> Created by shixinzhang on 17/4/24.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class Test {
    public static void main(String[] args) {
        UploadManager uploadManager = new UploadManager.Builder()
                .file(new File("sdcard/test/"))
                .name("serverName")
                .fileType("image/png")
                .fileName("test")
                .url("www.shixinzhang.top")
                .callback(new Callback() {
                    @Override
                    public void onFailure(final Call call, final IOException e) {

                    }

                    @Override
                    public void onResponse(final Call call, final Response response) throws IOException {

                    }
                })
                .build();

        uploadManager.upload();
    }
}
