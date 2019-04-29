package com.shiji.png.pat.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.shiji.png.pat.sp.mapper.Mapper;
import com.shiji.png.pat.sp.mapper.MapperChain;
import com.shiji.png.pat.sp.util.PreferenceUtils;
import com.shiji.png.pat.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author bruce.wu
 * @date 2018/8/1
 */
public abstract class AbstractPreferenceStorage<T> implements PreferenceStorage {

    private static final String IS_PERSISTENCE_KEY = "_persistence";

    private final Context context;
    private final SharedPreferences sp;

    private final Class<T> modelType;

    protected AbstractPreferenceStorage(Context context) {
        this(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    protected AbstractPreferenceStorage(Context context, String name) {
        this(context, name, Context.MODE_PRIVATE);
    }

    protected AbstractPreferenceStorage(Context context, String name, int mode) {
        this(context, context.getSharedPreferences(name, mode));
    }

    protected AbstractPreferenceStorage(Context context, SharedPreferences sp) {
        this.context = context;
        this.sp = sp;
        this.modelType = ReflectionUtils.getActualType(getClass(), 0);
    }

    public Class<T> getModelType() {
        return modelType;
    }

    @Override
    public boolean isPersistence() {
        return sp.getBoolean(IS_PERSISTENCE_KEY, false);
    }

    @Override
    public void save(Object model) {
        try {
            SharedPreferences.Editor editor = sp.edit();
            List<Field> fields = PreferenceUtils.getPreferenceFields(modelType);
            Mapper chain = MapperChain.builder().build();
            for (Field field : fields) {
                field.setAccessible(true);
                String key = PreferenceUtils.getKey(field, context.getResources());
                chain.fieldToPreference(editor, key, field, model);
            }
            editor.putBoolean(IS_PERSISTENCE_KEY, true);
            editor.apply();
        } catch (Exception e) {
            Log.e(getModelType().getSimpleName(), "save failed", e);
        }
    }

    @Override
    public T load() {
        if (!isPersistence()) {
            return null;
        }
        try {
            T instance = ReflectionUtils.newInstance(modelType);
            List<Field> fields = PreferenceUtils.getPreferenceFields(modelType);
            Mapper chain = MapperChain.builder().build();
            for (Field field : fields) {
                field.setAccessible(true);
                String key = PreferenceUtils.getKey(field, context.getResources());
                chain.preferenceToField(sp, key, field, instance, context.getResources());
            }
            return instance;
        } catch (Exception e) {
            Log.e(getModelType().getSimpleName(), "load failed", e);
            return null;
        }
    }

    @Override
    public void clear() {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }
}
