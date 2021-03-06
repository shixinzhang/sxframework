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
import android.support.annotation.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import rx.Observable;
import rx.Subscriber;
import top.shixinzhang.sxframework.eventsubscribe.third.rxbus.thread.EventThread;

/**
 * Wraps a 'producer' method on a specific object.
 * <p/>
 * <p> This class only verifies the suitability of the method and event type if something fails.  Callers are expected
 * to verify their uses of this class.
 */
public class ProducerEvent extends Event {

    /**
     * Object sporting the producer method.
     */
    @Nullable
    private final Object target;
    /**
     * Producer method.
     */
    @Nullable
    private final Method method;
    /**
     * Producer thread
     */
    private final EventThread thread;
    /**
     * Object hash code.
     */
    private final int hashCode;
    /**
     * Should this producer produce events
     */
    private boolean valid = true;

    public ProducerEvent(@Nullable Object target, @Nullable Method method, EventThread thread) {
        if (target == null) {
            throw new NullPointerException("EventProducer target cannot be null.");
        }
        if (method == null) {
            throw new NullPointerException("EventProducer method cannot be null.");
        }

        this.target = target;
        this.thread = thread;
        this.method = method;
        method.setAccessible(true);

        // Compute hash code eagerly since we know it will be used frequently and we cannot estimate the runtime of the
        // target's hashCode call.
        final int prime = 31;
        hashCode = (prime + method.hashCode()) * prime + target.hashCode();
    }

    public boolean isValid() {
        return valid;
    }

    /**
     * If invalidated, will subsequently refuse to produce events.
     * <p/>
     * Should be called when the wrapped object is unregistered from the Bus.
     */
    public void invalidate() {
        valid = false;
    }

    /**
     * Invokes the wrapped producer method and produce a {@link Observable}.
     */
    public Observable produce() {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(@NonNull Subscriber<? super Object> subscriber) {
                try {
                    subscriber.onNext(produceEvent());
                    subscriber.onCompleted();
                } catch (InvocationTargetException e) {
                    throwRuntimeException("Producer " + ProducerEvent.this + " threw an exception.", e);
                }
            }
        }).subscribeOn(EventThread.getScheduler(thread));
    }

    /**
     * Invokes the wrapped producer method.
     *
     * @throws IllegalStateException     if previously invalidated.
     * @throws InvocationTargetException if the wrapped method throws any {@link Throwable} that is not
     *                                   an {@link Error} ({@code Error}s are propagated as-is).
     */
    private Object produceEvent() throws InvocationTargetException {
        if (!valid) {
            throw new IllegalStateException(toString() + " has been invalidated and can no longer produce events.");
        }
        try {
            return method.invoke(target);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Error) {
                throw (Error) e.getCause();
            }
            throw e;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "[EventProducer " + method + "]";
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProducerEvent other = (ProducerEvent) obj;
        return method.equals(other.method) && target == other.target;
    }

    @Nullable
    public Object getTarget() {
        return target;
    }
}
