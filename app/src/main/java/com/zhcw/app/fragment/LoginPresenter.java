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

import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.xuexiang.xormlite.InternalDataBaseRepository;
import com.xuexiang.xormlite.db.DBService;
import com.xuexiang.xutil.common.logger.Logger;
import com.xuexiang.xutil.system.AppExecutors;
import com.zhcw.app.App;
import com.zhcw.app.base.IConstants;
import com.zhcw.app.base.UiContract;
import com.zhcw.app.net.DoNetWork;
import com.zhcw.app.utils.TokenUtils;
import com.zhcw.lib.base.bean.User;
import com.zhcw.lib.db.entity.DbUser;
import com.zhcw.lib.http.ZhcwCallback;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
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
    public void toLogin(String cell, String psw) {
        Map<String, String> map = new HashMap<>();
        map.put("cell", cell);
        map.put("userPwd", psw);


        zhcwCallback = new ZhcwCallback(loginView) {

            @Override
            public void doRecode0000(String transactionType, Object obj) {
                super.doRecode0000(transactionType, obj);
                User u = (User) obj;
                Logger.e(transactionType + "  userId = " + u.getUserId());
                App.userMgr.setUser(u);
                TokenUtils.getInstance().handleLoginSuccess(u.getUserId());
                loginView.successLogin();
                AppExecutors.get().poolIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        dbData(cell, psw);
                    }
                });
            }

            @Override
            public void doRecodeError(String msg) {
                super.doRecodeError(msg);

            }
        };
        //错误处理 toast
        zhcwCallback.loadingErrToast(false);
        DoNetWork.getClient().login(loginBusiCode, map, zhcwCallback);
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

    //测试数据库
    private void dbData(String cell, String psw) {
        if(TextUtils.isEmpty(cell) || TextUtils.isEmpty(psw)) return;

        try {
            DbUser dbUser;
            DBService<DbUser> internal = InternalDataBaseRepository.getInstance().getDataBase(DbUser.class);
//            internal.deleteAll();
            List<DbUser> list = internal.queryAndOrderBy("mobile", cell, "id", false);
            if (null == list || list.size() < 1) {
                dbUser = new DbUser();
                dbUser.setMobile(cell);
                dbUser.setPassword(psw);
                dbUser.setType(1);
                internal.insert(dbUser);
            } else {
                dbUser = list.get(0);
                if(!cell.equals(dbUser.getMobile()) || !psw.equals(dbUser.getPassword())) {
                    dbUser.setMobile(cell);
                    dbUser.setPassword(psw);
                    internal.updateData(dbUser);
                }
            }

            Logger.d(internal.queryAll().toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
