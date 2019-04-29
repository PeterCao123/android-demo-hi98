package com.shiji.png.pat.app.domain.persistence;

import android.content.Context;

import com.shiji.png.pat.app.domain.model.PrefInfo;
import com.shiji.png.pat.app.storage.AbstractPreferenceStorage;

/**
 * @author bruce.wu
 * @date 2018/9/4
 */
public class PrefStore extends AbstractPreferenceStorage<PrefInfo> {

    public PrefStore(Context context) {
        super(context, PrefInfo.class);
    }

    @Override
    public boolean isPersistence() {
        return true;
    }

}
