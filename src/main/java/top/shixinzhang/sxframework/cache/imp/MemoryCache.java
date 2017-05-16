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

package top.shixinzhang.sxframework.cache.imp;

import android.support.v4.util.LruCache;

import java.util.Map;

import top.shixinzhang.sxframework.cache.IMemoryCache;

/**
 * The Creator is Leone90 && E-mail: butleone@163.com
 *
 * @author Leone90
 * @date 15/12/14
 * Edit it! Change it! Beat it! Whatever!
 */
public class MemoryCache implements IMemoryCache{
    private final LruCache<String, Value> memoryCache;

    public MemoryCache(int count) {
        memoryCache = new LruCache<>(count);
    }

    private void checkMemoryCacheValid(){
        try {
            for(Map.Entry<String, Value> entry : memoryCache.snapshot().entrySet()){
                if(entry.getValue()==null || !entry.getValue().isValid()){
                    memoryCache.remove(entry.getKey());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void put(String key, String value) {
        checkMemoryCacheValid();
        memoryCache.put(key, new Value(value));
    }

    public void put(String key, String value, int saveTime) {
        checkMemoryCacheValid();
        memoryCache.put(key, new Value(value, saveTime));
    }

    public String getAsString(String key) {
        checkMemoryCacheValid();
        Value value = memoryCache.remove(key);
        if(value!=null){
            if(value.isValid()){
                memoryCache.put(key, value);
                return value.getValue();
            }
        }
        return null;
    }

    /**
     * 移除某个cache
     * @param key
     */
    public void remove( String key ) {
        memoryCache.remove( key );
    }

    class Value{
        String value;
        Long invalidTime;

        Value(String value) {
            this.value = value;
        }
        /**
         * @param saveTime 要保存的时间，单位：秒
         */
        Value(String value, int saveTime) {
            this.value = value;
            if(saveTime>0){
                this.invalidTime = saveTime * 1000 + System.currentTimeMillis();
            }
        }
        boolean isValid(){
            if(invalidTime==null) return true;
            return System.currentTimeMillis() < invalidTime;
        }

        public String getValue() {
            return value;
        }
    }
}
