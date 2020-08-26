package com.zhcw.app.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xutil.display.Colors;
import com.zhcw.app.fragment.LoginFragment;
import com.zhcw.lib.base.BaseActivity;

public class LoginActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openPage(LoginFragment.class, getIntent().getExtras());
    }

    @Override
    protected void initStatusBarStyle() {
        super.initStatusBarStyle();
        StatusBarUtils.initStatusBarStyle(this, false, Colors.TRANSPARENT);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return KeyboardUtils.onDisableBackKeyDown(keyCode) && super.onKeyDown(keyCode, event);
    }
}
