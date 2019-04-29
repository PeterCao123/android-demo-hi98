package com.shiji.png.pat.sp.store;

import android.content.Context;

import com.shiji.png.pat.sp.AbstractPreferenceStorage;
import com.shiji.png.pat.sp.model.PrimitiveBooleanModel;

/**
 * @author bruce.wu
 * @date 2018/8/2
 */
public class PrimitiveBooleanStore extends AbstractPreferenceStorage<PrimitiveBooleanModel> {
    public PrimitiveBooleanStore(Context context) {
        super(context, "primitive-boolean");
    }
}
