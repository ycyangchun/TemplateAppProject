package com.zhcw.app;

import android.text.TextUtils;

import com.xuexiang.xormlite.ExternalDataBaseRepository;
import com.xuexiang.xormlite.InternalDataBaseRepository;
import com.xuexiang.xormlite.annotation.DataBase;
import com.xuexiang.xormlite.enums.DataBaseType;
import com.xuexiang.xormlite.logs.DBLog;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.app.AppUtils;
import com.xuexiang.xutil.common.logger.Logger;
import com.xuexiang.xutil.data.DateUtils;
import com.zhcw.app.base.Constants;
import com.zhcw.app.base.IConstants;
import com.zhcw.lib.base.BaseApplication;
import com.zhcw.lib.db.ExternalDataBase;
import com.zhcw.lib.db.InternalDataBase;
import com.zhcw.lib.http.MyCookieStoreListener;
import com.zhcw.lib.utils.FileUtilSupply;
import com.zhcw.lib.utils.manager.UserMgr;

import java.util.Date;

/**
 *  app Application
 */
@DataBase(name = "internal", type = DataBaseType.INTERNAL)
public class App extends BaseApplication implements MyCookieStoreListener {

    public static UserMgr userMgr;// 用户信息
    //TODO 设置服务器
    private final int server = 1;//0 开发服；1测试服；2正式服

    @Override
    public void onCreate() {
        buildApp();
        super.onCreate();
        initDb();
        FileUtilSupply.initFileUtils();
    }

    private void initDb() {
        InternalDataBaseRepository.getInstance()
                .setIDatabase(new InternalDataBase())  //设置内部存储的数据库实现接口
                .init(this);

        ExternalDataBaseRepository.getInstance()
                .setIDatabase(new ExternalDataBase(  //设置外部存储的数据库实现接口
                        ExternalDataBaseRepository.DATABASE_PATH,
                        ExternalDataBaseRepository.DATABASE_NAME,
                        ExternalDataBaseRepository.DATABASE_VERSION))
                .init(this);

        DBLog.debug(isDebug());
    }

    public void buildApp(){

        Constants.packageName = "zhcwLib";//(设置应用存储包名)  ： zhcw 中彩网
        Constants.versionName = String.valueOf(AppUtils.getAppVersionCode());
        Constants.DEBUG_VERSION = DateUtils.date2String(new Date()
                ,DateUtils.yyyyMMdd.get())+"_01";
        //userMgr
        userMgr = UserMgr.getInstance();


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

        XUtil.init(this);
    }

    /**
     * @return 当前app是否是调试开发模式
     */
    public static boolean isDebug() {
        return Constants.isLog;
    }


    /////////////////////////////
    @Override
    public void setJsessionid(String newCookie) {
        App.userMgr.setJsessionid(newCookie);
    }

    @Override
    public String getJsessionid() {
        return App.userMgr.getJsessionid();
    }

    @Override
    public boolean isLogin() {
        return App.userMgr.isLogin() && !TextUtils.isEmpty(getJsessionid());
    }
}
