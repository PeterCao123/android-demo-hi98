package com.shiji.png.pat.app.ui.dialog;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import com.shiji.png.pat.app.R;
import com.shiji.png.pat.app.functional.Action;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * @author bruce.wu
 * @date 2018/9/4
 */
public class DialogService {

    private final Context context;

    private int titleRes;
    private int positiveTextRes = android.R.string.ok;
    private int negativeTextRes = android.R.string.cancel;

    private boolean overrideNegative = false;
    private boolean overridePositive = false;

    private boolean cancelable = false;

    private Runnable positive;
    private Runnable negative;
    private Action<Integer> choice;
    private Action<String> input;

    public DialogService(Context context) {
        this.context = context;
        this.titleRes = R.string.app_name;
    }

    public DialogService title(@StringRes int titleRes) {
        this.titleRes = titleRes;
        return this;
    }

    public DialogService positiveTextRes(@StringRes int positiveTextRes) {
        this.positiveTextRes = positiveTextRes;
        return this;
    }

    public DialogService negativeTextRes(@StringRes int negativeTextRes) {
        this.negativeTextRes = negativeTextRes;
        return this;
    }

    public DialogService positive(Runnable runnable) {
        this.positive = runnable;
        return this;
    }

    public DialogService negative(Runnable runnable) {
        this.negative = runnable;
        return this;
    }

    public DialogService overrideNegative() {
        this.overrideNegative = true;
        return this;
    }

    public DialogService overridePositive() {
        this.overridePositive = true;
        return this;
    }

    public DialogService cancelable() {
        this.cancelable = true;
        return this;
    }

    public DialogService choice(Action<Integer> consumer) {
        this.choice = consumer;
        return this;
    }

    public DialogService input(Action<String> consumer) {
        this.input = consumer;
        return this;
    }

    public void alert(Throwable tr) {
        alert(tr.getMessage());
    }

    public void alert(@StringRes int resId) {
        alert(context.getString(resId));
    }

    public void alert(String message) {
        new AlertDialog.Builder(context)
                .setTitle(titleRes)
                .setMessage(message)
                .setCancelable(cancelable)
                .setPositiveButton(positiveTextRes, (dialog, which) -> {
                    dialog.dismiss();
                    if (positive != null) {
                        positive.run();
                    }
                })
                .show();
    }

    public void confirm(@StringRes int resId) {
        confirm(context.getString(resId));
    }

    public void confirm(String message) {
        AlertDialog dlg = new AlertDialog.Builder(context)
                .setTitle(titleRes)
                .setMessage(message)
                .setCancelable(cancelable)
                .setNegativeButton(negativeTextRes, (dialog, which) -> {
                    dialog.dismiss();
                    if (negative != null) {
                        negative.run();
                    }
                })
                .setPositiveButton(positiveTextRes, (dialog, which) -> {
                    dialog.dismiss();
                    if (positive != null) {
                        positive.run();
                    }
                })
                .show();
        if (overrideNegative && negative != null) {
            dlg.getButton(BUTTON_NEGATIVE).setOnClickListener(v -> negative.run());
        }
        if (overridePositive && positive != null) {
            dlg.getButton(BUTTON_POSITIVE).setOnClickListener(v -> positive.run());
        }
    }

    public void singleChoice(String[] items) {
        new AlertDialog.Builder(context)
                .setTitle(titleRes)
                .setCancelable(cancelable)
                .setSingleChoiceItems(items, 0, (dialog, which) -> {
                    dialog.dismiss();
                    choice.accept(which);
                })
                .show();
    }

    public void showInput(@LayoutRes int viewRes) {
        new AlertDialog.Builder(context)
                .setTitle(titleRes)
                .setView(viewRes)
                .setCancelable(cancelable)
                .setNegativeButton(negativeTextRes, (dialog, which) -> {
                    dialog.dismiss();
                    if (negative != null) {
                        negative.run();
                    }
                })
                .setPositiveButton(positiveTextRes, (dialog, which) -> {
                    dialog.dismiss();
                    EditText editText = ((AlertDialog)dialog).findViewById(R.id.text);
                    if (editText != null) {
                        input.accept(editText.getText().toString());
                    } else {
                        input.accept("");
                    }
                })
                .show();
    }

}
