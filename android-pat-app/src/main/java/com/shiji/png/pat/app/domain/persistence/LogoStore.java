package com.shiji.png.pat.app.domain.persistence;

import android.content.Context;

import com.shiji.png.pat.app.storage.AbstractPreferenceStorage;
import com.shiji.png.pat.model.LogoInfo;

/**
 * @author bruce.wu
 * @date 2018/9/4
 */
public class LogoStore extends AbstractPreferenceStorage<LogoInfo> {
    public LogoStore(Context context) {
        super(context, "logo-info", LogoInfo.class);
    }
}
