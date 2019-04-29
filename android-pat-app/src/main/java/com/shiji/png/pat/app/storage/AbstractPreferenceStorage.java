package com.shiji.png.pat.app.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;

/**
 * @author bruce.wu
 * @since 2019/2/13 16:04
 */
public abstract class AbstractPreferenceStorage<T> implements PreferenceStorage<T> {

    private static final String IS_PERSISTENCE_KEY = "_persistence";

    private final SharedPreferences sp;
    private final Class<T> modelType;

    protected AbstractPreferenceStorage(Context context, Class<T> modelType) {
        this(context, PreferenceManager.getDefaultSharedPreferences(context), modelType);
    }

    protected AbstractPreferenceStorage(Context context, String name, Class<T> modelType) {
        this(context, context.getSharedPreferences(name, Context.MODE_PRIVATE), modelType);
    }

    protected AbstractPreferenceStorage(Context context, SharedPreferences sp, Class<T> modelType) {
        this.sp = sp;
        this.modelType = modelType;
    }

    @Override
    public boolean isPersistence() {
        return sp.getBoolean(IS_PERSISTENCE_KEY, false);
    }

    @Override
    public void save(T model) {
        try {
            SharedPreferences.Editor editor = sp.edit();
            Field[] fields = model.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (ignored(field)) {
                    continue;
                }
                field.setAccessible(true);
                saveFromField(editor, model, field);
            }
            editor.putBoolean(IS_PERSISTENCE_KEY, true);
            editor.apply();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "save failed", e);
        }
    }

    @Override
    public T load() {
        if (!isPersistence()) {
            return null;
        }
        try {
            Constructor<T> constructor = modelType.getDeclaredConstructor();
            constructor.setAccessible(true);
            T instance = constructor.newInstance();

            Field[] fields = modelType.getDeclaredFields();
            for (Field field : fields) {
                if (ignored(field)) {
                    continue;
                }
                field.setAccessible(true);
                loadToField(this.sp, instance, field);
            }

            return instance;
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "load failed", e);
            return null;
        }
    }

    @Override
    public void clear() {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    private boolean ignored(Field field) {
        return field.isAnnotationPresent(Ignore.class)
                || Modifier.isStatic(field.getModifiers())
                || Modifier.isTransient(field.getModifiers());
    }

    private String getKey(Field field) {
        if (field.isAnnotationPresent(Key.class)) {
            Key annotation = field.getAnnotation(Key.class);
            return annotation.name();
        }
        return field.getName();
    }

    private String getDefault(Field field, String def) {
        if (field.isAnnotationPresent(Default.class)) {
            Default annotation = field.getAnnotation(Default.class);
            return annotation.value();
        }
        return def;
    }

    private void saveFromField(SharedPreferences.Editor editor, Object obj, Field field) throws IllegalAccessException {
        Class<?> type = field.getType();
        String key = getKey(field);
        if (String.class.equals(type)) {
            String val = (String)field.get(obj);
            if (val != null) {
                editor.putString(key, val);
            }
            return;
        }
        if (BigDecimal.class.equals(type)) {
            BigDecimal val = (BigDecimal)field.get(obj);
            if (val != null) {
                editor.putString(key, val.toPlainString());
            }
            return;
        }
        if (boolean.class.equals(type)) {
            editor.putBoolean(key, field.getBoolean(obj));
            return;
        }
        if (Boolean.class.equals(type)) {
            Object val = field.get(obj);
            if (val != null) {
                editor.putBoolean(key, (boolean)val);
            }
            return;
        }
        throw new IllegalAccessException("Unsupported field type: " + type.getName());
    }

    private void loadToField(SharedPreferences sp, Object obj, Field field) throws IllegalAccessException {
        Class<?> type = field.getType();
        String key = getKey(field);

        if (String.class.equals(type)) {
            field.set(obj, sp.getString(key, getDefault(field, null)));
            return;
        }

        if (BigDecimal.class.equals(type)) {
            field.set(obj, new BigDecimal(sp.getString(key, getDefault(field, "0"))));
            return;
        }

        if (boolean.class.equals(type) || Boolean.class.equals(type)) {
            boolean def = Boolean.parseBoolean(getDefault(field, "false"));
            field.set(obj, sp.getBoolean(key, def));
            return;
        }

        throw new IllegalAccessException("Unsupported field type: " + type.getName());
    }

}
