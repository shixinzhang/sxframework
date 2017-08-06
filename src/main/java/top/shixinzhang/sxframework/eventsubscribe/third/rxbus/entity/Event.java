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

package top.shixinzhang.sxframework.eventsubscribe.third.rxbus.entity;

import android.support.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;

abstract class Event {
    /**
     * Throw a {@link RuntimeException} with given message and cause lifted from an {@link
     * InvocationTargetException}. If the specified {@link InvocationTargetException} does not have a
     * cause, neither will the {@link RuntimeException}.
     */
    public void throwRuntimeException(String msg, @NonNull InvocationTargetException e) {
        throwRuntimeException(msg, e.getCause());
    }

    /**
     * Throw a {@link RuntimeException} with given message and cause lifted from an {@link
     * InvocationTargetException}. If the specified {@link InvocationTargetException} does not have a
     * cause, neither will the {@link RuntimeException}.
     */
    public void throwRuntimeException(String msg, @NonNull Throwable e) {
        Throwable cause = e.getCause();
        if (cause != null) {
            throw new RuntimeException(msg + ": " + cause.getMessage(), cause);
        } else {
            throw new RuntimeException(msg + ": " + e.getMessage(), e);
        }
    }
}