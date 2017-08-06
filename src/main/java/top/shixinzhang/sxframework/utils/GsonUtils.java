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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @date 15/11/9
 * @description Edit it! Change it! Beat it! Whatever, just do it!
 */
public class GsonUtils {

    @NonNull
    private static Gson mGson = new GsonBuilder().disableHtmlEscaping().create();

    @NonNull
    public static Gson getGson() {
        return mGson;
    }

    /**
     * fromJson
     *
     * @param json json
     * @param c    c
     * @param <T>  <T>
     * @return T
     */
    public static <T> T fromJson(String json, @NonNull Class<T> c) {
        return mGson.fromJson(json, c);
    }

    /**
     * fromJson
     *
     * @param json json
     * @param c    c
     * @param <T>  <T>
     * @return T
     */
    public static <T> T fromJson(JsonElement json, @NonNull Class<T> c) {
        return mGson.fromJson(json, c);
    }

    /**
     * fromJson
     *
     * @param json json
     * @param type type
     * @param <T>  <T>
     * @return T
     */
    public static <T> T fromJson(JsonElement json, Type type) {
        return mGson.fromJson(json, type);
    }

    /**
     * fromJson
     *
     * @param json json
     * @param type type
     * @param <T>  <T>
     * @return T
     */
    public static <T> T fromJson(String json, Type type) {
        return mGson.fromJson(json, type);
    }

    /**
     * fromJson
     *
     * @param json json
     * @param type type
     * @param <T>  <T>
     * @return T
     */
    public static <T> T fromJson(@NonNull Reader json, Type type) {
        return mGson.fromJson(json, type);
    }

    /**
     * toJson
     *
     * @param src src
     * @return String
     */
    public static String toJson(Object src) {
        return mGson.toJson(src);
    }

    /**
     * 字符串转jsonobject
     * @param string
     * @return
     */
    public static JsonObject toJsonObject(String string) {
        if(TextUtils.isEmpty(string))
            return null;

        JsonParser jp = new JsonParser();
        try {
            JsonElement element = jp.parse(string);
            if (null != element)
                return element.getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JsonArray toJsonArray(String s) {
        if (TextUtils.isEmpty(s))
            return null;

        JsonParser jp = new JsonParser();
        try {
            JsonElement element = jp.parse(s);
            if (null != element)
                return element.getAsJsonArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 转换jsonArray为jsonObject
     * @param jsonArray
     * @return
     */
    @NonNull
    public static JsonObject getJsonObjectFromArray(@Nullable JsonArray jsonArray) {
        JsonObject jsonObject = new JsonObject();
        if (null == jsonArray || jsonArray.size() <= 0)
            return jsonObject;

        Iterator<JsonElement> iterator = jsonArray.iterator();
        Set<Map.Entry<String, JsonElement>> entries;
        Iterator<Map.Entry<String,JsonElement>> entryIterator;
        Map.Entry<String,JsonElement> temp;

        while (iterator.hasNext()) {
            entries = iterator.next().getAsJsonObject().entrySet();

            if (null != entries && !entries.isEmpty()) {
                entryIterator = entries.iterator();
                if (null == entryIterator)
                    break;

                while (entryIterator.hasNext()) {
                    temp = entryIterator.next();
                    jsonObject.add(temp.getKey(), temp.getValue());
                }
            }
        }
        return jsonObject;
    }
}
