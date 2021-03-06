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

package com.zhcw.app.activity;

import android.os.Build;
import android.text.TextUtils;
import android.view.View;

import com.xuexiang.templateproject.activity.MainActivity;
import com.xuexiang.xaop.XAOP;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xaop.consts.PermissionConsts;
import com.xuexiang.xui.widget.activity.BaseSplashActivity;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.common.logger.Logger;
import com.xuexiang.xutil.system.AppExecutors;
import com.zhcw.app.App;
import com.zhcw.app.R;
import com.zhcw.app.base.Constants;
import com.zhcw.app.utils.ToastListUtil;
import com.zhcw.app.utils.TokenUtils;
import com.zhcw.lib.utils.DeviceID;
import com.zhcw.lib.utils.FileUtilSupply;
import com.zhcw.lib.utils.MMKVUtils;
import com.zhcw.lib.utils.SplashUtils;
import com.zhcw.lib.utils.ZhcwUtils;
import com.zhcw.lib.utils.manager.UserMgr;
import com.zhcw.lib.utils.sdkinit.CrashHandler;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 启动页【无需适配屏幕大小】
 *
 */
public class SplashActivity extends BaseSplashActivity implements CancelAdapt {

    @Override
    protected long getSplashDurationMillis() {
        return 500;
    }

    /**
     * activity启动后的初始化
     */
    @Override
    protected void onCreateActivity() {
        initSplashView(R.drawable.xui_config_bg_splash);
        startSplash(false);

    }


    /**
     * 启动页结束后的动作
     */
    @Override
    protected void onSplashFinished() {
        boolean isAgree = MMKVUtils.getBoolean("key_agree_privacy", false);
        if (isAgree) {
            toMain();
        } else {
            showPrivacy();
        }
    }

    //权限申请
    private void showPrivacy() {
        SplashUtils.showPrivacyDialog(this, (dialog, which) -> {
            dialog.dismiss();
            handleRequestPermission(mWelcomeLayout);
        });
        //设置动态申请权限切片 申请权限被拒绝的事件响应监听
        XAOP.setOnPermissionDeniedListener(permissionsDenied -> {
//                    XToastUtils.error("权限申请被拒绝:" + StringUtils.listToString(permissionsDenied, ","));
                    showPrivacy();
                }
        );
    }

    @SingleClick
    @Permission({PermissionConsts.STORAGE, PermissionConsts.LOCATION})
    private void handleRequestPermission(View v) {// 必须有参数
//        XToastUtils.toast("权限申请通过！");
        MMKVUtils.put("key_agree_privacy", true);
        toMain();
    }

    // 主页
    private void toMain() {
        initFileStorage();
        if (TokenUtils.getInstance().getKeyToken()) {
            ActivityUtils.startActivity(MainActivity.class);
        } else {
            ActivityUtils.startActivity(LoginActivity.class);
        }
        finish();
    }

    //初始存储目录
    private void initFileStorage() {
        FileUtilSupply.initCache();
        CrashHandler.getInstance().init(App.getAppContext());
        AppExecutors.get().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                initCacheTxt();
            }
        });

        initDeviceData();
    }

    //设备信息
    public void initDeviceData() {
        Constants.uaStr = Build.MODEL;// 手机型号
        Constants.deviceId = DeviceID.getInstance().getDeviceID();
        Constants.imeiStr = DeviceID.getInstance().getImei();
        if (TextUtils.isEmpty(Constants.imeiStr))
            Constants.imeiStr = DeviceID.getInstance().getDeviceId();

    }



    //初始cache
    private void initCacheTxt() {

        ZhcwUtils zhcwUtils = ZhcwUtils.getInstance();
        zhcwUtils.writeCacheFile("txt/1.txt", "111111112222");
        zhcwUtils.writeAssetsCacheFile("ds/k3/t1.txt");
        zhcwUtils.writeAssetsCacheFile("ds/t2.txt");
//      Logger.d(zhcwUtils.readCacheFile("ds/t3.txt"));

        Logger.d(ToastListUtil.getIT().getMV("DC101062", "默认key 11111111111111111"));
//      ToastList.getInstance().removeToast();
        Logger.d(ToastListUtil.getIT().getMV("DC101059", "默认key 22"));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
