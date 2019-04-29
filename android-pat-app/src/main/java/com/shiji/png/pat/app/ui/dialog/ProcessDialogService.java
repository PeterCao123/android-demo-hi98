package com.shiji.png.pat.app.ui.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.StringRes;

import com.shiji.png.pat.app.R;

/**
 * @author bruce.wu
 * @date 2018/9/4
 */
public final class ProcessDialogService {

    private final Context context;

    private ProgressDialog progressDialog;

    private String message;

    public ProcessDialogService(Context context) {
        this.context = context;
        this.message(R.string.loading);
    }

    public ProcessDialogService message(@StringRes int resId) {
        this.message = context.getString(resId);
        return this;
    }

    public void show() {
        hide();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hide() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
