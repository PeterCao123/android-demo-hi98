package com.shiji.png.pat.printer;

import android.app.Application;

/**
 * @author bruce.wu
 * @since 2019/1/30 16:25
 */
public abstract class PrinterContext {

    private static Application appContext;

    public static void init(Application application) {
        appContext = application;
    }

    public static Application app() {
        if (appContext == null) {
            throw new NullPointerException("app context is null");
        }
        return appContext;
    }

}
