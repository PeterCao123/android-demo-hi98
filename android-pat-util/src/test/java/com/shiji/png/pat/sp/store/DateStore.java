package com.shiji.png.pat.sp.store;

import android.content.Context;

import com.shiji.png.pat.sp.AbstractPreferenceStorage;
import com.shiji.png.pat.sp.model.DateModel;

/**
 * @author bruce.wu
 * @date 2018/8/2
 */
public class DateStore extends AbstractPreferenceStorage<DateModel> {
    public DateStore(Context context) {
        super(context, "date");
    }
}
