package com.zhcw.lib.utils.manager;

import android.content.Context;

import com.xuexiang.xutil.XUtil;
import com.zhcw.app.R;
import com.zhcw.app.utils.ToastListUtil;
import com.zhcw.lib.widget.dialog.BaseDialog;
import com.zhcw.lib.widget.dialog.NormalDialog;
import com.zhcw.lib.widget.dialog.YzmDialog;

public class DialogManager {

    private BaseDialog dialog;

    private DialogManager() {
    }

    private static class Instance {
        private static final DialogManager INSTANCE = new DialogManager();
    }

    public static DialogManager getIT() {
        return DialogManager.Instance.INSTANCE;
    }

    public interface DialogListener{
        void dialogListener(int type,String msg);
    }
    /**
     * 知道了
     * @param ctx
     * @param content
     */
    public void promptDialog(Context ctx, String content){
        if(null != ctx) {
            dialog = new NormalDialog(ctx)
                    .setContent(content)
                    .setNegativeText("知道了");

            dialog.show();
        }
    }

    /**
     * 选择
     * @param ctx
     * @param content
     */
    public void selectDialog(Context ctx, String content, String negative,String positive,
                             boolean cancel , DialogManager.DialogListener listener){
        if(null != ctx) {
            dialog = new NormalDialog(ctx)
                    .setContent(content)
                    .setCancelable(cancel)
                    .setNegativeText(negative)
                    .setPositiveText(positive)
                    .setListener(listener)
            ;

            dialog.show();
        }
    }

    /**
     *
     * @param ctx
     * @param content
     */
    public void yzmDialog(Context ctx, String content, String phoneNumber,DialogManager.DialogListener listener){
        if(null != ctx) {
            dialog = new YzmDialog(ctx)
                    .setTitle(ToastListUtil.getIT().getMV("BC350042",
                            XUtil.getResources().getString(R.string.login_yzm)))
                    .setContent(content)
                    .setPhoneNumber(phoneNumber)
                    .setListener(listener)
                    .setNegativeText("取消")
                    .setPositiveText("确定");

            dialog.show();
        }
    }

    public void dismiss(){
        if(null != dialog){
            dialog.dismiss();
        }
    }
}
