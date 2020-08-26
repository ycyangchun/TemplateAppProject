/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.zhcw.lib.mvvm;

import com.zhcw.lib.base.bean.User;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * 替代 UserMgr
 */
public class UserViewModel extends ViewModel {
    private MutableLiveData<User> user;
    private MutableLiveData<String> jsessionid;// 登录后 head cookie(退出登录，登录超时置空)
    private MutableLiveData<Boolean> isLogin;//用户是否登录
    private MutableLiveData<String> loginUserId;
    private MutableLiveData<Boolean>  webChangeUser ;//是否有用户切换登录 （web 界面用）


}
