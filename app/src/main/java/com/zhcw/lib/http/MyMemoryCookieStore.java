package com.zhcw.lib.http;

import com.lzy.okgo.cookie.store.CookieStore;
import com.lzy.okgo.model.HttpHeaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * ================================================
 * Cookie 的内存管理
 * ================================================
 */
public class MyMemoryCookieStore implements CookieStore {

    private final Map<String, List<Cookie>> memoryCookies = new HashMap<>();

    private MyCookieStoreListener myCookieStoreListener;

    public void setMyCookieStoreListener(MyCookieStoreListener myCookieStoreListener) {
        this.myCookieStoreListener = myCookieStoreListener;
    }

    @Override
    public synchronized void saveCookie(HttpUrl url, List<Cookie> cookies) {
        List<Cookie> oldCookies = memoryCookies.get(url.host());
        List<Cookie> needRemove = new ArrayList<>();
        for (Cookie newCookie : cookies) {
            if (newCookie.name().equals("JSESSIONID")) {
//                App.userMgr.setJsessionid(newCookie.name() + "=" + newCookie.value());
                if(null !=myCookieStoreListener)
                    myCookieStoreListener.setJsessionid(newCookie.name() + "=" + newCookie.value());
            }
            for (Cookie oldCookie : oldCookies) {
                if (newCookie.name().equals(oldCookie.name())) {
                    needRemove.add(oldCookie);
                }
            }
        }
        oldCookies.removeAll(needRemove);
        oldCookies.addAll(cookies);
        memoryCookies.put(url.host(), oldCookies);
    }

    @Override
    public synchronized void saveCookie(HttpUrl url, Cookie cookie) {
        List<Cookie> cookies = memoryCookies.get(url.host());
        List<Cookie> needRemove = new ArrayList<>();
        for (Cookie item : cookies) {
            if (cookie.name().equals(item.name())) {
                needRemove.add(item);
            }
        }
        cookies.removeAll(needRemove);
        cookies.add(cookie);
        memoryCookies.put(url.host(), cookies);
    }

    @Override
    public synchronized List<Cookie> loadCookie(HttpUrl url) {
        List<Cookie> cookies = memoryCookies.get(url.host());
        if (cookies == null) {
            cookies = new ArrayList<>();
            memoryCookies.put(url.host(), cookies);
        }
        return cookies;
    }

    @Override
    public synchronized List<Cookie> getAllCookie() {
        List<Cookie> cookies = new ArrayList<>();
        Set<String> httpUrls = memoryCookies.keySet();
        for (String url : httpUrls) {
            cookies.addAll(memoryCookies.get(url));
        }
        return cookies;
    }


    @Override
    public List<Cookie> getCookie(HttpUrl url) {
        List<Cookie> cookies = new ArrayList<>();
        List<Cookie> urlCookies = memoryCookies.get(url.host());
        if (urlCookies != null) cookies.addAll(urlCookies);
        return cookies;
    }


    public List<Cookie> getCookie(String url) {
        List<Cookie> cookies = new ArrayList<>();
        HttpUrl httpUrl = HttpUrl.get(url);
        List<Cookie> urlCookies = memoryCookies.get(httpUrl.host());
        if (urlCookies != null) cookies.addAll(urlCookies);
        return cookies;
    }

    public HttpHeaders getHttpHeaders(String url) {
        List<Cookie> cookies = new ArrayList<>();
        HttpUrl httpUrl = HttpUrl.get(url);

        List<Cookie> urlCookies = memoryCookies.get(httpUrl.host());
        if (urlCookies != null) cookies.addAll(urlCookies);
        //删除 sessionId
        Iterator<Cookie> it_b = cookies.iterator();
        while(it_b.hasNext()){
            Cookie item = it_b.next();
            if (item.name().equals("JSESSIONID")) {
                it_b.remove();
            }
        }
        //添加 sessionId

        if (null != myCookieStoreListener && myCookieStoreListener.isLogin()) {
            Cookie cookie_jsid = Cookie.parse(httpUrl, myCookieStoreListener.getJsessionid());
            cookies.add(cookie_jsid);
        }

        //设置post headers
        HttpHeaders httpHeaders = new HttpHeaders();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < cookies.size(); i++) {
            builder.append(cookies.get(i).toString());
            if (i != cookies.size() - 1) {
                builder.append(";");
            }
        }
        httpHeaders.put("Set-Cookie", builder.toString());
        return httpHeaders;
    }

    @Override
    public synchronized boolean removeCookie(HttpUrl url, Cookie cookie) {
        List<Cookie> cookies = memoryCookies.get(url.host());
        return (cookie != null) && cookies.remove(cookie);
    }

    @Override
    public synchronized boolean removeCookie(HttpUrl url) {
        return memoryCookies.remove(url.host()) != null;
    }

    @Override
    public synchronized boolean removeAllCookie() {
        memoryCookies.clear();
        return true;
    }

}
