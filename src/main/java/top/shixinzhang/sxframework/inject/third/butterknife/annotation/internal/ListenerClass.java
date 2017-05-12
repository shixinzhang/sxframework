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

package top.shixinzhang.sxframework.inject.third.butterknife.annotation.internal;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME) @Target(ANNOTATION_TYPE)
public @interface ListenerClass {
  String targetType();

  /** Name of the setter method on the {@linkplain #targetType() target type} for the listener. */
  String setter();

  /**
   * Name of the method on the {@linkplain #targetType() target type} to remove the listener. If
   * empty {@link #setter()} will be used by default.
   */
  String remover() default "";

  /** Fully-qualified class name of the listener type. */
  String type();

  /** Enum which declares the listener callback methods. Mutually exclusive to {@link #method()}. */
  Class<? extends Enum<?>> callbacks() default NONE.class;

  /**
   * Method data for single-method listener callbacks. Mutually exclusive with {@link #callbacks()}
   * and an error to specify more than one value.
   */
  ListenerMethod[] method() default { };

  /** Default value for {@link #callbacks()}. */
  enum NONE { }
}
