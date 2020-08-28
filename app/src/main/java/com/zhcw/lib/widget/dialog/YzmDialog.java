package com.zhcw.lib.widget.dialog;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xuexiang.xui.utils.CountDownButtonHelper;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.zhcw.app.R;
import com.zhcw.app.base.UiContract;
import com.zhcw.app.fragment.LoginPresenter;
import com.zhcw.lib.mvp.IView;
import com.zhcw.lib.utils.XToastUtils;
import com.zhcw.lib.utils.manager.DialogManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class YzmDialog extends BaseNormalDialog<YzmDialog> implements UiContract.LoginView {

    private static int STYLE_DEFAULT = STYLE_MOBAN;

    protected static int LAYOUT_DEFAULT = R.layout.layout_dialog_login_yzm;

    protected TextView yzm_bnt;
    protected EditText yzm_et;

    private CountDownButtonHelper mCountDownHelper;
    private String phoneNumber, psw;
    private UiContract.LoginPresenter loginPresenter;

    public YzmDialog(@NonNull Context context) {
        super(context);
        init();
    }

    private void init() {
        setStyle(STYLE_DEFAULT);
        setCustomView(LAYOUT_DEFAULT);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        new LoginPresenter(this);
    }

    private void initView() {
        yzm_bnt = getView(getContext().getResources().getIdentifier("yzm_bnt", "id", getContext().getPackageName()));
        yzm_et = getView(getContext().getResources().getIdentifier("yzm_et", "id", getContext().getPackageName()));
        mCountDownHelper = new CountDownButtonHelper(yzm_bnt, 10);
        yzm_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getVerifyCode();
            }
        });
    }


    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        // TODO: 2019-11-18 这里只是界面演示而已
        XToastUtils.warning("只是演示，验证码请随便输");
        mCountDownHelper.start();
    }

    public YzmDialog setListener(DialogManager.DialogListener listener) {
        if (null != listener) {
            setNegativeListener(new BaseDialog.OnDialogClickListener() {
                @Override
                public void onClick(BaseDialog dialog) {
                    listener.dialogListener(0,"");
                }
            });
            setPositiveListener(new BaseDialog.OnDialogClickListener(false) {
                @Override
                public void onClick(BaseDialog dialog) {
                    if(null != yzm_et && yzm_et.getText().toString().trim().length() > 0) {
                        String yzmStr = yzm_et.getText().toString().trim();
                        listener.dialogListener(1, yzmStr);
                        KeyboardUtils.hideSoftInput(yzm_et);
                        toLogin(phoneNumber,psw,yzmStr);
//                        dismiss();
                    }else {
                        XToastUtils.error("请输入验证码");
                    }
                }
            });
        }
        return this;
    }

    public YzmDialog setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public YzmDialog setPsw(String psw) {
        this.psw = psw;
        return this;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mCountDownHelper != null) {
            mCountDownHelper.recycle();
        }
    }

    ///////////////////


    @Override
    public void toLogin(String cell, String psw, String identCode) {
        loginPresenter.toLogin(cell, psw,identCode);
    }

    @Override
    public void successLogin() {
        dismiss();
    }

    @Override
    public void forgetPassword() {

    }

    @Override
    public void showPassword() {

    }

    @Override
    public void setPresenter(UiContract.LoginPresenter presenter) {
        loginPresenter = presenter;
    }
}
