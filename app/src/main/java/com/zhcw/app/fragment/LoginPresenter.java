package com.zhcw.app.fragment;

import android.content.Context;
import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.xuexiang.xormlite.InternalDataBaseRepository;
import com.xuexiang.xormlite.db.DBService;
import com.xuexiang.xutil.common.logger.Logger;
import com.xuexiang.xutil.system.AppExecutors;
import com.zhcw.app.App;
import com.zhcw.app.base.IConstants;
import com.zhcw.app.base.UiContract;
import com.zhcw.app.net.DoNetWork;
import com.zhcw.app.utils.TokenUtils;
import com.zhcw.lib.base.bean.User;
import com.zhcw.lib.db.entity.DbUser;
import com.zhcw.lib.http.ZhcwCallback;
import com.zhcw.lib.utils.manager.DialogManager;
import com.zhcw.lib.utils.manager.UserMgr;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginPresenter extends UiContract.LoginPresenter {

    private UiContract.LoginView loginView;
    private UiContract.VerifyView verifyView;

    private ZhcwCallback zhcwCallback;
   

    private Context mContext;
    public LoginPresenter(Context context ,UiContract.LoginView loginView) {
        super(loginView);
        this.mContext =  context;
        this.loginView = loginView;
        this.loginView.setPresenter(this);
    }

    public LoginPresenter(Context context,UiContract.LoginView loginView, UiContract.VerifyView verifyView) {
        super(loginView);
        this.mContext =  context;
        this.loginView = loginView;
        this.verifyView = verifyView;
        this.verifyView.setPresenter(this);
        this.loginView.setPresenter(this);
    }

    @Override
    public void toLogin(String cell, String psw, String identCode) {
        Map<String, String> map = new HashMap<>();
        map.put("cell", cell);
        map.put("userPwd", psw);
        if (!TextUtils.isEmpty(identCode)) {
            map.put("identCode", identCode);
        }

        zhcwCallback = new ZhcwCallback(loginView) {

            @Override
            public void doRecode0000(String transactionType, Object obj) {
                super.doRecode0000(transactionType, obj);
                User u = (User) obj;
                Logger.e(transactionType + "  userId = " + u.getUserId());
                App.userMgr.setUser(u);
                TokenUtils.getInstance().handleLoginSuccess(u.getUserId());
                loginView.successLogin();
                AppExecutors.get().poolIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        dbData(cell, psw);
                    }
                });
            }

            @Override
            public void doRecodeNot0000(String transactionType, String msg, String dis, int resc) {
                super.doRecodeNot0000(transactionType, msg, dis, resc);
                switch (resc) {
                    case 102298:// 该用户已在其他设备上登录
                        StringBuilder sb = new StringBuilder(cell);
                        sb.replace(3, 7, "****");
                        DialogManager.getIT().yzmDialog(mContext, sb.toString(), cell, psw);
                        break;
                }
            }

            @Override
            public void doRecodeError(String msg) {
                super.doRecodeError(msg);

            }
        }

        ;
        //错误处理 toast
        zhcwCallback.loadingErrToast(false);
        DoNetWork.getClient().

                login(IConstants.loginBusiCode, map, zhcwCallback);
    }

    @Override
    public void verifyCode(String type, String cell, String psw) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("mobile", cell);
        if (type.equals("BM")) {
            map.put("userId", UserMgr.getInstance().getLoginUserId());
        }

        if (type.equals("LOGIN")) {
            map.put("password", psw);
        }

        DoNetWork.getClient().post(IConstants.verifyBusiCode, map);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != zhcwCallback) {
            zhcwCallback.onDestroy();
            OkGo.getInstance().cancelTag(IConstants.loginBusiCode);
        }

    }

    //测试数据库
    private void dbData(String cell, String psw) {
        if (TextUtils.isEmpty(cell) || TextUtils.isEmpty(psw)) return;

        try {
            DbUser dbUser;
            DBService<DbUser> internal = InternalDataBaseRepository.getInstance().getDataBase(DbUser.class);
//            internal.deleteAll();
            List<DbUser> list = internal.queryAndOrderBy("mobile", cell, "id", false);
            if (null == list || list.size() < 1) {
                dbUser = new DbUser();
                dbUser.setMobile(cell);
                dbUser.setPassword(psw);
                dbUser.setType(1);
                internal.insert(dbUser);
            } else {
                dbUser = list.get(0);
                if (!cell.equals(dbUser.getMobile()) || !psw.equals(dbUser.getPassword())) {
                    dbUser.setMobile(cell);
                    dbUser.setPassword(psw);
                    internal.updateData(dbUser);
                }
            }

            Logger.d(internal.queryAll().toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
