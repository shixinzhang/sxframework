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

package top.shixinzhang.sxframework.statistic.ubt.model;


import java.io.Serializable;
import java.util.Map;

import top.shixinzhang.utils.GsonUtils;

/**
 * 最细粒度的操作，包括操作事件、名称、事件、其他
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @description Edit it! Change it! Beat it! Whatever, just do it!
 */
public class UBTAction implements Serializable {

    private String action;
    private String name;
    private String timeInMills;
    private Map<String, Object> params;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeInMills() {
        return timeInMills;
    }

    public UBTAction setTimeInMills(final String timeInMills) {
        this.timeInMills = timeInMills;
        return this;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}
