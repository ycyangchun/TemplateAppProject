package com.zhcw.app.base;

public interface IConstants {


    int SERVER_CESHI_KAIFA = 0;//开发服
    int SERVER_CESHI_CESHI = 1;//测试服
    int SERVER_ZHENGSHI = 2;//正式服

    String src_platform = "030";// /src中的平台 这个里是不能修改
    String src_project = "100";// src中用来区分平台的 这个需要根据各个平台来修改
    String SYS_TYPE_M = "M";// 马上奖系统
    String SYS_TYPE_T = "T";// 代表投注大赛系统
    String SYS_TYPE_Z = "Z";// 代表资讯系统
    String SYS_TYPE_U = "U";// 用户中心系统
    String SYS_TYPE_F = "F";// 全民疯抢系统
    String SYS_TYPE_A = "A";// 账户中心
    String SYS_TYPE_U1 = "U1";// 用户中心系统 （新格式）
    String SYS_TYPE_DA = "DA";// 数据分析

    //////// web //////////
    int WEB_NO_PARAM = -1;// -1 不添加param;
    int WEB_ZHCW_PARAM = 0;// 0 中彩网;
    int WEB_XMS_PARAM = 1;// 1 小秘书
    int WEB_OTHER_PARAM = 2;// 2 other

    ////////网络请求错误显示处理////////
    int ERR_SHOW_TOAST = 0;// 显示 toast
    int ERR_SHOW_DIALOG = 1;// 显示 dialog
    int ERR_NO_SHOW = 2;// 父类不显示（可以子类单独处理）

    /**
     * 加载界面的三种状态
     */
    String STATE_LOADING = "loading";
    String STATE_SUCCESSED = "success";
    String STATE_FAILED = "failed";

    //数据库的名字
    String DB_NAME = "lottery_manager_system.sqlite";
}
