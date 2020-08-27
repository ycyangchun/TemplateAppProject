package com.zhcw.lib.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.xuexiang.xutil.common.logger.Logger;
import com.zhcw.app.App;
import com.zhcw.app.BuildConfig;
import com.zhcw.app.base.Constants;
import com.zhcw.lib.http.MyCookieStoreListener;
import com.zhcw.lib.http.MyMemoryCookieStore;
import com.zhcw.lib.utils.FileUtilSupply;
import com.zhcw.lib.utils.sdkinit.ANRWatchDogInit;
import com.zhcw.lib.utils.sdkinit.CrashHandler;
import com.zhcw.lib.utils.sdkinit.UMengInit;
import com.zhcw.lib.utils.sdkinit.XBasicLibInit;
import com.zhcw.lib.utils.sdkinit.XUpdateInit;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import androidx.multidex.MultiDex;
import okhttp3.OkHttpClient;

/**
 * @author xuexiang
 * @since 2018/11/7 下午1:12
 */
public class BaseApplication extends Application {

    private static Context GLOBAL_APP_CONTEXT;
    private int activityNumber = 0;//当前Activity个数
    private boolean isForeground = false;//应用是否处于前端

    public static MyMemoryCookieStore memoryCookieStore;// cookie
    public MyCookieStoreListener myCookieStoreListener;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //解决4.x运行崩溃的问题
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        GLOBAL_APP_CONTEXT = this;
        initLibs();
        //前后台
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }


    /**
     * 初始化基础库
     */
    private void initLibs() {

        XBasicLibInit.init(this);

        XUpdateInit.init(this);

        //运营统计数据运行时不初始化
        if (!App.isDebug()) {
            UMengInit.init(this);
        }

        initOkGo();
        //ANR监控
        ANRWatchDogInit.init();
    }



    /**
     *
     * @return
     */
    public static Context getAppContext() {
        return GLOBAL_APP_CONTEXT;
    }

    /**
     * Activity 生命周期监听，用于监控app前后台状态切换
     */
    ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (activityNumber == 0) {
                Logger.i("app回到前台");
                isForeground = true;
            }
            activityNumber++;
            Logger.i("activityNumber = " + activityNumber);
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            activityNumber--;
            Logger.i("activityNumber = " + activityNumber);
            if (activityNumber == 0) {
                // app回到后台
                Logger.i("app回到后台");
                isForeground = false;
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    };


    public static MyMemoryCookieStore getMemoryCookieStore() {
        return memoryCookieStore;
    }

    ////////////// okgo /////////////////
    private void initOkGo() {
        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
        headers.put("Content-Type", "text/html; charset=UTF-8");//header不支持中文，不允许有特殊字符
//      headers.put("Accept-Encoding", "*");
        headers.put("Connection", "keep-alive");
        headers.put("Accept", "*/*");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "X-Requested-With");
        headers.put("Vary", "Accept-Encoding");
//      headers.put("Cookie", "add cookies here");

        HttpParams params = new HttpParams();
//        params.put("commonParamsKey1", "这里支持中文参数");     //param支持中文,直接传,不要自己编码
        //----------------------------------------------------------------------------------------//

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        if (Constants.isLog)
            loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.HEADERS);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        //第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
        //builder.addInterceptor(new ChuckInterceptor(this));

        //超时时间设置，默认60秒
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间

        //自动管理cookie（或者叫session的保持），以下几种任选其一就行
        //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));              //使用数据库保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            //使用内存保持cookie，app退出后，cookie消失
        memoryCookieStore = new MyMemoryCookieStore();
        memoryCookieStore.setMyCookieStoreListener(myCookieStoreListener);
        builder.cookieJar(new CookieJarImpl(memoryCookieStore));

        //https相关设置，以下几种方案根据需要自己设置
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        //方法二：自定义信任规则，校验服务端证书
        //HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        //方法三：使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
        builder.hostnameVerifier(new SafeHostnameVerifier());

        // 其他统一的配置
        // 详细说明看GitHub文档：https://github.com/jeasonlzy/
        OkGo.getInstance().init(this)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(0)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers)                      //全局公共头
                .addCommonParams(params);                       //全局公共参数
    }

    /**
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     */
    private class SafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            //验证主机名是否匹配
            //return hostname.equals("server.jeasonlzy.com");
            return true;
        }
    }
}
