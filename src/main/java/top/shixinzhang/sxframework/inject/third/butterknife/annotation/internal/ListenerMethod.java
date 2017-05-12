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

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME) @Target(FIELD)
public @interface ListenerMethod {
  /** Name of the listener method for which this annotation applies. */
  String name();

  /** List of method parameters. If the type is not a primitive it must be fully-qualified. */
  String[] parameters() default { };

  /** Primitive or fully-qualified return type of the listener method. May also be {@code void}. */
  String returnType() default "void";

  /** If {@link #returnType()} is not {@code void} this value is returned when no binding exists. */
  String defaultReturn() default "null";
}
