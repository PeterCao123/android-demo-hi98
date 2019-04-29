package com.shiji.png.pat.sp.mapper;

import android.content.SharedPreferences;
import android.content.res.Resources;

import java.lang.reflect.Field;

/**
 * @author bruce.wu
 * @date 2018/8/1
 */
public abstract class AbstractMapper implements Mapper {

    protected abstract boolean support(Class<?> type);

    protected abstract void fromField(SharedPreferences.Editor editor, String key,
                                      Field field, Object instance) throws Exception;

    protected abstract void toField(SharedPreferences sp, String key,
                                    Field field, Object instance, Resources resources) throws Exception;

    @Override
    public boolean fieldToPreference(SharedPreferences.Editor editor, String key,
                                     Field field, Object instance) throws Exception {
        if (!support(field.getType())) {
            return false;
        }
        fromField(editor, key, field, instance);
        return true;
    }

    @Override
    public boolean preferenceToField(SharedPreferences sp, String key,
                                     Field field, Object instance, Resources resources) throws Exception {
        if (!support(field.getType())) {
            return false;
        }
        toField(sp, key, field, instance, resources);
        return true;
    }
}
