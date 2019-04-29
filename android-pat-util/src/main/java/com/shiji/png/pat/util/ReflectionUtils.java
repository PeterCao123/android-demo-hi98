package com.shiji.png.pat.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author bruce.wu
 * @date 2018/8/1
 */
public class ReflectionUtils {

    public static <T> T newInstance(Class<T> type) throws Exception {
        Constructor<T> constructor = type.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    public static <T> Class<T> getActualType(Class<?> type) {
        return getActualType(type, 0);
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getActualType(Class<?> type, int index) {
        if (!(type.getGenericSuperclass() instanceof ParameterizedType)) {
            return null;
        }
        return (Class<T>)((ParameterizedType)type.getGenericSuperclass()).getActualTypeArguments()[index];
    }

    public static List<Field> getFieldsOfType(Class<?> type) {
        Class<?> t = type;
        List<Field> fields = new ArrayList<>();
        while (t != null) {
            Collections.addAll(fields, t.getDeclaredFields());
            t = t.getSuperclass();
        }
        return fields;
    }

    private ReflectionUtils() {}

}
