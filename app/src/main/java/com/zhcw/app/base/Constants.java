package com.zhcw.app.base;

import java.util.HashMap;

public class Constants {

    public static boolean isDes; //是否加密
    public static boolean isLog;//打印日志
    public static int SERVER_TYPE; // 哪个服务器


    public static String DEBUG_VERSION = "";//"20190618_01";//服务器版本，打包时间，等
    public static String versionName = "";//1000
    public static String packageName = "";//"zhcwLib";//zhcw 中彩网
    public static String CHANNELID = "";// 渠道
    public static String imeiStr = "";// 手机串号
    public static String uaStr = "";//手机型号
    public static String DeviceID = "";//设备id MD5



    public static String HOST_URL_Z = "";// 正式
    public static String HOST_URL = "";// 用户中心正式
    public static String CHART_URL = "";//图表走势的URL,这是正式URL
    public static String XMS_DEFAULT_URL = "";//小秘书默认url
    public static String WEB_URL_Z = "";// web正式

    public static void setServerType(int serverType) {
        SERVER_TYPE = serverType;
        switch (serverType) {
            case IConstants.SERVER_CESHI_KAIFA:// 开发服
                isLog = true;
                isDes = false;

                DEBUG_VERSION = "kf" + versionName + "_" + DEBUG_VERSION;
                HOST_URL_Z = "http://192.168.36.222:8080/zhcwapp/serv.do";// 测试
                HOST_URL = "http://192.168.35.101:38080/client.do";// 用户中心测试
                CHART_URL = "http://chart.51caiyou.com";//图表走势的URL
                XMS_DEFAULT_URL = "http://192.168.70.128/";//小秘书默认url
                WEB_URL_Z = "http://218.249.41.157:8080/h5/";// 测试
                break;
            case IConstants.SERVER_CESHI_CESHI:// 测试服
                isLog = true;
                isDes = false;

                DEBUG_VERSION = "cs" + versionName + "_" + DEBUG_VERSION;
                HOST_URL_Z = "http://192.168.36.222:8080/zhcwapp/serv.do";// 测试
                HOST_URL = "http://192.168.35.113:7771/client.do";// 用户中心测试
                CHART_URL = "http://chart.51caiyou.com";//图表走势的URL
                XMS_DEFAULT_URL = "http://xms.51caiyou.com";//小秘书默认url
                WEB_URL_Z = "http://218.249.41.157:8080/h5/";// 测试

                break;
            case IConstants.SERVER_ZHENGSHI:// 正式服
                isLog = false;
                isDes = true;

                DEBUG_VERSION = "";
                HOST_URL_Z = "http://c.zhcw.com/zhcwapp/serv.do";// 正式
                HOST_URL = "http://u.zhcw.com/client.do";// 用户中心正式
                CHART_URL = "http://chart.zhcw.com";//图表走势的URL
                XMS_DEFAULT_URL = "https://xms.zhcw.com";//小秘书默认url

                break;
        }
    }


    public static HashMap<String,String> toastBean;// toast


    /**
     *  获取 key --> value
     * @param mapK
     * @param defaultV
     * @return
     */
    public static String getMapValue(String mapK ,String defaultV){
        if(null == toastBean){
            toastBean = ToastList.getInstance().getToastMap();
        }
        return getMapValue(toastBean,mapK,defaultV);
    }

    /**
     *  获取 key --> value
     * @param map
     * @param mapK
     * @param defaultV
     * @return
     */
    public static String getMapValue(HashMap<String,String> map,String mapK ,String defaultV){
        String v = null;
        if(null != map){
            v = map.get(mapK);
        }
        if(null == v){
            v = defaultV;
        }
        return v;
    }
}
