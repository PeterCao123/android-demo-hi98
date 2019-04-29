package com.shiji.png.pat.sp.store;

import android.content.Context;

import com.shiji.png.pat.sp.AbstractPreferenceStorage;
import com.shiji.png.pat.sp.model.BigDecimalModel;

/**
 * @author bruce.wu
 * @since 2018/12/13 14:07
 */
public class BigDecimalStore extends AbstractPreferenceStorage<BigDecimalModel> {
    public BigDecimalStore(Context context) {
        super(context, "string");
    }
}
