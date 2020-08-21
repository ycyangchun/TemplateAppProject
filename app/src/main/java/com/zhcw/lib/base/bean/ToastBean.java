package com.zhcw.lib.base.bean;

import java.util.HashMap;
import java.util.List;

public class ToastBean extends BaseMessage {

    /**
     * busiCode : 10140204
     * resCode : 000000
     * message : 获取提示语成功
     * tipsList : [{"tipsVal":"1;13;","tipsKey":"BC410001"},{"tipsVal":"您已被禁言，如有异议请联系客服，400-844-9518","tipsKey":"DC101059"}]
     * timeId : -1
     */
    //统一处理 BaseMessage 中了
    private String busiCode;
    private String timeId;
    private List<TipsListBean> tipsList;

    // 自行添加的转换  list to map
    public HashMap<String,String> listToMap(){
        HashMap<String ,String> tipsMap = new HashMap<>();
        if(null != tipsList && tipsList.size() > 0){
            for (TipsListBean t : tipsList) {
                tipsMap.put(t.tipsKey, t.tipsVal);
            }
        }
        return tipsMap;
    }
    public String getBusiCode() {
        return busiCode;
    }


    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }


    public String getTimeId() {
        return timeId;
    }

    public void setTimeId(String timeId) {
        this.timeId = timeId;
    }

    public List<TipsListBean> getTipsList() {
        return tipsList;
    }

    public void setTipsList(List<TipsListBean> tipsList) {
        this.tipsList = tipsList;
    }

    public static class TipsListBean {
        /**
         * tipsVal : 1;13;
         * tipsKey : BC410001
         */

        private String tipsVal;
        private String tipsKey;

        public String getTipsVal() {
            return tipsVal;
        }

        public void setTipsVal(String tipsVal) {
            this.tipsVal = tipsVal;
        }

        public String getTipsKey() {
            return tipsKey;
        }

        public void setTipsKey(String tipsKey) {
            this.tipsKey = tipsKey;
        }
    }



}
