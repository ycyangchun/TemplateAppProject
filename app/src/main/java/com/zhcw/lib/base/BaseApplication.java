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

package com.zhcw.lib.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.xuexiang.xutil.common.logger.Logger;
import com.zhcw.app.BuildConfig;
import com.zhcw.lib.utils.FileUtilSupply;
import com.zhcw.lib.utils.sdkinit.ANRWatchDogInit;
import com.zhcw.lib.utils.sdkinit.CrashHandler;
import com.zhcw.lib.utils.sdkinit.UMengInit;
import com.zhcw.lib.utils.sdkinit.XBasicLibInit;
import com.zhcw.lib.utils.sdkinit.XUpdateInit;

import androidx.multidex.MultiDex;

/**
 * @author xuexiang
 * @since 2018/11/7 下午1:12
 */
public class BaseApplication extends Application {

    private static Context GLOBAL_APP_CONTEXT;
    private int activityNumber = 0;//当前Activity个数
    private boolean isForeground = false;//应用是否处于前端
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //解决4.x运行崩溃的问题
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLibs();
        //前后台
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }


    /**
     * 初始化基础库
     */
    private void initLibs() {

        XBasicLibInit.init(this);

        XUpdateInit.init(this);

        //运营统计数据运行时不初始化
        if (!BaseApplication.isDebug()) {
            UMengInit.init(this);
        }

        //ANR监控
        ANRWatchDogInit.init();
    }


    /**
     * @return 当前app是否是调试开发模式
     */
    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    /**
     *
     * @return
     */
    public static Context getAppContext() {
        return GLOBAL_APP_CONTEXT;
    }

    /**
     * Activity 生命周期监听，用于监控app前后台状态切换
     */
    ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (activityNumber == 0) {
                Logger.i("app回到前台");
                isForeground = true;
            }
            activityNumber++;
            Logger.i("activityNumber = " + activityNumber);
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            activityNumber--;
            Logger.i("activityNumber = " + activityNumber);
            if (activityNumber == 0) {
                // app回到后台
                Logger.i("app回到后台");
                isForeground = false;
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    };
}
