package com.xue.tools;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialog;

import com.xue.R;

import java.util.HashMap;
import java.util.HashSet;


public class LoadingDialog {

    private static HashMap<Integer, LoadingDialog> map = new HashMap();

    private AppCompatDialog dialog;

    public HashSet<Integer> showSet;

    private LoadingDialog(Context context) {
        dialog = new AppCompatDialog(context, R.style.transparentDialog);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCanceledOnTouchOutside(false);

        showSet = new HashSet();
    }

    public static LoadingDialog getLoadingDialog(Context context) {
        if (context == null) {
            return null;
        }

        LoadingDialog loadingDialog;
        synchronized (map) {
            loadingDialog = map.get(context.hashCode());
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(context);
            }
            map.put(context.hashCode(), loadingDialog);
        }
        return loadingDialog;
    }

    public void setCancelable(boolean cancelable) {
        dialog.setCancelable(cancelable);
    }

    public void setOnDismissListener(@Nullable DialogInterface.OnDismissListener listener) {
        dialog.setOnDismissListener(listener);
    }

    public void show(int hashcode) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }

        synchronized (showSet) {
            showSet.add(hashcode);
        }
    }

    public void dismiss(int hashcode) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        synchronized (showSet) {
            showSet.remove(hashcode);

            if (showSet.size() == 0) {
                map.remove(this);
            }
        }
    }
}
