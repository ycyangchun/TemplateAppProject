package com.zhcw.lib.http;

import android.text.TextUtils;


import com.xuexiang.xaop.XAOP;
import com.xuexiang.xaop.util.MD5Utils;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.app.AppUtils;
import com.xuexiang.xutil.common.logger.Logger;
import com.zhcw.app.base.Constants;
import com.zhcw.app.base.IConstants;
import com.zhcw.lib.BASE64.DesUtil;
import com.zhcw.lib.utils.DeviceID;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

public class ZhcwRequest {

    /**
     *  header
     * @param busiCode
     * @param sysType
     * @param md5   摘要的内容为（消息体）
     * @return
     */
    private JSONObject getSONPostHeader(String busiCode, String sysType, String md5) {
        JSONObject header = new JSONObject();
        try {
            String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
            header.put("messageID", "9999" + date + "" + (int) (Math.random() * 90 + 10));// //messengerID
            // +
            // "yyyyMMddHHmmss"+随机两位数
            header.put("timeStamp", date);
            header.put("messengerID", "9999");
            header.put("transactionType", busiCode);
            header.put("sysType", sysType);
            header.put("src", Constants.channelId);
            header.put("platform", IConstants.platform);// 10 微信平台不合法
            header.put("imei",TextUtils.isEmpty(Constants.imeiStr) ? "no imei" : Constants.imeiStr);
            header.put("ua", TextUtils.isEmpty(Constants.uaStr) ? "no ua" : Constants.uaStr);
            // 对消息包的摘要,
            // 摘要算法为MD5，摘要的内容为（messageID+timeStamp+messengerID+transactionType+投注代理密码+消息体）
            if (!TextUtils.isEmpty(md5)) {
                header.put("digest", md5);
            }
            if (TextUtils.isEmpty(Constants.deviceId)) {
                Constants.deviceId = DeviceID.getInstance().getDeviceID();
            }
            header.put("deviceId", Constants.deviceId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return header;
    }


    /**
     *  body
     * @param params
     * @return
     */
    private  JSONObject getSONPostBody(Map<String, String> params) {
        JSONObject body = new JSONObject();
        try {
            Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
            while (it.hasNext()) {
                 Map.Entry<String, String> entry = it.next();
                 body.put(entry.getKey(),entry.getValue());
             }
        } catch (Exception ex) {

        }
        return body;
    }

    /**
     *  {"message":{"head":{},"body":{}}}
     * @param busiCode
     * @param sysType
     * @param md5
     * @param map
     * @return
     */
    private JSONObject getJSONParams(String busiCode, String sysType, String md5,Map<String, String> map) {
        JSONObject all = new JSONObject();
        JSONObject params = new JSONObject();
        try {
            all.put("head", getSONPostHeader(busiCode,sysType,md5));
            all.put("body", getSONPostBody(map));
            params.put("message", all);
        } catch (Exception ex) {

        }
        return params;
    }


    //md5
    private String getPostParamsStringMD5(Map<String, String> params) {
        return MD5Utils.encode(getPostParamsString(params));
    }
    //md5 ParamsString
    private String getPostParamsString(Map<String, String> params) {
        params.put("key", "zhcwBody!@#");
        StringBuffer buf1 = new StringBuffer();
        Iterator iter = params.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            buf1.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        buf1.deleteCharAt(buf1.length() - 1);
        return buf1.toString();
    }


    // 拼接加密数据
    public Map<String, String> encryptPostParam (String url, String busiCode, String sysType, Map<String, String> map, boolean isMd5) {
        boolean isDes = Constants.isDes;
//        if(Constants.SERVER_TYPE != IConstants.SERVER_ZHENGSHI){// 过滤哪些不加密
//            isDes = false;
//        }
        String md5 = "";
        if(isMd5){
            md5 = getPostParamsStringMD5(map);
        }
        String buf = getJSONParams(busiCode,sysType,md5,map).toString();
        map.clear();// 清除后重新赋值

        if(isDes){
            map.put("encry", "1");
            map.put("transMessage", DesUtil.encryptDES(buf, DesUtil.password));
            Logger.dTag("加密前url=", url + "?encry=0&transMessage=" + buf);
        }else{
            map.put("encry", "0");
            map.put("transMessage", buf);
            Logger.dTag("不加密url=", url + "?encry=0&transMessage=" + buf);
        }
        return map;

    }



    /////////////////////////////////////////////////////////////////////////

}
