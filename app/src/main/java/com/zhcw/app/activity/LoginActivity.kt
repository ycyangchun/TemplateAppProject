package com.zhcw.app.activity

import android.os.Bundle
import com.xuexiang.xui.utils.StatusBarUtils
import com.xuexiang.xutil.display.Colors
import com.zhcw.app.fragment.LoginFragment
import com.zhcw.lib.base.BaseActivity

/**
 * 登录
 */
class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openPage(LoginFragment::class.java)
        //        PageOption.to(LoginFragment.class).open(this);
    }

    override fun initStatusBarStyle() {
        super.initStatusBarStyle()
        StatusBarUtils.initStatusBarStyle(this, false, Colors.TRANSPARENT)
    }
}