package com.zhcw.app.activity

import android.os.Build
import android.text.TextUtils
import android.view.View
import com.xuexiang.templateproject.activity.MainActivity
import com.xuexiang.xaop.XAOP
import com.xuexiang.xaop.annotation.Permission
import com.xuexiang.xaop.annotation.SingleClick
import com.xuexiang.xaop.consts.PermissionConsts
import com.xuexiang.xui.widget.activity.BaseSplashActivity
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import com.xuexiang.xutil.app.ActivityUtils
import com.xuexiang.xutil.common.logger.Logger
import com.xuexiang.xutil.system.AppExecutors
import com.zhcw.app.App
import com.zhcw.app.R
import com.zhcw.app.base.Constants
import com.zhcw.app.utils.ToastListUtil
import com.zhcw.app.utils.TokenUtils
import com.zhcw.lib.utils.*
import com.zhcw.lib.utils.sdkinit.CrashHandler
import me.jessyan.autosize.internal.CancelAdapt

/**
 * 启动页【无需适配屏幕大小】
 *
 */
class SplashActivity : BaseSplashActivity(), CancelAdapt {
    override fun getSplashDurationMillis(): Long {
        return 500
    }

    /**
     * activity启动后的初始化
     */
    override fun onCreateActivity() {
        initSplashView(R.drawable.xui_config_bg_splash)
        startSplash(false)
    }

    /**
     * 启动页结束后的动作
     */
    override fun onSplashFinished() {
        val isAgree = MMKVUtils.getBoolean("key_agree_privacy", false)
        if (isAgree) {
            toMain()
        } else {
            showPrivacy()
        }
    }

    //权限申请
    private fun showPrivacy() {
        SplashUtils.showPrivacyDialog(this) { dialog: MaterialDialog, which: DialogAction? ->
            dialog.dismiss()
            handleRequestPermission(mWelcomeLayout)
        }
        //设置动态申请权限切片 申请权限被拒绝的事件响应监听
        XAOP.setOnPermissionDeniedListener { permissionsDenied: List<String?>? ->
//                    XToastUtils.error("权限申请被拒绝:" + StringUtils.listToString(permissionsDenied, ","));
            showPrivacy()
        }
    }

    @SingleClick
    @Permission(PermissionConsts.STORAGE, PermissionConsts.LOCATION)
    private fun handleRequestPermission(v: View) { // 必须有参数
//        XToastUtils.toast("权限申请通过！");
        MMKVUtils.put("key_agree_privacy", true)
        toMain()
    }

    // 主页
    private fun toMain() {
        initFileStorage()
        if (TokenUtils.getInstance().keyToken) {
            ActivityUtils.startActivity(MainActivity::class.java)
        } else {
            ActivityUtils.startActivity(LoginActivity::class.java)
        }
        finish()
    }

    //初始存储目录
    private fun initFileStorage() {
        FileUtilSupply.initCache()
        CrashHandler.getInstance().init(App.getAppContext())
        AppExecutors.get().diskIO().execute { initCacheTxt() }
        initDeviceData()
    }

    //设备信息
    fun initDeviceData() {
        Constants.uaStr = Build.MODEL // 手机型号
        Constants.deviceId = DeviceID.getInstance().deviceID
        Constants.imeiStr = DeviceID.getInstance().imei
        if (TextUtils.isEmpty(Constants.imeiStr)) Constants.imeiStr = DeviceID.getInstance().deviceId
    }

    //初始cache
    private fun initCacheTxt() {
        val zhcwUtils = ZhcwUtils.getInstance()
        zhcwUtils.writeCacheFile("txt/1.txt", "111111112222")
        zhcwUtils.writeAssetsCacheFile("ds/k3/t1.txt")
        zhcwUtils.writeAssetsCacheFile("ds/t2.txt")
        //      Logger.d(zhcwUtils.readCacheFile("ds/t3.txt"));
        Logger.d(ToastListUtil.getIT().getMV("DC101062", "默认key 11111111111111111"))
        //      ToastList.getInstance().removeToast();
        Logger.d(ToastListUtil.getIT().getMV("DC101059", "默认key 22"))
    }

    override fun onDestroy() {
        super.onDestroy()
        if (null != XAOP.getOnPermissionDeniedListener()) {
//            XAOP.setOnPermissionDeniedListener(null)
        }
    }
}