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

package top.shixinzhang.sxframework.inject.third.butterknife.annotation;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import top.shixinzhang.sxframework.inject.third.butterknife.annotation.internal.ListenerClass;
import top.shixinzhang.sxframework.inject.third.butterknife.annotation.internal.ListenerMethod;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Bind a method to an {@code OnPageChangeListener} on the view for each ID specified.
 * <pre><code>
 * {@literal @}OnPageChange(R.id.example_pager) void onPageSelected(int position) {
 *   Toast.makeText(this, "Selected " + position + "!", Toast.LENGTH_SHORT).show();
 * }
 * </code></pre>
 * Any number of parameters from {@code onPageSelected} may be used on the method.
 * <p>
 * To bind to methods other than {@code onPageSelected}, specify a different {@code callback}.
 * <pre><code>
 * {@literal @}OnPageChange(value = R.id.example_pager, callback = PAGE_SCROLL_STATE_CHANGED)
 * void onPageStateChanged(int state) {
 *   Toast.makeText(this, "State changed: " + state + "!", Toast.LENGTH_SHORT).show();
 * }
 * </code></pre>
 */
@Target(METHOD)
@Retention(CLASS)
@ListenerClass(
    targetType = "android.support.v4.view.ViewPager",
    setter = "addOnPageChangeListener",
    remover = "removeOnPageChangeListener",
    type = "android.support.v4.view.ViewPager.OnPageChangeListener",
    callbacks = OnPageChange.Callback.class
)
public @interface OnPageChange {
  /** View IDs to which the method will be bound. */
  @NonNull @IdRes int[] value() default { View.NO_ID };

  /** Listener callback to which the method will be bound. */
  @NonNull Callback callback() default Callback.PAGE_SELECTED;

  /** {@code ViewPager.OnPageChangeListener} callback methods. */
  enum Callback {
    /** {@code onPageSelected(int)} */
    @ListenerMethod(
        name = "onPageSelected",
        parameters = "int"
    )
    PAGE_SELECTED,

    /** {@code onPageScrolled(int, float, int)} */
    @ListenerMethod(
        name = "onPageScrolled",
        parameters = {
            "int",
            "float",
            "int"
        }
    )
    PAGE_SCROLLED,

    /** {@code onPageScrollStateChanged(int)} */
    @ListenerMethod(
        name = "onPageScrollStateChanged",
        parameters = "int"
    )
    PAGE_SCROLL_STATE_CHANGED,
  }
}
