package com.shiji.png.droid.payment;

import android.app.Application;

/**
 * @author bruce.wu
 * @since 2018/12/5 16:37
 */
public final class ApplicationHolder {

    private static Application appContext;

    public static void init(Application application) {
        appContext = application;
    }

    public static Application app() {
        return appContext;
    }

}
