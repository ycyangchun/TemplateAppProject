package com.zhcw.app;

import com.xuexiang.xutil.app.AppUtils;
import com.xuexiang.xutil.common.logger.Logger;
import com.xuexiang.xutil.data.DateUtils;
import com.zhcw.app.base.Constants;
import com.zhcw.app.base.IConstants;
import com.zhcw.lib.base.BaseApplication;
import com.zhcw.lib.utils.FileUtilSupply;
import com.zhcw.lib.utils.manager.UserMgr;

import java.util.Date;

/**
 *  app Application
 */
public class App extends BaseApplication{

    public static UserMgr userMgr;// 用户信息
    //TODO 设置服务器
    private final int server = 1;//0 开发服；1测试服；2正式服

    @Override
    public void onCreate() {
        buildApp();
        super.onCreate();
    }

    public void buildApp(){

        Constants.packageName = "zhcwLib";//(设置应用存储包名)  ： zhcw 中彩网
        Constants.versionName = AppUtils.getAppName();
        Constants.DEBUG_VERSION = DateUtils.date2String(new Date()
                ,DateUtils.yyyyMMdd.get())+"_01";
        //userMgr
        userMgr = UserMgr.getInstance();
        FileUtilSupply.initFileUtils();

        switch (server){
            case IConstants.SERVER_CESHI_KAIFA:
                Constants.setServerType(IConstants.SERVER_CESHI_KAIFA);
                break;
            case IConstants.SERVER_CESHI_CESHI:
                Constants.setServerType(IConstants.SERVER_CESHI_CESHI);
                break;
            case IConstants.SERVER_ZHENGSHI:
                Constants.setServerType(IConstants.SERVER_ZHENGSHI);
                break;
        }

    }

    /**
     * @return 当前app是否是调试开发模式
     */
    public static boolean isDebug() {
        return Constants.isLog;
    }

}
