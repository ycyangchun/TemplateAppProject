package com.zhcw.lib.http;


public interface CallbackHandler {
    void doRecodeStart(String transactionType);
    void doRecode0000(String transactionType, Object obj);
    void doRecodeNot0000(String transactionType, String msg, String dis, int resc);
    void doRecodeError(String msg);
    void doRecodeFinish();
}
