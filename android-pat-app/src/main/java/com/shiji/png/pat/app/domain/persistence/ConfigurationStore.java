package com.shiji.png.pat.app.domain.persistence;

import android.content.Context;

import com.shiji.png.pat.app.storage.AbstractPreferenceStorage;
import com.shiji.png.pat.model.ConfigurationInfo;

/**
 * @author bruce.wu
 * @date 2018/9/4
 */
public class ConfigurationStore extends AbstractPreferenceStorage<ConfigurationInfo> {
    public ConfigurationStore(Context context) {
        super(context, "conf-info", ConfigurationInfo.class);
    }
}
