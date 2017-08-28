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

package top.shixinzhang.sxframework.cache;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import top.shixinzhang.sxframework.network.third.okhttp3.CacheControl;
import top.shixinzhang.sxframework.network.third.okhttp3.Request;
import top.shixinzhang.sxframework.network.third.okhttp3.internal.Util;
import okio.Buffer;
import top.shixinzhang.sxframework.cache.imp.DiskCache;
import top.shixinzhang.sxframework.cache.imp.MemoryCache;
import top.shixinzhang.utils.GsonUtils;
import top.shixinzhang.utils.LogUtils;

/**
 * The Creator is Leone90 && E-mail: butleone@163.com
 *
 * @author Leone90
 * @date 15/12/14
 * Edit it! Change it! Beat it! Whatever!
 */
public class CacheManager {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private Context mContext;
    private final DiskCache mDiskCache;
    @NonNull
    private final MemoryCache mMemoryCache;
    private static CacheManager mCacheManager;

    /**
     * CacheManager
     * @param context context
     * @return
     */
    public static CacheManager init(@NonNull Context context) {
        mCacheManager = new CacheManager(context);
        return mCacheManager;
    }

    /**
     * CacheManager
     * @param context context
     */
    private CacheManager(@NonNull Context context) {
        mDiskCache = DiskCache.get(context);
        mMemoryCache = new MemoryCache(128);
        mContext = context;
    }

    /**
     * getCache
     * @return CacheManager
     */
    public static CacheManager getCache() {
        return mCacheManager;
    }

    /**
     * clearCache
     */
    public void clearCache() {
        mDiskCache.clear();
        clearExternalCache();
    }

    /**
     * clearExternalCache
     */
    public void clearExternalCache() {
        File cacheDir = mContext.getExternalCacheDir();
        if (cacheDir != null) {
            deleteFile(cacheDir.listFiles());
        }
    }

    /**
     * deleteFile
     * @param files files
     */
    public static void deleteFile(@Nullable File[] files) {
        if (files == null) return;
        File fileWillDelete = null;
        for (File f : files) {
            if (f.isDirectory()) {
                deleteFile(f.listFiles());
            }
            if (fileWillDelete == null) {
                fileWillDelete = new File(f.getParentFile(), ".delete");
            }
            f.renameTo(fileWillDelete);
            fileWillDelete.delete();
        }
    }

    /**
     * 保存 String数据 到 缓存中
     *
     * @param key        保存的key
     * @param value      保存的String数据
     * @param saveTime   保存的时间，单位：秒,如果<0，则不会过期
     * @param isInMemory 是不是存在内存中
     */
    private void put(@NonNull String key, @NonNull String value, int saveTime, boolean isInMemory) {
        put( key, value, saveTime, null ,isInMemory);
    }

    private void put(@NonNull String key, @NonNull String value, int saveTime, String eTag, boolean isInMemory) {
        if (saveTime == 0) return;
        if (isInMemory) {
            if (saveTime < 0) {
                mMemoryCache.put(key, value);
            } else {
                mMemoryCache.put(key, value, saveTime);
            }
        } else {
            if (saveTime < 0) {
                mDiskCache.put(key, value, eTag);
            } else {
                mDiskCache.put(key, value, saveTime, eTag);
            }
        }
    }

    /**
     * getAsString
     * @param key key
     * @return
     */
    @Nullable
    private String getAsString(String key) {
        return getAsString(key, false);
    }

    /**
     * getAsString
     * @param key key
     * @param findInMemory findInMemory
     * @return
     */
    @Nullable
    private String getAsString(@Nullable String key, boolean findInMemory) {
        if (key == null) {
            return null;
        }
        if (findInMemory) {
            return mMemoryCache.getAsString(key);
        }
        return mDiskCache.getAsString(key);
    }

    /**
     * 把请求结果缓存
     *
     * @param request  请求对象
     * @param response 请求结果
     */
    public void cacheResponse(Request request, String response) {
        cacheResponse(request, response, null);
    }

    public void cacheResponse(@Nullable Request request, @Nullable String response, String eTag) {
        if (request == null || response == null) {
            return;
        }

        CacheControl control = request.cacheControl();
        if (control == null) {
            return;
        }

        String method = request.method();
        if (TextUtils.isEmpty(method)) {
            return;
        }

        String body = "";
        if (request.body() != null && method.equals("POST")) {
            Buffer buffer = new Buffer();
            try {
                request.body().writeTo(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            body = buffer.readString(UTF8);
        }

        String urlCacheKey = Util.md5Hex(request.url() + body);

        // 如果 <= 0，则不会过期
        int maxAgeTime = control.maxAgeSeconds();
        maxAgeTime = maxAgeTime == 0 ? -1 : maxAgeTime;

        LogUtils.d("Cache", response);

        put(urlCacheKey, response, maxAgeTime, eTag, false);
    }

    /**
     * 拿出缓存请求结果
     * @param request 请求对象
     * @param clz 请求结果
     * @param <T> <T>
     * @return
     */
    @Nullable
    public <T>T getCacheResponse(Request request, Class<T> clz) {
        return getCacheResponse(request, (Type) clz);
    }

    /**
     * 拿出缓存请求结果
     * @param request 请求对象
     * @param type type
     * @param <T> <T>
     * @return
     */
    @Nullable
    public <T>T getCacheResponse(@Nullable Request request, @NonNull Type type) {
        if (request == null) {
            return null;
        }

        String method = request.method();
        if (TextUtils.isEmpty(method)) {
            method = "";
        }

        String body = "";
        if (request.body() != null && method.equals("POST")) {
            Buffer buffer = new Buffer();
            try {
                request.body().writeTo(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            body = buffer.readString(UTF8);
        }

        String urlCacheKey = Util.md5Hex(request.url() + body);

        String responseStr = getAsString(urlCacheKey);
        if (TextUtils.isEmpty(responseStr)) {
            return null;
        }

        TypeAdapter<?> adapter = GsonUtils.getGson().getAdapter(TypeToken.get(type));
        T result = null;
        try {
            result = (T) adapter.fromJson(responseStr);
        } catch (@NonNull IOException | ClassCastException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public IDiskCache.Entry getEntry(Request request) {
        String urlCacheKey = getCacheKey( request );
        return mDiskCache.getEntry(urlCacheKey);
    }

    @Nullable
    private String getCacheKey(@Nullable Request request) {
        if( request == null )
            return null;

        String body = "";
        if (request.body() != null && request.method().equals("POST")) {
            Buffer buffer = new Buffer();
            try {
                request.body().writeTo(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            body = buffer.readString(UTF8);
        }

        return Util.md5Hex(request.url() + body);
    }
    /**
     * 移除cache
     * @param key
     */
    public void remove(@NonNull String key ) {
        mMemoryCache.remove( key );
        mDiskCache.remove( key );
    }

    public void remove( Request request ) {
        remove( getCacheKey(request) );
    }
}
