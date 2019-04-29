package com.shiji.png.pat.sp.store;

import android.content.Context;

import com.shiji.png.pat.sp.AbstractPreferenceStorage;
import com.shiji.png.pat.sp.model.BoxedLongModel;

/**
 * @author bruce.wu
 * @date 2018/8/2
 */
public class BoxedLongStore extends AbstractPreferenceStorage<BoxedLongModel> {
    public BoxedLongStore(Context context) {
        super(context, "boxed-long");
    }
}
