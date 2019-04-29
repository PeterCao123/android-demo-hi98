package com.shiji.png.droid.icbc.simulator.ui.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;

import com.shiji.png.droid.icbc.simulator.R;


/**
 * @author bruce.wu
 * @date 2018/9/4
 */
public final class ProcessDialogService {

    private final Context context;

    private ProgressDialog progressDialog;

    private String message;

    public ProcessDialogService(Context context,String prefix) {
        this.context = context;
        this.message(R.string.processing,prefix);
    }

    public ProcessDialogService message(@StringRes int resId) {
        this.message = context.getString(resId);
        return this;
    }
    public ProcessDialogService message(@StringRes int resId,String prefix) {
        this.message = context.getString(resId);
        this.message = prefix +"\n" + this.message;
        return this;
    }
    public ProcessDialogService message(String message) {
        this.message = message;
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
