package com.shiji.png.droid.icbc.simulator.util;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;

import com.shiji.png.droid.icbc.simulator.model.Converter;

public class DeviceUtils {
    public static boolean siScreenOn(Context context){
        PowerManager powerManager = (PowerManager) context
                .getSystemService(Context.POWER_SERVICE);
        return powerManager.isScreenOn();
    }
    public static void awakeScreen(Context context){
        PowerManager pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //获取电源管理器对象
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        wl.acquire();
    }
    public static void wakeUp(Context context) {
        PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        if (!pm.isScreenOn()) {
            int flag = PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK;
            PowerManager.WakeLock wl = pm.newWakeLock(flag, "ppa:bright");
            wl.acquire(500);
            wl.release();
        }

        //unlock
        KeyguardManager km = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("ppa:unlock");
        //kl.reenableKeyguard();
        kl.disableKeyguard();

        //play sound
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
    }
}
