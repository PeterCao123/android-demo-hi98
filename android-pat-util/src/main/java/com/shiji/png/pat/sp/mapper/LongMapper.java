package com.shiji.png.pat.sp.mapper;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.shiji.png.pat.sp.util.PreferenceUtils;

import java.lang.reflect.Field;

/**
 * @author bruce.wu
 * @date 2018/8/2
 */
public class LongMapper extends AbstractMapper {
    @Override
    protected boolean support(Class<?> type) {
        return Long.class.equals(type) || long.class.equals(type);
    }

    @Override
    protected void fromField(SharedPreferences.Editor editor, String key, Field field, Object instance) throws Exception {
        Object val = field.get(instance);
        if (val != null) {
            editor.putLong(key, (long) val);
        }
    }

    @Override
    protected void toField(SharedPreferences sp, String key, Field field, Object instance, Resources resources) throws Exception {
        long def = PreferenceUtils.getDefaultLong(field, resources, 0);
        field.set(instance, sp.getLong(key, def));
    }

}
