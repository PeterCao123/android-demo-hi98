package com.shiji.png.pat.sp.mapper;

import android.content.SharedPreferences;
import android.content.res.Resources;

import java.lang.reflect.Field;

/**
 * @author bruce.wu
 * @date 2018/8/1
 */
public interface Mapper {

    boolean fieldToPreference(SharedPreferences.Editor editor, String key,
                              Field field, Object instance) throws Exception;

    boolean preferenceToField(SharedPreferences sp, String key,
                              Field field, Object instance, Resources resources) throws Exception;

}
