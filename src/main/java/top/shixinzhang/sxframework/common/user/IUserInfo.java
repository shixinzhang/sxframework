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
 * <br> 封装一层固定的用户信息接口，将使用入口收到这里，便于后面修改
 * <p>
 * <br> Created by shixinzhang on 17/6/13.
 */

public interface IUserInfo extends Serializable{
    //用户ID
    String getUserId();
    void setUserId(String userId);

    //用户名
    String getUserName();
    void setUserName(String userName);

    //昵称
    String getNickName();
    void setNickName(String nickName);

    //真实名称
    String getRealName();
    void setRealName(String realName);

    //手机号
    String getPhoneNumber();
    void setPhoneNumber(String phoneNumber);

    //邮箱
    String getEmail();
    void setEmail(String email);

    //性别
    String getGender();
    void setGender(String gender);

    //生日
    String getBirthday();
    void setBirthday(String birthday);

    //头像地址
    String getHeadPortrait();
    void setHeadPortrait(String portrait);

    //身份证
    String getIdCard();
    void setIdCard(String idCard);
}
