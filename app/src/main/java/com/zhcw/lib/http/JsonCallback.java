
package com.zhcw.lib.http;

import android.text.TextUtils;

import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.base.Request;
import com.lzy.okgo.utils.IOUtils;
import com.xuexiang.xutil.common.logger.Logger;
import com.zhcw.app.base.Constants;
import com.zhcw.lib.BASE64.DesUtil;
import com.zhcw.lib.base.bean.BaseBean;
import com.zhcw.lib.base.bean.BaseMessage;


import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import androidx.annotation.NonNull;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 描    述：默认将返回的数据解析成需要的Bean,可以是 BaseBean，String，List，Map
 * 修订历史：
 * ================================================
 */
public abstract class JsonCallback<T> extends AbsCallback<T> {

    private Type type;
    private Class<T> clazz;
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public JsonCallback() {
    }

    public JsonCallback(Type type) {
        this.type = type;
    }

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    private CallbackHandler callbackHandler;// handler

    public JsonCallback setCallbackHandler(CallbackHandler callbackHandler) {
        this.callbackHandler = callbackHandler;
        return this;
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        // 主要用于在所有请求之前添加公共的请求头或请求参数
        // 例如登录授权的 token
        // 使用的设备信息
        // 可以随意添加,也可以什么都不传
        // 还可以在这里对所有的参数进行加密，均在这里实现
//        request.headers("header1", "HeaderValue1")//
//                .params("params1", "ParamsValue1")//
//                .params("token", "3215sdf13ad1f65asd4f3ads1f");
        if (null != callbackHandler)
            callbackHandler.doRecodeStart((String) request.getTag());
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象,生产onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertResponse(Response response) throws Throwable {

        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用

        //详细自定义的原理和文档，看这里： https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback
        boolean isDes = Constants.isDes;
//        if(Constants.SERVER_TYPE != IConstants.SERVER_ZHENGSHI){// 过滤哪些不加密
//            isDes = false;
//        }
        if (isDes || Constants.isLog)
            response = decryptResponse(response, isDes);

        if (type == null) {
            if (clazz == null) {
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                JsonConvert<T> convert = new JsonConvert<>(clazz);
                return convert.convertResponse(response);
            }
        }

        JsonConvert<T> convert = new JsonConvert<>(type);
        return convert.convertResponse(response);
    }

    @Override
    public void onSuccess(com.lzy.okgo.model.Response<T> response) {
        T t = response.body();
        if (t.getClass() == BaseBean.class) {
            BaseBean bean = (BaseBean) t;
            BaseBean.MessageBean.HeadBean headBean = bean.getMessage().getHead();
            String resCode = headBean.getResCode();
            String digest = headBean.getDigest();
            String headMsg = headBean.getMessage();
            String transactionType = headBean.getTransactionType();
            Object obj = bean.getMessage().getBody();

            int rescInt = getResc(resCode);
            if (isSuccess(resCode, rescInt)) {// head 成功
                BaseMessage pojo = null;
                try {
                    pojo = (BaseMessage) obj;
                    String bodyResCode = pojo.getResCode();
                    String bodyMsg = pojo.getMessage();
                    int bodyRescInt = getResc(bodyResCode);

                    if (isSuccess(bodyResCode, bodyRescInt)) {// body 成功
                        if (null != callbackHandler)
                            callbackHandler.doRecode0000(transactionType, obj);
                    } else {
                        if (null != callbackHandler)
                            callbackHandler.doRecodeNot0000(transactionType, bodyMsg, "", bodyRescInt);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new UnsupportedOperationException("========== bean 需要继承 BaseMessage =================");
                }
            } else {
                if (null != callbackHandler)
                    callbackHandler.doRecodeNot0000(transactionType, headMsg, digest, rescInt);
            }

        } else {
            onError(response);// 不是BaseBean 当做错误处理
        }
    }

    private int getResc(String resCode) {
        int resc = -1;
        try {
            resc = Integer.parseInt(resCode);
        } catch (Exception ex) {

        }finally {
            return resc;
        }


    }

    private boolean isSuccess(String resCode, int resc) {
        if (resc == 0 || resCode.equals("0") || resCode.equals("000000") || resCode.equals("0000"))
            return true;
        else
            return false;
    }

    @Override
    public void onError(com.lzy.okgo.model.Response<T> response) {
        super.onError(response);
        if (null != callbackHandler)
            callbackHandler.doRecodeError(response.message());
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if (null != callbackHandler)
            callbackHandler.doRecodeFinish();
    }


    @NonNull
    private Response decryptResponse(Response response, boolean isDes) throws IOException {

        ResponseBody responseBody = response.body();
        byte[] bytesOriginal = IOUtils.toByteArray(responseBody.byteStream());//读取后需要从新装入，否则ResponseBody为null
        MediaType contentType = responseBody.contentType();
        String original = new String(bytesOriginal, getCharset(contentType));

        if (isDes) {//解密
            String decrypt = DesUtil.decryptDES(original, DesUtil.password);
            if (!TextUtils.isEmpty(decrypt)) {
                original = decrypt;
            }
        }

        byte[] bytes = IOUtils.toByteArray(original);
        responseBody = ResponseBody.create(responseBody.contentType(), bytes);
        response = response.newBuilder().body(responseBody).build();

        if (isDes) {
            Logger.dTag("解密recode 后再封装到 response=", new String(bytes));
        } else {
            Logger.dTag("原始recode=", original);
        }
        return response;
    }


    private static Charset getCharset(MediaType contentType) {
        Charset charset = contentType != null ? contentType.charset(UTF8) : UTF8;
        if (charset == null) charset = UTF8;
        return charset;
    }
}
