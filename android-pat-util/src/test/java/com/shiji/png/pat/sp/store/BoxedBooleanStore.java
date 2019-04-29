package com.shiji.png.pat.sp.store;

import android.content.Context;

import com.shiji.png.pat.sp.AbstractPreferenceStorage;
import com.shiji.png.pat.sp.model.BoxedBooleanModel;

/**
 * @author bruce.wu
 * @date 2018/8/2
 */
public class BoxedBooleanStore extends AbstractPreferenceStorage<BoxedBooleanModel> {
    public BoxedBooleanStore(Context context) {
        super(context, "boxed-boolean");
    }
}
