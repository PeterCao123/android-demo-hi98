package com.shiji.png.pinpad.agent.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.shiji.png.droid.payment.AndroidServiceManager;
import com.shiji.png.pinpad.agent.MyService;
import com.shiji.png.pinpad.model.ConfigUtils;

/**
 * @author bruce.wu
 * @date 2018/10/22
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidServiceManager.init(this);
        MyService.start(this);
        ConfigUtils.initDisplayOpinion(getApplicationContext());
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.i("App", "onTrimMemory...level=" + level);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
