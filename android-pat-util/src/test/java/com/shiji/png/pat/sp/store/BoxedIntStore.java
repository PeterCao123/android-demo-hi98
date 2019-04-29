package com.shiji.png.pat.sp.store;

import android.content.Context;

import com.shiji.png.pat.sp.AbstractPreferenceStorage;
import com.shiji.png.pat.sp.model.BoxedIntModel;

/**
 * @author bruce.wu
 * @date 2018/8/2
 */
public class BoxedIntStore extends AbstractPreferenceStorage<BoxedIntModel> {
    public BoxedIntStore(Context context) {
        super(context, "boxed-int");
    }
}
