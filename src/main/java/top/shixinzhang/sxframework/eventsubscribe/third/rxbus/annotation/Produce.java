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

package top.shixinzhang.sxframework.eventsubscribe.third.rxbus.annotation;


import android.support.annotation.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import top.shixinzhang.sxframework.eventsubscribe.third.rxbus.Bus;
import top.shixinzhang.sxframework.eventsubscribe.third.rxbus.finder.AnnotatedFinder;
import top.shixinzhang.sxframework.eventsubscribe.third.rxbus.thread.EventThread;

/**
 * Marks a method as an instance producer, as used by {@link AnnotatedFinder} and {@link Bus}.
 * <p/>
 * Bus infers the instance type from the annotated method's return type. Producer methods may return null when there is
 * no appropriate value to share. The calling {@link Bus} ignores such returns and posts nothing.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Produce {
    @NonNull Tag[] tags() default {};

    @NonNull EventThread thread() default EventThread.MAIN_THREAD;
}
