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

package top.shixinzhang.sxframework.eventsubscribe.third.rxbus.finder;


import android.support.annotation.NonNull;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import top.shixinzhang.sxframework.eventsubscribe.third.rxbus.annotation.Produce;
import top.shixinzhang.sxframework.eventsubscribe.third.rxbus.annotation.Subscribe;
import top.shixinzhang.sxframework.eventsubscribe.third.rxbus.annotation.Tag;
import top.shixinzhang.sxframework.eventsubscribe.third.rxbus.entity.EventType;
import top.shixinzhang.sxframework.eventsubscribe.third.rxbus.entity.ProducerEvent;
import top.shixinzhang.sxframework.eventsubscribe.third.rxbus.entity.SubscriberEvent;
import top.shixinzhang.sxframework.eventsubscribe.third.rxbus.thread.EventThread;

/**
 * Helper methods for finding methods annotated with {@link Produce} and {@link Subscribe}.
 */
public final class AnnotatedFinder {

    /**
     * Cache event bus producer methods for each class.
     */
    private static final ConcurrentMap<Class<?>, Map<EventType, SourceMethod>> PRODUCERS_CACHE =
            new ConcurrentHashMap<>();

    /**
     * Cache event bus subscriber methods for each class.
     */
    private static final ConcurrentMap<Class<?>, Map<EventType, Set<SourceMethod>>> SUBSCRIBERS_CACHE =
            new ConcurrentHashMap<>();

    private static void loadAnnotatedProducerMethods(@NonNull Class<?> listenerClass,
                                                     @NonNull Map<EventType, SourceMethod> producerMethods) {
        Map<EventType, Set<SourceMethod>> subscriberMethods = new HashMap<>();
        loadAnnotatedMethods(listenerClass, producerMethods, subscriberMethods);
    }

    private static void loadAnnotatedSubscriberMethods(@NonNull Class<?> listenerClass,
                                                       @NonNull Map<EventType, Set<SourceMethod>> subscriberMethods) {
        Map<EventType, SourceMethod> producerMethods = new HashMap<>();
        loadAnnotatedMethods(listenerClass, producerMethods, subscriberMethods);
    }

    /**
     * Load all methods annotated with {@link Produce} or {@link Subscribe} into their respective caches for the
     * specified class.
     */
    private static void loadAnnotatedMethods(@NonNull Class<?> listenerClass,
                                             @NonNull Map<EventType, SourceMethod> producerMethods, @NonNull Map<EventType, Set<SourceMethod>> subscriberMethods) {
        for (Method method : listenerClass.getDeclaredMethods()) {
            // The compiler sometimes creates synthetic bridge methods as part of the
            // type erasure process. As of JDK8 these methods now include the same
            // annotations as the original declarations. They should be ignored for
            // subscribe/produce.
            if (method.isBridge()) {
                continue;
            }
            if (method.isAnnotationPresent(Subscribe.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation but requires "
                            + parameterTypes.length + " arguments.  Methods must require a single argument.");
                }

                Class<?> parameterClazz = parameterTypes[0];
                if (parameterClazz.isInterface()) {
                    throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation on " + parameterClazz
                            + " which is an interface.  Subscription must be on a concrete class type.");
                }

                if ((method.getModifiers() & Modifier.PUBLIC) == 0) {
                    throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation on " + parameterClazz
                            + " but is not 'public'.");
                }

                Subscribe annotation = method.getAnnotation(Subscribe.class);
                EventThread thread = annotation.thread();
                Tag[] tags = annotation.tags();
                int tagLength = (tags == null ? 0 : tags.length);
                do {
                    String tag = Tag.DEFAULT;
                    if (tagLength > 0) {
                        tag = tags[tagLength - 1].value();
                    }
                    EventType type = new EventType(tag, parameterClazz);
                    Set<SourceMethod> methods = subscriberMethods.get(type);
                    if (methods == null) {
                        methods = new HashSet<>();
                        subscriberMethods.put(type, methods);
                    }
                    methods.add(new SourceMethod(thread, method));
                    tagLength--;
                } while (tagLength > 0);
            } else if (method.isAnnotationPresent(Produce.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 0) {
                    throw new IllegalArgumentException("Method " + method + "has @Produce annotation but requires "
                            + parameterTypes.length + " arguments.  Methods must require zero arguments.");
                }
                if (method.getReturnType() == Void.class) {
                    throw new IllegalArgumentException("Method " + method
                            + " has a return type of void.  Must declare a non-void type.");
                }

                Class<?> parameterClazz = method.getReturnType();
                if (parameterClazz.isInterface()) {
                    throw new IllegalArgumentException("Method " + method + " has @Produce annotation on " + parameterClazz
                            + " which is an interface.  Producers must return a concrete class type.");
                }
                if (parameterClazz.equals(Void.TYPE)) {
                    throw new IllegalArgumentException("Method " + method + " has @Produce annotation but has no return type.");
                }

                if ((method.getModifiers() & Modifier.PUBLIC) == 0) {
                    throw new IllegalArgumentException("Method " + method + " has @Produce annotation on " + parameterClazz
                            + " but is not 'public'.");
                }

                Produce annotation = method.getAnnotation(Produce.class);
                EventThread thread = annotation.thread();
                Tag[] tags = annotation.tags();
                int tagLength = (tags == null ? 0 : tags.length);
                do {
                    String tag = Tag.DEFAULT;
                    if (tagLength > 0) {
                        tag = tags[tagLength - 1].value();
                    }
                    EventType type = new EventType(tag, parameterClazz);
                    if (producerMethods.containsKey(type)) {
                        throw new IllegalArgumentException("Producer for type " + type + " has already been registered.");
                    }
                    producerMethods.put(type, new SourceMethod(thread, method));
                    tagLength--;
                } while (tagLength > 0);
            }
        }

        PRODUCERS_CACHE.put(listenerClass, producerMethods);
        SUBSCRIBERS_CACHE.put(listenerClass, subscriberMethods);
    }

    /**
     * This implementation finds all methods marked with a {@link Produce} annotation.
     */
    @NonNull
    static Map<EventType, ProducerEvent> findAllProducers(@NonNull Object listener) {
        final Class<?> listenerClass = listener.getClass();
        Map<EventType, ProducerEvent> producersInMethod = new HashMap<>();

        Map<EventType, SourceMethod> methods = PRODUCERS_CACHE.get(listenerClass);
        if (null == methods) {
            methods = new HashMap<>();
            loadAnnotatedProducerMethods(listenerClass, methods);
        }
        if (!methods.isEmpty()) {
            for (Map.Entry<EventType, SourceMethod> e : methods.entrySet()) {
                ProducerEvent producer = new ProducerEvent(listener, e.getValue().method, e.getValue().thread);
                producersInMethod.put(e.getKey(), producer);
            }
        }

        return producersInMethod;
    }

    /**
     * This implementation finds all methods marked with a {@link Subscribe} annotation.
     */
    @NonNull
    static Map<EventType, Set<SubscriberEvent>> findAllSubscribers(@NonNull Object listener) {
        Class<?> listenerClass = listener.getClass();
        Map<EventType, Set<SubscriberEvent>> subscribersInMethod = new HashMap<>();

        Map<EventType, Set<SourceMethod>> methods = SUBSCRIBERS_CACHE.get(listenerClass);
        if (null == methods) {
            methods = new HashMap<>();
            loadAnnotatedSubscriberMethods(listenerClass, methods);
        }
        if (!methods.isEmpty()) {
            for (Map.Entry<EventType, Set<SourceMethod>> e : methods.entrySet()) {
                Set<SubscriberEvent> subscribers = new HashSet<>();
                for (SourceMethod m : e.getValue()) {
                    subscribers.add(new SubscriberEvent(listener, m.method, m.thread));
                }
                subscribersInMethod.put(e.getKey(), subscribers);
            }
        }

        return subscribersInMethod;
    }

    private AnnotatedFinder() {
        // No instances.
    }

    private static class SourceMethod {
        private EventThread thread;
        private Method method;

        private SourceMethod(EventThread thread, Method method) {
            this.thread = thread;
            this.method = method;
        }
    }

}
