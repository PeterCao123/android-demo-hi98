package com.shiji.png.pat.sp.store;

import android.content.Context;

import com.shiji.png.pat.sp.AbstractPreferenceStorage;
import com.shiji.png.pat.sp.model.StringModel;

/**
 * @author bruce.wu
 * @date 2018/8/2
 */
public class StringStore extends AbstractPreferenceStorage<StringModel> {
    public StringStore(Context context) {
        super(context, "string");
    }
}
