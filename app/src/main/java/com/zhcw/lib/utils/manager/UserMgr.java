package com.zhcw.lib.utils.manager;

import android.text.TextUtils;

import com.zhcw.lib.base.bean.User;


public class UserMgr {
    private static UserMgr instance;
    private User user;
    private String jsessionid;// 登录后 head cookie(退出登录，登录超时置空)
    private boolean isLogin = false;//用户是否登录
    private String loginUserId;
    private boolean webChangeUser = false;//是否有用户切换登录 （web 界面用）
    private UserMgr() {

    }

    public static UserMgr getInstance() {
        if (instance == null) {
            instance = new UserMgr();
        }
        return instance;

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if(null != user && !TextUtils.isEmpty(user.getUserId()))
            setLogin(true);
    }

    public String getJsessionid() {
        return jsessionid;
    }

    public void setJsessionid(String jsessionid) {
        this.jsessionid = jsessionid;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
        if(null != user && login){
            loginUserId = user.getUserId();
        }else {
            loginUserId = null;
            jsessionid = null;
        }
    }

    //需要判断的界面，自己记录 loginUserId
    public boolean isWebChangeUser(String webUserId) {
        if(!TextUtils.isEmpty(webUserId) && isLogin){
            webChangeUser = !loginUserId.equals(webUserId);
        }
        return webChangeUser;
    }

    public String getLoginUserId() {
        return loginUserId;
    }

    //退出应用
    public void quitUser(){
        user = null;
        jsessionid = null;
        isLogin = false;
        loginUserId = null;
        webChangeUser = false;
    }
}
