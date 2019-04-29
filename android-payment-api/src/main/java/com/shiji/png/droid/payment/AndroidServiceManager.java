package com.shiji.png.droid.payment;

import android.app.Application;

import com.shiji.png.payment.ServiceManager;

/**
 * @author bruce.wu
 * @since 2018/12/5 16:51
 */
public final class AndroidServiceManager {

    public static void init(Application application) {
        ApplicationHolder.init(application);
        ServiceManager.assignScanner(new AndroidServiceScanner(application));
        ServiceManager.assignSelector(new AndroidServiceSelector(application));
    }

}
