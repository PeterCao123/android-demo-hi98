package com.shiji.png.pinpad.agent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author bruce.wu
 * @date 2018/10/19
 */
public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, intent.toString());
        MyService.start(context);
    }

}
