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

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.xuexiang.xpage.base.XPageActivity;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.core.CoreSwitchBean;
import com.xuexiang.xrouter.facade.service.SerializationService;
import com.xuexiang.xrouter.launcher.XRouter;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.widget.slideback.SlideBack;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.app.AppUtils;
import com.xuexiang.xutil.resource.ResourceUtils;
import com.zhcw.app.base.Constants;
import com.zhcw.app.base.IConstants;
import com.zhcw.lib.utils.manager.ActivityStackManager;
import com.zhcw.lib.utils.sdkinit.CrashHandler;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

/**
 * 基础容器Activity
 *
 * @author XUE
 * @since 2019/3/22 11:21
 */
public class BaseActivity extends XPageActivity {

    Unbinder mUnbinder;
    public String Tag;
    public BaseActivity baseAct;

    @Override
    protected void attachBaseContext(Context newBase) {
        //注入字体
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    /**
     * 是否支持侧滑返回
     */
    public static final String KEY_SUPPORT_SLIDE_BACK = "key_support_slide_back";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initAppTheme();
        initStatusBarStyle();
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
        Tag = this.getLocalClassName();
        baseAct = this;
        ActivityStackManager.getScreenManager().pushActivity(baseAct);//
        initChannelSrc();
        // 侧滑回调
        if (isSupportSlideBack()) {
            SlideBack.with(this)
                    .haveScroll(true)
                    .callBack(this::popPage)
                    .register();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStackManager.getScreenManager().popActivity(baseAct);
    }

    // app 退出
    public void appQuit() {
        CrashHandler.getInstance().onDestroy();
        ActivityStackManager.getScreenManager().popActivity();
        XUtil.exitApp();
    }

    /**
     * 初始化应用的主题
     */
    protected void initAppTheme() {
        XUI.initTheme(this);
    }

    /**
     * 初始化状态栏的样式
     */
    protected void initStatusBarStyle() {

    }

    //渠道id
    public void initChannelSrc() {
        String channel = AppUtils.getStringValueInMetaData("UMENG_CHANNEL");
        if(!TextUtils.isEmpty(channel)) {
            if (channel.contains("channelid")) {
                Constants.channelId = channel.substring("channelid".length(), channel.length());
            } else {
                Constants.channelId = channel.substring(0, 10);
            }
        }
        if(TextUtils.isEmpty(Constants.channelId)) {
            Constants.channelId = ResourceUtils.readStringFromAssert("channel.txt");
            Constants.channelId = Constants.channelId.substring(0, 10);
        }
        Constants.channelId += "|" + IConstants.src_platform + IConstants.src_project + Constants.versionName;
    }


    /**
     * @return 是否支持侧滑返回
     */
    protected boolean isSupportSlideBack() {
        CoreSwitchBean page = getIntent().getParcelableExtra(CoreSwitchBean.KEY_SWITCH_BEAN);
        return page == null || page.getBundle() == null || page.getBundle().getBoolean(KEY_SUPPORT_SLIDE_BACK, true);
    }



    /**
     * 打开fragment
     *
     * @param clazz          页面类
     * @param addToBackStack 是否添加到栈中
     * @return 打开的fragment对象
     */
    public <T extends XPageFragment> T openPage(Class<T> clazz, boolean addToBackStack) {
        CoreSwitchBean page = new CoreSwitchBean(clazz)
                .setAddToBackStack(addToBackStack);
        return (T) openPage(page);
    }

    /**
     * 打开fragment
     *
     * @return 打开的fragment对象
     */
    public <T extends XPageFragment> T openNewPage(Class<T> clazz) {
        CoreSwitchBean page = new CoreSwitchBean(clazz)
                .setNewActivity(true);
        return (T) openPage(page);
    }


    /**
     * 切换fragment
     *
     * @param clazz 页面类
     * @return 打开的fragment对象
     */
    public <T extends XPageFragment> T switchPage(Class<T> clazz) {
        return changePage(clazz);
    }


    /**
     * 序列化对象
     *
     * @param object
     * @return
     */
    public String serializeObject(Object object) {
        return XRouter.getInstance().navigation(SerializationService.class).object2Json(object);
    }

    @Override
    protected void onRelease() {
        mUnbinder.unbind();
        super.onRelease();
    }

}
