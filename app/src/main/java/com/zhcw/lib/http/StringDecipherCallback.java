package com.zhcw.lib.http;

import android.text.TextUtils;

import com.lzy.okgo.request.base.Request;
import com.lzy.okgo.utils.IOUtils;
import com.xuexiang.xutil.common.logger.Logger;
import com.zhcw.app.base.Constants;
import com.zhcw.app.base.IConstants;
import com.zhcw.lib.BASE64.DesUtil;

import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * ================================================
 * 解密callback
 * ================================================
 */
public abstract class StringDecipherCallback extends com.lzy.okgo.callback.StringCallback {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    public StringDecipherCallback(){

    }

    @Override
    public void onStart(Request<String, ? extends Request> request) {

    }

    @Override
    public String convertResponse(Response response) throws Throwable {
        boolean isDes = Constants.isDes;
        if(Constants.SERVER_TYPE != IConstants.SERVER_ZHENGSHI){// 过滤哪些不加密
            isDes = false;
        }
        ResponseBody responseBody = response.body();
        byte[] bytesOriginal = IOUtils.toByteArray(responseBody.byteStream());//读取后需要从新装入，否则ResponseBody为null
        MediaType contentType = responseBody.contentType();
        String original = new String(bytesOriginal, getCharset(contentType));

        if(isDes){//解密
            String decrypt = DesUtil.decryptDES(original, DesUtil.password);
            if (!TextUtils.isEmpty(decrypt)) {
                original = decrypt;
            }
        }

        byte[] bytes = IOUtils.toByteArray(original);
        responseBody = ResponseBody.create(responseBody.contentType(), bytes);
        response =  response.newBuilder().body(responseBody).build();

        if(isDes){
            Logger.dTag("解密recode 后再封装到 response=", new String(bytes));
        } else{
            Logger.dTag("原始recode=", original);
        }
        return super.convertResponse(response);
    }

    @Override
    public void onFinish() {

    }

    private static Charset getCharset(MediaType contentType) {
        Charset charset = contentType != null ? contentType.charset(UTF8) : UTF8;
        if (charset == null) charset = UTF8;
        return charset;
    }
}
