package com.zhcw.lib.widget.dialog;


import android.content.Context;

import com.zhcw.app.R;
import com.zhcw.lib.utils.manager.DialogManager;

import androidx.annotation.NonNull;

public class NormalDialog extends BaseNormalDialog<NormalDialog> {

    private static int STYLE_DEFAULT = STYLE_MOBAN;

    public static int LAYOUT_XQ = R.layout.layout_normaldialog_xq;
    public static int LAYOUT_METERAIL = R.layout.layout_normaldialog_meterail;
    protected static int LAYOUT_DEFAULT = LAYOUT_XQ;

    public NormalDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public static void setDefaultStyle(int style) {
        STYLE_DEFAULT = style;
    }

    public static void setDefaultLayout(int layoutId) {
        LAYOUT_DEFAULT = layoutId;
    }

    private void init() {
        setStyle(STYLE_DEFAULT);
        setCustomView(LAYOUT_DEFAULT);
    }

    public NormalDialog setXQLayoutStyle() {
        setStyle(STYLE_ALERT);
        setCustomView(LAYOUT_XQ);
        setWidthMatch();
        setHeightWrap();
        return this;
    }

    public NormalDialog setMeterailLayoutStyle() {
        setStyle(STYLE_ALERT);
        setCustomView(LAYOUT_METERAIL);
        setWidthMatch();
        setHeightWrap();
        return this;
    }

    public NormalDialog setListener(DialogManager.DialogListener listener) {
        if(null != listener) {
            setNegativeListener(new BaseDialog.OnDialogClickListener() {
                @Override
                public void onClick(BaseDialog dialog) {
                    listener.dialogListener(0,"");
                }
            });
            setPositiveListener(new BaseDialog.OnDialogClickListener() {
                @Override
                public void onClick(BaseDialog dialog) {
                    listener.dialogListener(1,"");
                }
            });
        }
        return this;
    }


}
