/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

package com.zhcw.lib.utils.sdkinit;

import android.app.Application;


import com.xuexiang.templateproject.MyApp;
import com.xuexiang.templateproject.core.BaseActivity;
import com.xuexiang.templateproject.utils.TokenUtils;
import com.xuexiang.templateproject.utils.XToastUtils;
import com.xuexiang.xaop.XAOP;
import com.xuexiang.xpage.AppPageConfig;
import com.xuexiang.xpage.PageConfig;
import com.xuexiang.xrouter.launcher.XRouter;
import com.xuexiang.xui.XUI;
import com.xuexiang.xutil.XUtil;
import com.zhcw.app.App;
import com.zhcw.lib.base.BaseActivity;
import com.zhcw.lib.utils.MMKVUtils;

/**
 * X系列基础库初始化
 *
 * @author xuexiang
 * @since 2019-06-30 23:54
 */
public final class XBasicLibInit {

    private XBasicLibInit() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化基础库SDK
     */
    public static void init(Application application) {
        //UI框架
        initXUI(application);

        //工具类
        initXUtil(application);

        //网络请求框架
//        initXHttp2(application);

        //页面框架
        initXPage(application);

        //切片框架
        initXAOP(application);

        //路由框架
        initRouter(application);
    }

    /**
     * 初始化XUtil工具类
     */
    private static void initXUtil(Application application) {
        XUtil.debug(App.isDebug());
        MMKVUtils.init(application);
        TokenUtils.init(application);
    }

    /**
     * 初始化XHttp2
     */
    private static void initXHttp2(Application application) {
        //初始化网络请求框架，必须首先执行
//        XHttpSDK.init(application);
        //需要调试的时候执行
//        if (App.isDebug()) {
//            XHttpSDK.debug();
//        }
//        XHttpSDK.debug(new CustomLoggingInterceptor()); //设置自定义的日志打印拦截器
        //设置网络请求的全局基础地址
//        XHttpSDK.setBaseUrl(Constants.HOST_URL_Z);
        //设置动态参数添加拦截器
//        XHttpSDK.addInterceptor(new CustomDynamicInterceptor());
        //请求失效校验拦截器
//        XHttpSDK.addInterceptor(new CustomExpiredInterceptor());
    }

    /**
     * 初始化XPage页面框架
     */
    private static void initXPage(Application application) {
        PageConfig.getInstance()
                //页面注册
                .setPageConfiguration(context -> {
                    //自动注册页面,是编译时自动生成的，build一下就出来了
                    return AppPageConfig.getInstance().getPages();
                })
                .debug(App.isDebug() ? "PageLog" : null)
                .enableWatcher(App.isDebug())
                .setContainActivityClazz(BaseActivity.class)
                .init(application);
    }

    /**
     * 初始化XAOP
     */
    private static void initXAOP(Application application) {
        XAOP.init(application);
        XAOP.debug(App.isDebug());

    }

    /**
     * 初始化XUI框架
     */
    private static void initXUI(Application application) {
        XUI.init(application);
        XUI.debug(App.isDebug());
    }

    /**
     * 初始化路由框架
     */
    private static void initRouter(Application application) {
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (App.isDebug()) {
            XRouter.openLog();     // 打印日志
            XRouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        XRouter.init(application);
    }

}
