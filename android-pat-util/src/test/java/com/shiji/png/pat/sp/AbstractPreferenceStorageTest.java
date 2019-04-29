package com.shiji.png.pat.sp;

import android.content.Context;

import com.shiji.png.pat.sp.model.BoxedBooleanModel;
import com.shiji.png.pat.sp.model.BoxedDoubleModel;
import com.shiji.png.pat.sp.model.BoxedIntModel;
import com.shiji.png.pat.sp.model.BoxedLongModel;
import com.shiji.png.pat.sp.model.DateModel;
import com.shiji.png.pat.sp.model.EmptyModel;
import com.shiji.png.pat.sp.model.FullModel;
import com.shiji.png.pat.sp.model.PrimitiveBooleanModel;
import com.shiji.png.pat.sp.model.PrimitiveDoubleModel;
import com.shiji.png.pat.sp.model.PrimitiveIntModel;
import com.shiji.png.pat.sp.model.PrimitiveLongModel;
import com.shiji.png.pat.sp.model.StringModel;
import com.shiji.png.pat.sp.store.BoxedBooleanStore;
import com.shiji.png.pat.sp.store.BoxedDoubleStore;
import com.shiji.png.pat.sp.store.BoxedIntStore;
import com.shiji.png.pat.sp.store.BoxedLongStore;
import com.shiji.png.pat.sp.store.DateStore;
import com.shiji.png.pat.sp.store.EmptyStore;
import com.shiji.png.pat.sp.store.FullStore;
import com.shiji.png.pat.sp.store.PrimitiveBooleanStore;
import com.shiji.png.pat.sp.store.PrimitiveDoubleStore;
import com.shiji.png.pat.sp.store.PrimitiveIntStore;
import com.shiji.png.pat.sp.store.PrimitiveLongStore;
import com.shiji.png.pat.sp.store.StringStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author bruce.wu
 * @date 2018/8/2
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public class AbstractPreferenceStorageTest {

    private Context appContext;

    @Before
    public void setUp() {
        appContext = RuntimeEnvironment.application;
        ShadowLog.stream = System.out;
    }

    @Test
    public void getModelType() {
        assertEquals(EmptyModel.class, new EmptyStore(appContext).getModelType());
    }

    @Test
    public void isPersistence_default() {
        assertFalse(new EmptyStore(appContext).isPersistence());
    }

    @Test
    public void isPersistence_saved() {
        new EmptyStore(appContext).save(new EmptyModel());
        assertTrue(new EmptyStore(appContext).isPersistence());
    }

    @Test
    public void save_none() {
        assertNull(new EmptyStore(appContext).load());
    }

    @Test
    public void save_string_default() {
        StringModel model = new StringModel();
        new StringStore(appContext).save(model);
        StringModel m = new StringStore(appContext).load();
        assertNull(m.getValue());
    }

    @Test
    public void save_string_config() {
        StringModel model = new StringModel();
        model.setValue("hello");
        new StringStore(appContext).save(model);
        StringModel m = new StringStore(appContext).load();
        assertEquals("hello", m.getValue());
    }

    @Test
    public void save_boolean_primitive_default() {
        PrimitiveBooleanModel model = new PrimitiveBooleanModel();
        new PrimitiveBooleanStore(appContext).save(model);
        PrimitiveBooleanModel m = new PrimitiveBooleanStore(appContext).load();
        assertFalse(m.isChecked());
    }

    @Test
    public void save_boolean_primitive_config() {
        PrimitiveBooleanModel model = new PrimitiveBooleanModel();
        model.setChecked(true);
        new PrimitiveBooleanStore(appContext).save(model);
        PrimitiveBooleanModel m = new PrimitiveBooleanStore(appContext).load();
        assertTrue(m.isChecked());
    }

    @Test
    public void save_boolean_boxed_default() {
        BoxedBooleanModel model = new BoxedBooleanModel();
        new BoxedBooleanStore(appContext).save(model);
        BoxedBooleanModel m = new BoxedBooleanStore(appContext).load();
        assertFalse(m.getChecked());
    }

    @Test
    public void save_boolean_boxed_config() {
        BoxedBooleanModel model = new BoxedBooleanModel();
        model.setChecked(true);
        new BoxedBooleanStore(appContext).save(model);
        BoxedBooleanModel m = new BoxedBooleanStore(appContext).load();
        assertTrue(m.getChecked());
    }

    @Test
    public void save_int_primitive_default() {
        PrimitiveIntModel model = new PrimitiveIntModel();
        new PrimitiveIntStore(appContext).save(model);
        PrimitiveIntModel m = new PrimitiveIntStore(appContext).load();
        assertEquals(0, m.getValue());
    }

    @Test
    public void save_int_primitive_positive() {
        PrimitiveIntModel model = new PrimitiveIntModel();
        model.setValue(Integer.MAX_VALUE);
        new PrimitiveIntStore(appContext).save(model);
        PrimitiveIntModel m = new PrimitiveIntStore(appContext).load();
        assertEquals(Integer.MAX_VALUE, m.getValue());
    }

    @Test
    public void save_int_primitive_negative() {
        PrimitiveIntModel model = new PrimitiveIntModel();
        model.setValue(-123456);
        new PrimitiveIntStore(appContext).save(model);
        PrimitiveIntModel m = new PrimitiveIntStore(appContext).load();
        assertEquals(-123456, m.getValue());
    }

    @Test
    public void save_int_boxed_default() {
        BoxedIntModel model = new BoxedIntModel();
        new BoxedIntStore(appContext).save(model);
        BoxedIntModel m = new BoxedIntStore(appContext).load();
        assertEquals(0, m.getValue().intValue());
    }

    @Test
    public void save_int_boxed_positive() {
        BoxedIntModel model = new BoxedIntModel();
        model.setValue(Integer.MAX_VALUE);
        new BoxedIntStore(appContext).save(model);
        BoxedIntModel m = new BoxedIntStore(appContext).load();
        assertEquals(Integer.MAX_VALUE, m.getValue().intValue());
    }

    @Test
    public void save_int_boxed_negative() {
        BoxedIntModel model = new BoxedIntModel();
        model.setValue(-123456);
        new BoxedIntStore(appContext).save(model);
        BoxedIntModel m = new BoxedIntStore(appContext).load();
        assertEquals(-123456, m.getValue().intValue());
    }

    @Test
    public void save_long_primitive_default() {
        PrimitiveLongModel model = new PrimitiveLongModel();
        new PrimitiveLongStore(appContext).save(model);
        PrimitiveLongModel m = new PrimitiveLongStore(appContext).load();
        assertEquals(0, m.getValue());
    }

    @Test
    public void save_long_primitive_positive() {
        PrimitiveLongModel model = new PrimitiveLongModel();
        model.setValue(Long.MAX_VALUE);
        new PrimitiveLongStore(appContext).save(model);
        PrimitiveLongModel m = new PrimitiveLongStore(appContext).load();
        assertEquals(Long.MAX_VALUE, m.getValue());
    }

    @Test
    public void save_long_primitive_negative() {
        PrimitiveLongModel model = new PrimitiveLongModel();
        model.setValue(-1234567890);
        new PrimitiveLongStore(appContext).save(model);
        PrimitiveLongModel m = new PrimitiveLongStore(appContext).load();
        assertEquals(-1234567890, m.getValue());
    }

    @Test
    public void save_long_boxed_default() {
        BoxedLongModel model = new BoxedLongModel();
        new BoxedLongStore(appContext).save(model);
        BoxedLongModel m = new BoxedLongStore(appContext).load();
        assertEquals(0, m.getValue().longValue());
    }

    @Test
    public void save_long_boxed_positive() {
        BoxedLongModel model = new BoxedLongModel();
        model.setValue(Long.MAX_VALUE);
        new BoxedLongStore(appContext).save(model);
        BoxedLongModel m = new BoxedLongStore(appContext).load();
        assertEquals(Long.MAX_VALUE, m.getValue().longValue());
    }

    @Test
    public void save_long_boxed_negative() {
        BoxedLongModel model = new BoxedLongModel();
        model.setValue(-1234567890L);
        new BoxedLongStore(appContext).save(model);
        BoxedLongModel m = new BoxedLongStore(appContext).load();
        assertEquals(-1234567890L, m.getValue().longValue());
    }

    @Test
    public void save_double_primitive_default() {
        PrimitiveDoubleModel model = new PrimitiveDoubleModel();
        new PrimitiveDoubleStore(appContext).save(model);
        PrimitiveDoubleModel m = new PrimitiveDoubleStore(appContext).load();
        assertEquals(0, m.getValue(), 0.0001);
    }

    @Test
    public void save_double_primitive_positive() {
        PrimitiveDoubleModel model = new PrimitiveDoubleModel();
        model.setValue(Double.MAX_VALUE);
        new PrimitiveDoubleStore(appContext).save(model);
        PrimitiveDoubleModel m = new PrimitiveDoubleStore(appContext).load();
        assertEquals(Double.MAX_VALUE, m.getValue(), 0.0001);
    }

    @Test
    public void save_double_primitive_negative() {
        PrimitiveDoubleModel model = new PrimitiveDoubleModel();
        model.setValue(-12345.67890);
        new PrimitiveDoubleStore(appContext).save(model);
        PrimitiveDoubleModel m = new PrimitiveDoubleStore(appContext).load();
        assertEquals(-12345.67890, m.getValue(), 0.0001);
    }

    @Test
    public void save_double_boxed_default() {
        BoxedDoubleModel model = new BoxedDoubleModel();
        new BoxedDoubleStore(appContext).save(model);
        BoxedDoubleModel m = new BoxedDoubleStore(appContext).load();
        assertEquals(0, m.getValue(), 0.0001);
    }

    @Test
    public void save_double_boxed_positive() {
        BoxedDoubleModel model = new BoxedDoubleModel();
        model.setValue(Double.MAX_VALUE);
        new BoxedDoubleStore(appContext).save(model);
        BoxedDoubleModel m = new BoxedDoubleStore(appContext).load();
        assertEquals(Double.MAX_VALUE, m.getValue(), 0.0001);
    }

    @Test
    public void save_double_boxed_negative() {
        BoxedDoubleModel model = new BoxedDoubleModel();
        model.setValue(-12345.67890);
        new BoxedDoubleStore(appContext).save(model);
        BoxedDoubleModel m = new BoxedDoubleStore(appContext).load();
        assertEquals(-12345.67890, m.getValue(), 0.0001);
    }

    @Test
    public void save_full_default() {
        FullModel model = new FullModel();
        new FullStore(appContext).save(model);
        FullModel m = new FullStore(appContext).load();
        assertNull(m.getStr());
    }

    @Test
    public void save_date_default() {
        DateModel model = new DateModel();
        new DateStore(appContext).save(model);
        DateModel m = new DateStore(appContext).load();
        assertNull(m.getValue());
    }

}