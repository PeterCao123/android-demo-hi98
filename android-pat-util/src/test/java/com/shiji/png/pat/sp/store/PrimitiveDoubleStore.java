package com.shiji.png.pat.sp.store;

import android.content.Context;

import com.shiji.png.pat.sp.AbstractPreferenceStorage;
import com.shiji.png.pat.sp.model.PrimitiveDoubleModel;

/**
 * @author bruce.wu
 * @date 2018/8/2
 */
public class PrimitiveDoubleStore extends AbstractPreferenceStorage<PrimitiveDoubleModel> {
    public PrimitiveDoubleStore(Context context) {
        super(context, "primitive-double");
    }
}
