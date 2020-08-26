package com.zhcw.lib.mvp;

import android.app.Activity;
import android.content.Intent;

import com.xuexiang.xutil.XUtil;

import androidx.annotation.NonNull;
import me.jessyan.autosize.utils.Preconditions;


/**
 * ================================================
 * 框架要求框架中的每个 View 都需要实现此类, 以满足规范
 *
 * ================================================
 */
public interface IView<T>{

    /**
     * 可以多个IView 共用一个 presenter
     * @param presenter
     */
    void setPresenter(T presenter);

    /**
     * 显示加载
     */
    default void showLoading(boolean cancel) {

    }

    /**
     * 隐藏加载
     */
    default void hideLoading() {

    }

    /**
     * 显示信息
     *
     * @param message 消息内容, 不能为 {@code null}
     */
    default void showMessage(@NonNull String message){

    }

    /**
     * dialog
     * @param message
     * @param confirm
     * @param cancel
     */
    default void showDialog(@NonNull String message, String confirm, String cancel){

    }

    /**
     * 跳转 {@link Activity}
     *
     * @param intent {@code intent} 不能为 {@code null}
     */
    default void launchActivity(@NonNull Intent intent) {
        Preconditions.checkNotNull(intent);
        XUtil.getContext().startActivity(intent);
    }

    /**
     * 杀死自己
     */
    default void killMyselfView() {

    }
}
