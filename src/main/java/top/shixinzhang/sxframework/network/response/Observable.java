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

package top.shixinzhang.sxframework.network.response;

import rx.functions.Action1;

/**
 * Description:
 * <br>
 * <p>
 * <br> Created by shixinzhang on 17/7/4.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <br> https://about.me/shixinzhang
 */

public class Observable<T> {
    private T mResponse;
    private Action1<T> mSuccessAction;
    private Action1<T> mFailedAction;

    public Observable<T> onSuccess(Action1<T> action1) {
        mSuccessAction = action1;
        return this;
    }

    public Observable<T> onFailed(Action1<T> action1) {
        mFailedAction = action1;
        return this;
    }
}
