package com.shiji.png.pat.sp.store;

import android.content.Context;
import android.content.SharedPreferences;

import com.shiji.png.pat.sp.AbstractPreferenceStorage;
import com.shiji.png.pat.sp.model.FullModel;

/**
 * @author bruce.wu
 * @date 2018/8/2
 */
public class FullStore extends AbstractPreferenceStorage<FullModel> {
    public FullStore(Context context) {
        super(context, "full");
    }

    public FullStore(Context context, SharedPreferences sp) {
        super(context, sp);
    }
}
