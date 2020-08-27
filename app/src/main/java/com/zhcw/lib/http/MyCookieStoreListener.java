package com.zhcw.lib.http;

public interface MyCookieStoreListener {
    void setJsessionid(String newCookie);

    String getJsessionid();

    boolean isLogin();
}
