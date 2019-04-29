package com.shiji.png.pat.app.domain.persistence;

import android.content.Context;

import com.shiji.png.pat.app.storage.AbstractPreferenceStorage;
import com.shiji.png.pat.model.CheckInfo;

/**
 * @author bruce.wu
 * @date 2018/9/4
 */
public class CheckStore extends AbstractPreferenceStorage<CheckInfo> {
    public CheckStore(Context context) {
        super(context, "check-info", CheckInfo.class);
    }
}
