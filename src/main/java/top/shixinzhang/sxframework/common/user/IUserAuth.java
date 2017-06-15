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

package top.shixinzhang.sxframework.common.user;

import java.io.Serializable;

/**
 * Description:
 * <br> 用户信息的基本接口
 * <p>
 * <br> Created by shixinzhang on 17/6/13.
 */

public interface IUserAuth extends Serializable{

    //token
    String getToken();
    void setToken(String token);

    //有效期
    String getExpiredTime();
    void setExpiredTime(String expiredTime);

    //用户ID
    String getUserId();
    void setUserId(String userId);

    //登录方式，是否为验证码登录
    boolean isCodeLogin();
    void setIsCodeLogin(boolean isCodeLogin);

    // UserKey 的意义？
    String getUserKey();
    void setUserKey(String userKey);

    //密码
    String getPassword();
    void setPassword(String password);
}
