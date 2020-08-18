package com.zhcw.lib.base.bean;

/**
 *  返回 正常body ，错误body 共有字段
 */
public class BaseMessage {
    public String resCode;
    public String message;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
