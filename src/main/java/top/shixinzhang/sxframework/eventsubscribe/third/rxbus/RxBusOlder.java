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

package top.shixinzhang.sxframework.eventsubscribe.third.rxbus;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

@Deprecated
public class RxBusOlder {
    private static final boolean DEBUG = true;

    private static RxBusOlder sInstance;

    private ConcurrentHashMap<Object, List<Subject>> mSubjectsMapper = new ConcurrentHashMap<>();

    public static synchronized RxBusOlder instance() {
        if (sInstance == null) {
            sInstance = new RxBusOlder();
        }
        return sInstance;
    }

    private RxBusOlder() {
    }

    public <T> Observable<T> register(@NonNull Object tag, @NonNull Class<T> clazz) {
        List<Subject> subjectList = mSubjectsMapper.get(tag);
        if (subjectList == null) {
            subjectList = new ArrayList<>();
            mSubjectsMapper.put(tag, subjectList);
        }

        Subject<T, T> subject = new SerializedSubject<>(PublishSubject.<T>create());
        subjectList.add(subject);
        if (DEBUG) {
//            Timber.d("[register] mSubjectsMapper: " + mSubjectsMapper);
        }
        return subject;
    }

    public void unregister(@NonNull Object tag, @NonNull Observable observable) {
        List<Subject> subjects = mSubjectsMapper.get(tag);
        if (subjects != null) {
            subjects.remove(observable);
            if (subjects.isEmpty()) {
                mSubjectsMapper.remove(tag);
            }
            if (DEBUG) {
//                Timber.d("[unregister] mSubjectsMapper: " + mSubjectsMapper);
            }
        }
    }

    public void post(@NonNull Object tag, @NonNull Object content) {
        List<Subject> subjects = mSubjectsMapper.get(tag);
        if (subjects != null && !subjects.isEmpty()) {
            for (Subject subject : subjects) {
                subject.onNext(content);
            }
        }
        if (DEBUG) {
//            Timber.d("[send] mSubjectsMapper: " + mSubjectsMapper);
        }
    }

}
