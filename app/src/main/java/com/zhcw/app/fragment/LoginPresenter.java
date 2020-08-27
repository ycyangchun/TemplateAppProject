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

import com.lzy.okgo.OkGo;
import com.xuexiang.xutil.common.logger.Logger;
import com.zhcw.app.App;
import com.zhcw.app.base.IConstants;
import com.zhcw.app.base.UiContract;
import com.zhcw.app.net.DoNetWork;
import com.zhcw.lib.base.bean.User;
import com.zhcw.lib.http.ZhcwCallback;

import java.util.HashMap;
import java.util.Map;

public class LoginPresenter extends UiContract.LoginPresenter {

    private UiContract.LoginView loginView;
    private ZhcwCallback zhcwCallback;
    private final String loginBusiCode = "10020102";

    public LoginPresenter(UiContract.LoginView loginView) {
        super(loginView);
        this.loginView = loginView;
        this.loginView.setPresenter(this);
    }


    @Override
    public void toLogin(String userName, String psw) {
        Map<String,String> map = new HashMap<>();
        map.put("cell","17600145229");
        map.put("userPwd", "123456");


        zhcwCallback = new ZhcwCallback(loginView){

            @Override
            public void doRecode0000(String transactionType, Object obj) {
                super.doRecode0000(transactionType, obj);
                User u = (User)obj;
                Logger.e(transactionType + "   " + u.getUserId());
                App.userMgr.setUser(u);
                loginView.successLogin();
            }

            @Override
            public void doRecodeError(String msg) {
                super.doRecodeError(msg);

            }
        };
        //错误处理 toast
        zhcwCallback.loadingErrToast(false);
        DoNetWork.getClient().login(loginBusiCode, map,zhcwCallback);
    }


    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        zhcwCallback.onDestroy();
        OkGo.getInstance().cancelTag(loginBusiCode);
    }
}
