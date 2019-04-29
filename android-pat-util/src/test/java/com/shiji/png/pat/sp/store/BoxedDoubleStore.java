package com.shiji.png.pat.sp.store;

import android.content.Context;

import com.shiji.png.pat.sp.AbstractPreferenceStorage;
import com.shiji.png.pat.sp.model.BoxedDoubleModel;

/**
 * @author bruce.wu
 * @date 2018/8/2
 */
public class BoxedDoubleStore extends AbstractPreferenceStorage<BoxedDoubleModel> {
    public BoxedDoubleStore(Context context) {
        super(context, "boxed-double");
    }
}
