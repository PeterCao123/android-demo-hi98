package com.shiji.png.pat.sp.mapper;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.shiji.png.pat.sp.util.PreferenceUtils;

import java.lang.reflect.Field;

/**
 * @author bruce.wu
 * @date 2018/8/2
 */
public class DoubleMapper extends AbstractMapper {

    @Override
    protected boolean support(Class<?> type) {
        return Double.class.equals(type) || double.class.equals(type);
    }

    @Override
    protected void fromField(SharedPreferences.Editor editor, String key, Field field, Object instance) throws Exception {
        Object val = field.get(instance);
        if (val != null) {
            editor.putString(key, PreferenceUtils.getDoubleString(field, (double) val));
        }
    }

    @Override
    protected void toField(SharedPreferences sp, String key, Field field, Object instance, Resources resources) throws Exception {
        String def = PreferenceUtils.getDefaultString(field, resources, "0");
        String val = sp.getString(key, def);
        field.set(instance, PreferenceUtils.getDoubleValue(field, val));
    }

}
