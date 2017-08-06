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

package top.shixinzhang.sxframework.utils.encrypt;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.apache.commons.codec.binary.Base64;

/**
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @date 15/11/9
 * @description Edit it! Change it! Beat it! Whatever, just do it!
 */
public class Base64Utils {

    @NonNull
    private static Base64 mBase64 = new Base64();

    /**
     * encode
     * @param source source
     * @return String
     */
    @NonNull
    public static String encode(@NonNull String source) {
        if (!TextUtils.isEmpty(source)) {
            return new String(mBase64.encode(source.getBytes()));
        }
        return "";
    }

    /**
     * decode
     * @param source source
     * @return String
     */
    @NonNull
    public static String decode(@NonNull String source) {
        if (!TextUtils.isEmpty(source)) {
            return new String(mBase64.decode(source.getBytes()));
        }
        return "";
    }

    /**
     * encode
     * @param source source
     * @return String
     */
    @NonNull
    public static String encodeByte(@Nullable byte[] source) {
        if (source != null) {
            return new String(mBase64.encode(source));
        }
        return "";
    }

    /**
     * encode
     * @param source source
     * @return String
     */
    @Nullable
    public static byte[] encodeToByte(@Nullable byte[] source) {
        if (source != null) {
            return mBase64.encode(source);
        }
        return null;
    }

    /**
     * decode
     * @param source source
     * @return String
     */
    public static byte[] decodeByte(@NonNull String source) {
        if (!TextUtils.isEmpty(source)) {
            return mBase64.decode(source.getBytes());
        }
        return null;
    }
}
