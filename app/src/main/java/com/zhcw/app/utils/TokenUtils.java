package com.zhcw.app.utils;

import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.common.StringUtils;
import com.zhcw.app.App;
import com.zhcw.app.activity.LoginActivity;
import com.zhcw.lib.utils.MMKVUtils;
import com.zhcw.lib.utils.XToastUtils;



/**
 * Token管理工具
 * 本地记录登录token 时长25分钟,服务器超时 30分钟
 */
public final class TokenUtils {


    private static String KEY_TOKEN_TIME = "KEY_TOKEN_TIME";
    private static final long OVERTIME = 1000 * 60 * 25;
    private TokenUtils() {
        MMKVUtils.init(App.getAppContext());
    }

    private static class Instance {
        private static final TokenUtils INSTANCE = new TokenUtils();
    }

    public static TokenUtils getInstance() {
        return TokenUtils.Instance.INSTANCE;
    }



    public String initToken(String token) {
        MMKVUtils.put(KEY_TOKEN_TIME,System.currentTimeMillis());
        return token;
    }

    public Boolean getKeyToken() {
        boolean hasToke;
        long time = MMKVUtils.getLong(KEY_TOKEN_TIME,0L);
        if(System.currentTimeMillis() - time >= OVERTIME){
            hasToke = false;
        }else {
            hasToke = true;
        }

        return hasToke;
    }

    /**
     * 处理登录成功的事件
     *
     * @param token 账户信息
     */
    public boolean handleLoginSuccess(String token) {
        if (!StringUtils.isEmpty(token)) {
//            MobclickAgent.onProfileSignIn("github", token);
            XToastUtils.success("登录成功！");
            initToken(token);
            return true;
        } else {
            XToastUtils.error("登录失败！");
            return false;
        }
    }

    /**
     * 处理登出的事件
     */
    public void handleLogoutSuccess() {
//        MobclickAgent.onProfileSignOff();
        //登出时，清除账号信息
        XToastUtils.success("登出成功！");
        //跳转到登录页
        ActivityUtils.startActivity(LoginActivity.class);
    }

}
