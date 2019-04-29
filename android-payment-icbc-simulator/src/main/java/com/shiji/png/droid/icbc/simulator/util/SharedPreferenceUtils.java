package com.shiji.png.droid.icbc.simulator.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class SharedPreferenceUtils {
    public static String get(Context context, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sp.getString(key, null);
        if (TextUtils.isEmpty(value)) {
            return "";
        }
        return value;
    }
}
