package com.shiji.png.pat.sp.util;

import android.content.res.Resources;
import android.text.TextUtils;

import com.shiji.png.pat.sp.annotation.Default;
import com.shiji.png.pat.sp.annotation.Ignore;
import com.shiji.png.pat.sp.annotation.Key;
import com.shiji.png.pat.sp.annotation.Precision;
import com.shiji.png.pat.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.NO_ID;

/**
 * @author bruce.wu
 * @date 2018/8/1
 */
public class PreferenceUtils {

    /**
     * get all preference fields of model
     *
     * @param type model type
     * @return preference fields
     */
    public static List<Field> getPreferenceFields(Class<?> type) {
        List<Field> fields = ReflectionUtils.getFieldsOfType(type);
        List<Field> preferenceFields = new ArrayList<>(fields.size());
        for (Field field : fields) {
            if (field.isAnnotationPresent(Ignore.class)
                    || Modifier.isStatic(field.getModifiers())
                    || Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            preferenceFields.add(field);
        }
        return preferenceFields;
    }

    /**
     * get preference key of model field
     *
     * @param field field of model
     * @param resources resource accessor
     * @return preference key
     */
    public static String getKey(Field field, Resources resources) {
        if (field.isAnnotationPresent(Key.class)) {
            Key annotation = field.getAnnotation(Key.class);
            if (annotation.resId() != NO_ID) {
                return resources.getString(annotation.resId());
            }
            if (!TextUtils.isEmpty(annotation.name())) {
                return annotation.name();
            }
        }
        return field.getName();
    }

    /**
     * get default preference value as string
     *
     * @param field model field
     * @param resources resource accessor
     * @param value default value
     * @return the default value
     */
    public static String getDefaultString(Field field, Resources resources, String value) {
        if (field.isAnnotationPresent(Default.class)) {
            Default annotation = field.getAnnotation(Default.class);
            if (annotation.resId() != NO_ID) {
                return resources.getString(annotation.resId());
            }
            if (!TextUtils.isEmpty(annotation.value())) {
                return annotation.value();
            }
        }
        return value;
    }

    public static boolean getDefaultBoolean(Field field, Resources resources, boolean value) {
        String val = getDefaultString(field, resources, "");
        if (TextUtils.isEmpty(val)) {
            return value;
        }
        return Boolean.valueOf(val);
    }

    public static int getDefaultInt(Field field, Resources resources, int value) {
        String val = getDefaultString(field, resources, "");
        if (TextUtils.isEmpty(val)) {
            return value;
        }
        return Integer.valueOf(val);
    }

    public static long getDefaultLong(Field field, Resources resources, long value) {
        String val = getDefaultString(field, resources, "");
        if (TextUtils.isEmpty(val)) {
            return value;
        }
        return Long.valueOf(val);
    }

    public static double getDoubleValue(Field field, String value) {
        return getBigDecimalValue(field, value).doubleValue();
    }

    public static String getDoubleString(Field field, double value) {
        if (field.isAnnotationPresent(Precision.class)) {
            Precision annotation = field.getAnnotation(Precision.class);
            return BigDecimal.valueOf(value).setScale(annotation.scale(), annotation.roundingMode()).toPlainString();
        }
        return BigDecimal.valueOf(value).toPlainString();
    }

    public static BigDecimal getBigDecimalValue(Field field, String value) {
        if (field.isAnnotationPresent(Precision.class)) {
            Precision annotation = field.getAnnotation(Precision.class);
            return new BigDecimal(value).setScale(annotation.scale(), annotation.roundingMode());
        }
        return new BigDecimal(value);
    }

    public static String getBigDecimalString(Field field, BigDecimal value) {
        if (field.isAnnotationPresent(Precision.class)) {
            Precision annotation = field.getAnnotation(Precision.class);
            return value.setScale(annotation.scale(), annotation.roundingMode()).toPlainString();
        }
        return value.toPlainString();
    }

    private PreferenceUtils() {}

}
