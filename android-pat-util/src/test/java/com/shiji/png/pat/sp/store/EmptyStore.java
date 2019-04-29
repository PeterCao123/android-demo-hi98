package com.shiji.png.pat.sp.store;

import android.content.Context;

import com.shiji.png.pat.sp.AbstractPreferenceStorage;
import com.shiji.png.pat.sp.model.EmptyModel;

/**
 * @author bruce.wu
 * @date 2018/8/2
 */
public class EmptyStore extends AbstractPreferenceStorage<EmptyModel> {
    public EmptyStore(Context context) {
        super(context, "empty");
    }
}
