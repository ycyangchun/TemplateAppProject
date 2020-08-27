package com.zhcw.lib.http;

import com.xuexiang.xutil.common.logger.Logger;
import com.zhcw.app.base.Constants;
import com.zhcw.app.base.IConstants;
import com.zhcw.lib.mvp.IView;
import com.zhcw.lib.utils.manager.UserMgr;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

/**
 *  base 统一处理的handler
 */
public class ZhcwCallback implements CallbackHandler,LifecycleObserver {

    private String TAG = this.getClass().getSimpleName();
    private IView iView;
    private ZhcwCallback model;
    private int errShow = IConstants.ERR_NO_SHOW;//网络请求错误显示处理
    private boolean loadingShow = false;//加载是否有loading
    private boolean loadingCancel = true;//loading 是否可取消
    private Long L;
    private String tag ,errConfirm,errCancel;

    public ZhcwCallback(IView iView) {
        this.iView = iView;
        model = this;
        //将 LifecycleObserver 注册给 LifecycleOwner 后 @OnLifecycleEvent 才可以正常使用
        if (iView != null && iView instanceof LifecycleOwner) {
            ((LifecycleOwner) iView).getLifecycle().addObserver(this);
            if (model!= null && model instanceof LifecycleObserver){
                ((LifecycleOwner) iView).getLifecycle().addObserver((LifecycleObserver) model);
            }
        }
    }

    public ZhcwCallback() {
        model = this;
    }

    /**
     * 加载是否有loading
     * @param loadingShow
     */
    public void setLoadingShow(boolean loadingShow) {
        this.loadingShow = loadingShow;
    }

    /**
     * 加载是否有loading
     * loading 是否可取消
     * @param loadingShow
     * @param loadingCancel
     */
    public void setLoadingShow(boolean loadingShow, boolean loadingCancel) {
        this.loadingShow = loadingShow;
        this.loadingCancel = loadingCancel;
    }

    /**
     * 错误处理 toast
     * @param errShow
     */
    public void setErrShow(int errShow) {
        this.errShow = errShow;
    }

    /**
     * 错误处理 dialog
     * @param errShow
     */
    public void setErrShow(int errShow,String confirm,String cancel) {
        this.errShow = errShow;
        this.errConfirm = confirm;
        this.errCancel = cancel;
    }

    @Override
    public void doRecodeStart(String tag) {
        this.tag = tag;
        if(iView != null && loadingShow)
            iView.showLoading(loadingCancel);

        if(Constants.isLog)
            L = System.currentTimeMillis();
    }



    @Override
    public void doRecode0000(String transactionType, Object obj) {
        Logger.eTag(TAG,"doRecode0000 ");
    }

    @Override
    public void doRecodeNot0000(String transactionType, String msg, String dis, int resc) {
        switch (resc) {
            case 3002:
            case 0002:
            case 105003:// 登录过时了
                doRecode3002(transactionType);
                break;
            case 3001:// 金额不足

                break;
            case 3000:// 购彩资料不完善

                break;
            case 1015:
            case 100107:// 号码已经使用

                break;
            case 4444:// 是否扣奖金

                break;
            case 100108:// 注册信息有误，请重试。（返回上级页面）

                break;
            case 100109://获取获取验证码短信次数过多

                break;
            case 100203:// 尝试登录次数过多，账户已被锁定1小时！

                break;
            case 7777:// 登录密码与支付密码相同，建议使用不同密码！

                break;
            case 8888:// 支付密码与登录密码相同，建议使用不同密码！

                break;
            default:
                errShow(msg);
                Logger.eTag(TAG,"doRecodeNot0000 " + transactionType + " "+ msg +" "+ resc);
                break;
        }


    }




    @Override
    public void doRecodeError(String msg) {
        errShow(msg);
    }

    @Override
    public void doRecodeFinish() {
        if(iView != null && loadingShow) {
            iView.hideLoading();
        }

        Logger.eTag(TAG,"T========== busiCode ==========> "+ tag + " 总耗时："+(System.currentTimeMillis() - L) + "ms");
    }

    /**
     * 登录过时了
     * @param transactionType
     */
    public void doRecode3002(String transactionType) {
        UserMgr.getInstance().setLogin(false);
    }

    /**
     * 网络请求错误显示处理
     * @param msg
     */
    private void errShow(String msg) {
        if(iView != null){
            if(errShow == IConstants.ERR_SHOW_TOAST){
                iView.showMessage(msg);
            }else if(errShow == IConstants.ERR_SHOW_DIALOG){
                iView.showDialog(msg,errConfirm,errCancel);
            }
        }
    }

    public void onDestroy() {
        if(iView != null){
            iView.killMyselfView();
            iView = null;
        }
        this.onDestroy();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
    }
    ///////////////////////////////////////////////////
    //=============几种情况====================

    /**
     * 只加载，出现错误不提示
     *  @param cancel
     */
    public void loadingNoErr(Boolean cancel){
        errShow = IConstants.ERR_NO_SHOW;//网络请求错误显示处理方式
        loadingShow = true;//loading 是否显示
        loadingCancel = cancel;//loading 是否可取消
    }

    /**
     * 加载，出现错误toast提示
     *  @param cancel
     */
    public void loadingErrToast(Boolean cancel){
        errShow = IConstants.ERR_SHOW_TOAST;//网络请求错误显示处理方式
        loadingShow = true;//loading 是否显示
        loadingCancel = cancel;//loading 是否可取消
    }

    /**
     * 加载，出现错误dialog提示
     *  @param cancel
     */
    public void loadingErrDialog(Boolean cancel){
        errShow = IConstants.ERR_SHOW_DIALOG;//网络请求错误显示处理方式
        loadingShow = true;//loading 是否显示
        loadingCancel = cancel;//loading 是否可取消
    }
}
