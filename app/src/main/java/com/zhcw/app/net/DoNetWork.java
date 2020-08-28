package com.zhcw.app.net;


import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Response;
import com.zhcw.app.App;
import com.zhcw.app.base.Constants;
import com.zhcw.app.base.IConstants;
import com.zhcw.lib.base.bean.BaseBean;
import com.zhcw.lib.base.bean.User;
import com.zhcw.lib.http.JsonCallback;
import com.zhcw.lib.http.MyMemoryCookieStore;
import com.zhcw.lib.http.StringDecipherCallback;
import com.zhcw.lib.http.ZhcwCallback;
import com.zhcw.lib.http.ZhcwRequest;
import com.zhcw.lib.http.ZhcwRequestNew;
import com.zhcw.lib.utils.JsonUtils;
import com.zhcw.lib.utils.XToastUtils;

import org.json.JSONObject;

import java.util.Map;

public class DoNetWork {
    private static DoNetWork netWork;
    private static ZhcwRequest request;
    private static ZhcwRequestNew requestNew;
    private static MyMemoryCookieStore store;
    public static DoNetWork getClient() {
        if (netWork == null) {
            netWork = new DoNetWork();
        }
        return netWork;
    }

    private DoNetWork() {
        request = new ZhcwRequest();
        requestNew = new ZhcwRequestNew();
        store = App.getMemoryCookieStore();
    }

    public static void destory() {
        netWork = null;
        request = null;
        requestNew = null;
    }


    /**
     * 登录
     */
    public void login(String busiCode, Map<String, String> map, ZhcwCallback zhcwCallback){
        String url = Constants.HOST_URL;

        JsonCallback<BaseBean<User>> jsonCallback = new JsonCallback<BaseBean<User>>() {};
        jsonCallback.setCallbackHandler(zhcwCallback);

        post(url, busiCode, IConstants.SYS_TYPE_U, map, jsonCallback);
    }



    /////////////////// 测试用 /////////////////////////

    public void post(String busiCode, Map<String, String> map){
        String url = Constants.HOST_URL;
        post(url, busiCode, IConstants.SYS_TYPE_U, map);
    }


    public void post(String url,String busiCode, String sysType,Map<String, String> map){

        StringDecipherCallback stringDecipherCallback = new StringDecipherCallback(){
            @Override
            public void onSuccess(Response<String> response) {
                System.out.println(response.body());
            }
        };
        post(url, busiCode, sysType, map, stringDecipherCallback);
    }


    /**
     * post
     * @param url
     * @param busiCode
     * @param sysType
     * @param map
     * @param callback
     */
    public void post(String url, String busiCode, String sysType, Map<String, String> map, Callback callback){
        map = request.encryptPostParam(url,busiCode,sysType, map, false);

        OkGo.<String>post(url)//
                .headers(store.getHttpHeaders(url))
                .tag(busiCode)//
                .params(map,true)
                .execute(callback);
    }

    /**
     * post   application/json;charset=utf-8
     * @param url
     * @param busiCode
     * @param sysType
     * @param map
     * @param callback
     */
    public void postJson(String url, String busiCode, String sysType, Map<String, String> map, Callback callback){
        JSONObject job = requestNew.encryptPostParam(url,busiCode, sysType, map);
        OkGo.<String>post(url)//
                .headers(store.getHttpHeaders(url))
                .tag(busiCode)//
                .upJson(job)
                .execute(callback);
    }



    public void login(String busiCode, Map<String, String> map){

        StringDecipherCallback stringDecipherCallback = new StringDecipherCallback(){
            @Override
            public void onSuccess(Response<String> response) {
                //测试
                try {
                    User user = JsonUtils.unifiedBeanToBody(response.body() ,new TypeToken<BaseBean<User>>() {}.getType());
                    App.userMgr.setUser(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
//        String url = "http://g.zhcw.com/client.do"; //刮刮乐
//        String url = "http://192.168.35.233:8050/protocol.do";//福彩乐透 开发
        String url = "http://lotuc.zhcw.com/protocol.do";// 福彩乐透用户中心正式
        post(url, busiCode, IConstants.SYS_TYPE_U, map,stringDecipherCallback);
    }

    public void test(String busiCode, Map<String, String> map){

        String url = "http://g1.zhcw.com/client.do";
        post(url, busiCode, IConstants.SYS_TYPE_M, map);
    }

    public void adList(String busiCode, Map<String, String> map){

        String url = "http://192.168.35.102:8080/j";
        postJson(url, busiCode, IConstants.SYS_TYPE_U1, map, new JsonCallback<String>() {
            @Override
            public void onSuccess(Response<String> response) {
                super.onSuccess(response);
            }
        });
    }



}
