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

package com.zhcw.app.base;

import com.zhcw.lib.mvp.IView;
import com.zhcw.lib.mvp.ZIPresenter;

/**
 * ui 绑定契约
 */
public class UiContract {

    public interface LoginView extends IView<LoginPresenter>{

        //登录
        void toLogin(String cell, String psw);

        //登录成功
        void successLogin();

        //忘记密码
        void forgetPassword();

        //显示密码
        void showPassword();

    }

    public abstract static class LoginPresenter extends ZIPresenter {

        public LoginPresenter(LoginView loginView) {
            super(loginView);
        }

        //登录
        public abstract void toLogin(String cell, String psw);

    }
}
