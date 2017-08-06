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
import android.text.TextWatcher;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import top.shixinzhang.sxframework.inject.third.butterknife.annotation.internal.ListenerClass;
import top.shixinzhang.sxframework.inject.third.butterknife.annotation.internal.ListenerMethod;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Bind a method to an {@link TextWatcher TextWatcher} on the view for each ID specified.
 * <pre><code>
 * {@literal @}OnTextChanged(R.id.example) void onTextChanged(CharSequence text) {
 *   Toast.makeText(this, "Text changed: " + text, Toast.LENGTH_SHORT).show();
 * }
 * </code></pre>
 * Any number of parameters from {@link TextWatcher#onTextChanged(CharSequence, int, int, int)
 * onTextChanged} may be used on the method.
 * <p>
 * To bind to methods other than {@code onTextChanged}, specify a different {@code callback}.
 * <pre><code>
 * {@literal @}OnTextChanged(value = R.id.example, callback = BEFORE_TEXT_CHANGED)
 * void onBeforeTextChanged(CharSequence text) {
 *   Toast.makeText(this, "Before text changed: " + text, Toast.LENGTH_SHORT).show();
 * }
 * </code></pre>
 *
 * @see TextWatcher
 */
@Target(METHOD)
@Retention(CLASS)
@ListenerClass(
    targetType = "android.widget.TextView",
    setter = "addTextChangedListener",
    remover = "removeTextChangedListener",
    type = "android.text.TextWatcher",
    callbacks = OnTextChanged.Callback.class
)
public @interface OnTextChanged {
  /** View IDs to which the method will be bound. */
  @NonNull @IdRes int[] value() default { View.NO_ID };

  /** Listener callback to which the method will be bound. */
  @NonNull Callback callback() default Callback.TEXT_CHANGED;

  /** {@link TextWatcher} callback methods. */
  enum Callback {
    /** {@link TextWatcher#onTextChanged(CharSequence, int, int, int)} */
    @ListenerMethod(
        name = "onTextChanged",
        parameters = {
            "java.lang.CharSequence",
            "int",
            "int",
            "int"
        }
    )
    TEXT_CHANGED,

    /** {@link TextWatcher#beforeTextChanged(CharSequence, int, int, int)} */
    @ListenerMethod(
        name = "beforeTextChanged",
        parameters = {
            "java.lang.CharSequence",
            "int",
            "int",
            "int"
        }
    )
    BEFORE_TEXT_CHANGED,

    /** {@link TextWatcher#afterTextChanged(android.text.Editable)} */
    @ListenerMethod(
        name = "afterTextChanged",
        parameters = "android.text.Editable"
    )
    AFTER_TEXT_CHANGED,
  }
}
