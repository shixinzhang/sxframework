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

package top.shixinzhang.sxframework.views;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.widget.RadioGroup;


/**
 * Description:
 * <br>
 * <p>
 * <br> Created by shixinzhang on 17/10/28.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <br> https://about.me/shixinzhang
 */
public abstract class NavigationFragmentBinder implements RadioGroup.OnCheckedChangeListener {

    final FragmentManager mManager;
    final int mContainId;
    int mLastCheckId = -1;
    final SparseArray<Fragment> mBindMap;

    /**
     * RadioFragmentBinder
     *
     * @param manager   manager
     * @param containId containId
     */
    public NavigationFragmentBinder(FragmentManager manager, int containId) {
        this.mManager = manager;
        this.mContainId = containId;
        mBindMap = new SparseArray<>();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId < 0 || checkedId == mLastCheckId) {
            return;
        }
        mLastCheckId = checkedId;

        FragmentTransaction fTra = mManager.beginTransaction();

        if (mManager.getFragments() != null) {
            for (Fragment fg : mManager.getFragments()) {
                if (fg != null && fg.getId() == mContainId) {
                    fTra.hide(fg);
                }
            }
        }

        for (int i = 0, size = mBindMap.size(); i < size; i++) {
            Fragment fg = mBindMap.valueAt(i);
            if (fg != null) {
                fTra.hide(fg);
            }
        }

        Fragment fg = getFragment(checkedId);

        if (!fg.isAdded()) {
            fTra.add(mContainId, fg, checkedId + "");
        }
        fTra.show(fg).commitAllowingStateLoss();

        onFragmentChecked(checkedId, fg);
    }

    /**
     * 获取fragment
     *
     * @param checkId checkId
     * @return
     */
    public Fragment getFragment(int checkId) {
        Fragment fg = mBindMap.get(checkId);

        if (fg == null) {
            fg = mManager.findFragmentByTag("" + checkId);
        }

        if (fg == null) {
            fg = bindFragment(checkId);
        }

        if (fg != null) {
            mBindMap.put(checkId, fg);
        }

        return fg;
    }

    /**
     * 对应Radio的Fragment初始化
     */
    public abstract Fragment bindFragment(int checkedId);

    /**
     * 对应Radio被点击后回调
     */
    public abstract void onFragmentChecked(int checkedId, Fragment fragment);
}
