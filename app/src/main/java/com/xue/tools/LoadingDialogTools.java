package com.xue.tools;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;

import com.xue.R;


public class LoadingDialogTools {

    /**
     * 加载loading
     *
     * @param context
     * @return
     */
    public static AppCompatDialog showLoadingDialog(Context context) {
        if (context == null) {
            return null;
        }
        final AppCompatDialog dialog = new AppCompatDialog(context, R.style.transparentDialog);
        dialog.setContentView(R.layout.dialog_loading);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        return dialog;
    }

}
