package com.shiji.png.pat.app.domain;

import android.support.annotation.StringRes;

/**
 * @author bruce.wu
 * @date 2018/9/4
 */
public class InvalidPreferenceException extends Exception {

    private final int resId;

    public InvalidPreferenceException(@StringRes int resId) {
        super();
        this.resId = resId;
    }

    public int getResId() {
        return resId;
    }
}
