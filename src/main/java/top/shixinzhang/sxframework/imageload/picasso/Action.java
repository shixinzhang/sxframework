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
package top.shixinzhang.sxframework.imageload.picasso;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;


abstract class Action<T> {
  static class RequestWeakReference<M> extends WeakReference<M> {
    final Action action;

    public RequestWeakReference(Action action, M referent, ReferenceQueue<? super M> q) {
      super(referent, q);
      this.action = action;
    }
  }

  @NonNull
  final Picasso picasso;
  final Request request;
  @NonNull
  final WeakReference<T> target;
  final boolean noFade;
  final int memoryPolicy;
  final int networkPolicy;
  final int errorResId;
  final Drawable errorDrawable;
  final String key;
  @NonNull
  final Object tag;

  boolean willReplay;
  boolean cancelled;

  Action(@NonNull Picasso picasso, @Nullable T target, Request request, int memoryPolicy, int networkPolicy,
         int errorResId, Drawable errorDrawable, String key, @Nullable Object tag, boolean noFade) {
    this.picasso = picasso;
    this.request = request;
    this.target =
        target == null ? null : new RequestWeakReference<T>(this, target, picasso.referenceQueue);
    this.memoryPolicy = memoryPolicy;
    this.networkPolicy = networkPolicy;
    this.noFade = noFade;
    this.errorResId = errorResId;
    this.errorDrawable = errorDrawable;
    this.key = key;
    this.tag = (tag != null ? tag : this);
  }

  abstract void complete(Bitmap result, Picasso.LoadedFrom from);

  abstract void error();

  void cancel() {
    cancelled = true;
  }

  Request getRequest() {
    return request;
  }

  @Nullable
  T getTarget() {
    return target == null ? null : target.get();
  }

  String getKey() {
    return key;
  }

  boolean isCancelled() {
    return cancelled;
  }

  boolean willReplay() {
    return willReplay;
  }

  int getMemoryPolicy() {
    return memoryPolicy;
  }

  int getNetworkPolicy() {
    return networkPolicy;
  }

  @NonNull
  Picasso getPicasso() {
    return picasso;
  }

  Picasso.Priority getPriority() {
    return request.priority;
  }

  @NonNull
  Object getTag() {
    return tag;
  }
}
