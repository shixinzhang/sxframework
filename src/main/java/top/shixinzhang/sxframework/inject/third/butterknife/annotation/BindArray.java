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

import android.support.annotation.ArrayRes;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Bind a field to the specified array resource ID. The type of array will be inferred from the
 * annotated element.
 *
 * String array:
 * <pre><code>
 * {@literal @}BindArray(R.array.countries) String[] countries;
 * </code></pre>
 *
 * Int array:
 * <pre><code>
 * {@literal @}BindArray(R.array.phones) int[] phones;
 * </code></pre>
 *
 * Text array:
 * <pre><code>
 * {@literal @}BindArray(R.array.options) CharSequence[] options;
 * </code></pre>
 *
 * {@link android.content.res.TypedArray}:
 * <pre><code>
 * {@literal @}BindArray(R.array.icons) TypedArray icons;
 * </code></pre>
 */
@Retention(CLASS) @Target(FIELD)
public @interface BindArray {
  /** Array resource ID to which the field will be bound. */
  @ArrayRes int value();
}
