package com.zhcw.app.activity;

import android.os.Bundle;

import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xutil.display.Colors;
import com.zhcw.app.fragment.LoginFragment;
import com.zhcw.lib.base.BaseActivity;

public class LoginActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openPage(LoginFragment.class);
//        PageOption.to(LoginFragment.class).open(this);
    }

    @Override
    protected void initStatusBarStyle() {
        super.initStatusBarStyle();
        StatusBarUtils.initStatusBarStyle(this, false, Colors.TRANSPARENT);
    }

}
