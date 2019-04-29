package com.shiji.png.pat.sp.mapper;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bruce.wu
 * @date 2018/8/1
 */
public class MapperChain implements Mapper {

    public static Builder builder() {
        return new Builder();
    }

    private static final String TAG = "MapperChain";

    private final List<Mapper> chain = new ArrayList<>();

    public void appendMapper(Mapper mapper) {
        chain.add(mapper);
    }

    @Override
    public boolean fieldToPreference(SharedPreferences.Editor editor, String key, Field field, Object instance) throws Exception {
        for (Mapper mapper : chain) {
            if (mapper.fieldToPreference(editor, key, field, instance)) {
                return true;
            }
        }
        //unsupported field type
        Log.e(TAG, "save unsupported field type: " + field.getType().getName());
        return false;
    }

    @Override
    public boolean preferenceToField(SharedPreferences sp, String key, Field field, Object instance, Resources resources) throws Exception {
        for (Mapper mapper : chain) {
            if (mapper.preferenceToField(sp, key, field, instance, resources)) {
                return true;
            }
        }
        //unsupported field type
        Log.e(TAG, "load unsupported field type: " + field.getType().getName());
        return false;
    }

    public static class Builder {

        private final MapperChain chain;

        private Builder() {
            chain = new MapperChain();
            chain.appendMapper(new BooleanMapper());
            chain.appendMapper(new DoubleMapper());
            chain.appendMapper(new IntMapper());
            chain.appendMapper(new LongMapper());
            chain.appendMapper(new StringMapper());
            chain.appendMapper(new BigDecimalMapper());
        }

        public MapperChain build() {
            return chain;
        }

    }

}
