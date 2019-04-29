package com.shiji.png.pat.sp.mapper;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.shiji.png.pat.sp.util.PreferenceUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * @author bruce.wu
 * @since 2018/12/13 14:03
 */
public class BigDecimalMapper extends AbstractMapper {

    @Override
    protected boolean support(Class<?> type) {
        return BigDecimal.class.equals(type);
    }

    @Override
    protected void fromField(SharedPreferences.Editor editor, String key, Field field, Object instance) throws Exception {
        Object val = field.get(instance);
        if (val != null) {
            editor.putString(key, PreferenceUtils.getBigDecimalString(field, (BigDecimal) val));
        }
    }

    @Override
    protected void toField(SharedPreferences sp, String key, Field field, Object instance, Resources resources) throws Exception {
        String def = PreferenceUtils.getDefaultString(field, resources, "0");
        String val = sp.getString(key, def);
        field.set(instance, PreferenceUtils.getBigDecimalValue(field, val));
    }

}
