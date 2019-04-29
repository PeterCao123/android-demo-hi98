package com.shiji.png.pat.sp.store;

import android.content.Context;

import com.shiji.png.pat.sp.AbstractPreferenceStorage;
import com.shiji.png.pat.sp.model.PrimitiveIntModel;

/**
 * @author bruce.wu
 * @date 2018/8/2
 */
public class PrimitiveIntStore extends AbstractPreferenceStorage<PrimitiveIntModel> {
    public PrimitiveIntStore(Context context) {
        super(context, "primitive-int");
    }
}
