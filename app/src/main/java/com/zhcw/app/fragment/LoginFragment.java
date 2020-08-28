/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.zhcw.app.fragment;

import android.graphics.Color;
import android.view.View;

import com.xuexiang.templateproject.activity.MainActivity;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.utils.CountDownButtonHelper;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xutil.app.ActivityUtils;
import com.zhcw.app.R;
import com.zhcw.app.base.UiContract;
import com.zhcw.lib.base.BaseFragment;
import com.zhcw.lib.utils.XToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

@Page(name = "loginFragment",anim = CoreAnim.none)
public class LoginFragment extends BaseFragment implements UiContract.LoginView {

    @BindView(R.id.et_phone_number)
    MaterialEditText etPhoneNumber;
    @BindView(R.id.et_verify_code)
    MaterialEditText etVerifyCode;
    @BindView(R.id.btn_get_verify_code)
    RoundButton btnGetVerifyCode;

    private CountDownButtonHelper mCountDownHelper;
    private UiContract.LoginPresenter loginPresenter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initPage() {
        super.initPage();
        new LoginPresenter(getAttachContext(),this);
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle()
                .setImmersive(true);
        titleBar.setBackgroundColor(Color.TRANSPARENT);
        titleBar.setTitle("");
        titleBar.setLeftImageDrawable(ResUtils.getVectorDrawable(getContext(), R.drawable.ic_login_close));
        return titleBar;
    }

    @Override
    protected void initViews() {
        mCountDownHelper = new CountDownButtonHelper(btnGetVerifyCode, 60);
    }



    @SingleClick
    @OnClick({R.id.btn_get_verify_code, R.id.btn_login, R.id.tv_other_login, R.id.tv_forget_password, R.id.tv_user_protocol, R.id.tv_privacy_protocol})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_verify_code:
                if (etPhoneNumber.validate()) {
                    getVerifyCode(etPhoneNumber.getEditValue());
                }
                break;
            case R.id.btn_login:
                if (etPhoneNumber.validate()) {
                    if (etVerifyCode.validate()) {
                        loginByVerifyCode(etPhoneNumber.getEditValue(), etVerifyCode.getEditValue());
                    }
                }
                break;
            case R.id.tv_other_login:
                XToastUtils.info("其他登录方式");
                break;
            case R.id.tv_forget_password:
                XToastUtils.info("忘记密码");
                break;
            case R.id.tv_user_protocol:
                XToastUtils.info("用户协议");
                break;
            case R.id.tv_privacy_protocol:
                XToastUtils.info("隐私政策");
                break;
            default:
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode(String phoneNumber) {
        // TODO: 2019-11-18 这里只是界面演示而已
        XToastUtils.warning("只是演示，验证码请随便输");
        mCountDownHelper.start();
    }

    /**
     * 根据验证码登录
     *
     * @param phoneNumber 手机号
     * @param verifyCode  验证码
     */
    private void loginByVerifyCode(String phoneNumber, String verifyCode) {
        // TODO: 2019-11-18 这里只是界面演示而已
//        String token = RandomUtils.getRandomNumbersAndLetters(16);
//        if (TokenUtils.handleLoginSuccess(token)) {
//            popToBack();
//            ActivityUtils.startActivity(MainActivity.class);
//        }
        toLogin(phoneNumber,verifyCode,null);
    }


    @Override
    public void onDestroyView() {
        if (mCountDownHelper != null) {
            mCountDownHelper.recycle();
        }
        super.onDestroyView();
    }

    /////////////


    @Override
    public void toLogin(String cell, String psw,String identCode) {
        loginPresenter.toLogin(cell, psw,identCode);
    }

    @Override
    public void successLogin() {
        popToBack();
        ActivityUtils.startActivity(MainActivity.class);
    }

    @Override
    public void forgetPassword() {

    }

    @Override
    public void showPassword() {

    }

    @Override
    public void setPresenter(UiContract.LoginPresenter presenter) {
        loginPresenter =  presenter;
    }

}
