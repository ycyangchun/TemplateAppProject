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

package com.zhcw.app.fragment;

import com.zhcw.app.base.UiContract;

public class LoginPresenter extends UiContract.LoginPresenter {

    UiContract.LoginView loginView;

    public LoginPresenter(UiContract.LoginView loginView) {
        super(loginView);
        this.loginView = loginView;
        this.loginView.setPresenter(this);
    }


    @Override
    public void toLogin(String userName, String psw) {

    }


    @Override
    public void onStart() {

    }
}
