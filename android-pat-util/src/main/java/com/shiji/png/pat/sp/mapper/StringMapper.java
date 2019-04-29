package com.shiji.png.pat.sp.mapper;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.TextUtils;

import com.shiji.png.pat.sp.util.PreferenceUtils;

import java.lang.reflect.Field;

/**
 * @author bruce.wu
 * @date 2018/8/2
 */
public class StringMapper extends AbstractMapper {
    @Override
    protected boolean support(Class<?> type) {
        return String.class.equals(type);
    }

    @Override
    protected void fromField(SharedPreferences.Editor editor, String key, Field field, Object instance) throws Exception {
        Object val = field.get(instance);
        if (val != null) {
            editor.putString(key, (String) val);
        }
    }

    @Override
    protected void toField(SharedPreferences sp, String key, Field field, Object instance, Resources resources) throws Exception {
        String def = PreferenceUtils.getDefaultString(field, resources, null);
        String val = sp.getString(key, def);
        if (TextUtils.isEmpty(val)) {
            val = def;
        }
        field.set(instance, val);
    }
}
