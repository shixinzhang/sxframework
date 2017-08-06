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

package top.shixinzhang.sxframework.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Set;

/**
 * <br> Description: SharedPreferences 工具类
 * <p>
 * <br> Created by shixinzhang on 17/4/21.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class SpUtils {

    private static final String DEFAULT_NAME = "shixinzhang_sp";
    private static final Object mLockObj = new Object();     // TODO: 17/4/21 改成读写锁，效率更好？

    @NonNull
    private static String mName = DEFAULT_NAME;

    private SpUtils() {
    }

    public static void saveDataInDefault(@NonNull Context context, String key, Object object) {
        saveData(context, mName, key, object);
    }

    /**
     * 保存数据
     *
     * @param context
     * @param spName
     * @param key
     * @param object
     */
    public static void saveData(@NonNull Context context, String spName, String key, @Nullable Object object) {
        if (object == null) {
            return;
        }
        synchronized (mLockObj) {
            SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            switch (object.getClass().getSimpleName()) {
                case "String":
                    editor.putString(key, (String) object);
                    break;
                case "Integer":
                    editor.putInt(key, (Integer) object);
                    break;
                case "Boolean":
                    editor.putBoolean(key, (Boolean) object);
                    break;
                case "Float":
                    editor.putFloat(key, (Float) object);
                    break;
                case "Long":
                    editor.putLong(key, (Long) object);
                    break;
                case "HashSet":
                case "Set":
                    editor.putStringSet(key, (Set<String>) object);
                    break;
            }
            editor.apply();
        }
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static Object getDataFromDefault(@NonNull Context context, String key, Object defaultValue) {
        return getData(context, mName, key, defaultValue);
    }

    /**
     * 查询数据
     *
     * @param context
     * @param spName
     * @param key
     * @param defaultValue
     */
    @Nullable
    public static Object getData(@NonNull Context context, String spName, String key, @Nullable Object defaultValue) {
        if (defaultValue == null) {
            throw new IllegalArgumentException("Default value used for get data type, so it can't be null!");
        }
        synchronized (mLockObj) {
            SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
            Object result = null;

            switch (defaultValue.getClass().getSimpleName()) {
                case "String":
                    result = sp.getString(key, (String) defaultValue);
                    break;
                case "Integer":
                    result = sp.getInt(key, (Integer) defaultValue);
                    break;
                case "Boolean":
                    result = sp.getBoolean(key, (Boolean) defaultValue);
                    break;
                case "Float":
                    result = sp.getFloat(key, (Float) defaultValue);
                    break;
                case "Long":
                    result = sp.getLong(key, (Long) defaultValue);
                    break;
                case "HashSet":
                case "Set":
                    result = sp.getStringSet(key, (Set<String>) defaultValue);
                    break;
            }
            return result;
        }
    }

    public static void removeData(Context context, String key) {
        removeData(context, key);
    }

    /**
     * 删除指定 KEY 的数据
     *
     * @param context
     * @param spName
     * @param key
     */
    public static void removeData(@Nullable Context context, String spName, String key) {
        if (context == null) {
            return;
        }
        synchronized (mLockObj) {
            SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.remove(key);
        }
    }

    /**
     * 清除数据
     *
     * @param context
     * @param spName
     */
    public static void clear(@NonNull Context context, String spName) {
        synchronized (mLockObj) {
            SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
        }
    }
}
