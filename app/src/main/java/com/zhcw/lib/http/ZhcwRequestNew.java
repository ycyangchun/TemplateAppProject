package com.zhcw.lib.http;

import android.text.TextUtils;

import com.xuexiang.xutil.common.logger.Logger;
import com.zhcw.app.base.Constants;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

/**
 *
 */
public class ZhcwRequestNew {

    private JSONObject getSONPostHeaderNew(String busiCode, String sysType) {
        JSONObject header = new JSONObject();
        try {
            String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
            // messengerID + "yyyyMMddHHmmss"+随机两位数
            header.put("messageID", "9999" + date + "" + (int) (Math.random() * 90 + 10));

            header.put("timeStamp", date);
            header.put("messengerID", "");
            header.put("transactionType", busiCode);
            header.put("sysType", sysType);
            header.put("src", Constants.channelId);
            header.put("imei",TextUtils.isEmpty(Constants.imeiStr) ? "no imei" : Constants.imeiStr);
            header.put("ua", TextUtils.isEmpty(Constants.uaStr) ? "no ua" : Constants.uaStr);
            header.put("resCode","");
            header.put("message", "");
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



    private JSONObject getJSONParamsNew(String busiCode, String sysType, Map<String, String> map) {
        JSONObject all = new JSONObject();
        try {
            all.put("head", getSONPostHeaderNew(busiCode,sysType));
            all.put("body", getSONPostBody(map));
        } catch (Exception ex) {

        }
        return all;
    }


    public JSONObject encryptPostParam (String url, String busiCode, String sysType, Map<String, String> map) {

        JSONObject buf = getJSONParamsNew(busiCode,sysType,map);
        Logger.dTag("JSONObject加密前url=", url + " " + buf);

        return buf;

    }



    /////////////////////////////////////////////////////////////////////////

}
