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

import android.support.annotation.ColorRes;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Bind a field to the specified color resource ID. Type can be {@code int} or
 * {@link android.content.res.ColorStateList}.
 * <pre><code>
 * {@literal @}BindColor(R.color.background_green) int green;
 * {@literal @}BindColor(R.color.background_green_selector) ColorStateList greenSelector;
 * </code></pre>
 */
@Retention(CLASS) @Target(FIELD)
public @interface BindColor {
  /** Color resource ID to which the field will be bound. */
  @ColorRes int value();
}
