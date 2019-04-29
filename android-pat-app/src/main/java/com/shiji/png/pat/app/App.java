package com.shiji.png.pat.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.shiji.png.droid.payment.AndroidServiceManager;
import com.shiji.png.pat.app.router.ARouter;
import com.shiji.png.pat.printer.PrinterContext;
import com.shiji.png.pat.spat.service.SpatClient;

/**
 * @author bruce.wu
 * @since 2018/9/3
 */
public class App extends Application {

    private static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, getResources().getDisplayMetrics().toString());

        ARouter.init(this);

        if (BuildConfig.DEBUG) {
            SpatClient.logHttp();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        AndroidServiceManager.init(this);
        PrinterContext.init(this);
        MultiDex.install(this);
    }
}
