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

package top.shixinzhang.sxframework.eventsubscribe.third.rxbus.entity;

public class EventType {

    /**
     * Event Tag
     */
    private final String tag;

    /**
     * Event Clazz
     */
    private final Class<?> clazz;
    /**
     * Object hash code.
     */
    private final int hashCode;


    public EventType(String tag, Class<?> clazz) {
        if (tag == null) {
            throw new NullPointerException("EventType Tag cannot be null.");
        }
        if (clazz == null) {
            throw new NullPointerException("EventType Clazz cannot be null.");
        }

        this.tag = tag;
        this.clazz = clazz;

        // Compute hash code eagerly since we know it will be used frequently and we cannot estimate the runtime of the
        // target's hashCode call.
        final int prime = 31;
        hashCode = (prime + tag.hashCode()) * prime + clazz.hashCode();
    }

    @Override
    public String toString() {
        return "[EventType " + tag + " && " + clazz + "]";
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final EventType other = (EventType) obj;

        return tag.equals(other.tag) && clazz == other.clazz;
    }

}