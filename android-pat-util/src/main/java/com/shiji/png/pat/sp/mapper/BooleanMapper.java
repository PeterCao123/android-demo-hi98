package com.shiji.png.pat.sp.mapper;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.shiji.png.pat.sp.util.PreferenceUtils;

import java.lang.reflect.Field;

/**
 * @author bruce.wu
 * @date 2018/8/2
 */
public class BooleanMapper extends AbstractMapper {

    @Override
    protected boolean support(Class<?> type) {
        return Boolean.class.equals(type) || boolean.class.equals(type);
    }

    @Override
    protected void fromField(SharedPreferences.Editor editor, String key, Field field, Object instance) throws Exception {
        Object val = field.get(instance);
        if (val != null) {
            editor.putBoolean(key, (boolean) field.get(instance));
        }
    }

    @Override
    protected void toField(SharedPreferences sp, String key, Field field, Object instance, Resources resources) throws Exception {
        boolean def = PreferenceUtils.getDefaultBoolean(field, resources, false);
        field.set(instance, sp.getBoolean(key, def));
    }

}
