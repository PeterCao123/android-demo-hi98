package com.shiji.png.pat.sp.util;

import android.content.res.Resources;

import com.shiji.png.pat.sp.annotation.Default;
import com.shiji.png.pat.sp.annotation.Key;
import com.shiji.png.pat.sp.annotation.Precision;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;

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
public class PreferenceUtilsTest {

    private Resources resources;

    @Before
    public void setUp() {
        resources = Mockito.mock(Resources.class);
        Mockito.when(resources.getString(1)).thenReturn("string-res");
        Mockito.when(resources.getString(2)).thenReturn("true");
        Mockito.when(resources.getString(3)).thenReturn("12345");
        Mockito.when(resources.getString(4)).thenReturn("123456789");
    }

    @Test
    public void getKey_fieldName() throws Exception {
        Field field = TestingKeyModel.class.getDeclaredField("fieldKey");
        assertEquals("fieldKey", PreferenceUtils.getKey(field, resources));
    }@Test
    public void getKey_nameKey() throws Exception {
        Field field = TestingKeyModel.class.getDeclaredField("nameKey");
        assertEquals("hello", PreferenceUtils.getKey(field, resources));
    }

    @Test
    public void getKey_resKey() throws Exception {
        Field field = TestingKeyModel.class.getDeclaredField("resKey");
        assertEquals("string-res", PreferenceUtils.getKey(field, resources));
    }

    @Test
    public void getDefaultString_noDef() throws Exception {
        Field field = DefaultStringModel.class.getDeclaredField("noDef");
        assertNull(PreferenceUtils.getDefaultString(field, resources, null));
        assertEquals("", PreferenceUtils.getDefaultString(field, resources, ""));
        assertEquals("hello", PreferenceUtils.getDefaultString(field, resources, "hello"));
    }

    @Test
    public void getDefaultString_defValue() throws Exception {
        Field field = DefaultStringModel.class.getDeclaredField("defValue");
        assertEquals("hello", PreferenceUtils.getDefaultString(field, resources, null));
    }

    @Test
    public void getDefaultString_defRes() throws Exception {
        Field field = DefaultStringModel.class.getDeclaredField("defRes");
        assertEquals("string-res", PreferenceUtils.getDefaultString(field, resources, null));
    }

    @Test
    public void getDefaultBoolean_noDef() throws Exception {
        Field field = DefaultBoolModel.class.getDeclaredField("noDef");
        assertFalse(PreferenceUtils.getDefaultBoolean(field, resources, false));
        assertTrue(PreferenceUtils.getDefaultBoolean(field, resources, true));
    }

    @Test
    public void getDefaultBoolean_defValue() throws Exception {
        Field field = DefaultBoolModel.class.getDeclaredField("defValue");
        assertTrue(PreferenceUtils.getDefaultBoolean(field, resources, false));
    }

    @Test
    public void getDefaultBoolean_defRes() throws Exception {
        Field field = DefaultBoolModel.class.getDeclaredField("defRes");
        assertTrue(PreferenceUtils.getDefaultBoolean(field, resources, false));
    }

    @Test
    public void getDefaultInt_noDef() throws Exception {
        Field field = DefaultIntModel.class.getDeclaredField("noDef");
        assertEquals(111, PreferenceUtils.getDefaultInt(field, resources, 111));
        assertEquals(222, PreferenceUtils.getDefaultInt(field, resources, 222));
    }

    @Test
    public void getDefaultInt_defValue() throws Exception {
        Field field = DefaultIntModel.class.getDeclaredField("defValue");
        assertEquals(54321, PreferenceUtils.getDefaultInt(field, resources, 111));
    }

    @Test
    public void getDefaultInt_defRes() throws Exception {
        Field field = DefaultIntModel.class.getDeclaredField("defRes");
        assertEquals(12345, PreferenceUtils.getDefaultInt(field, resources, 111));
    }

    @Test
    public void getDefaultLong_noDef() throws Exception {
        Field field = DefaultLongModel.class.getDeclaredField("noDef");
        assertEquals(111111111, PreferenceUtils.getDefaultLong(field, resources, 111111111));
        assertEquals(222222222, PreferenceUtils.getDefaultLong(field, resources, 222222222));
    }

    @Test
    public void getDefaultLong_defValue() throws Exception {
        Field field = DefaultLongModel.class.getDeclaredField("defValue");
        assertEquals(987654321, PreferenceUtils.getDefaultLong(field, resources, 111111111));
    }

    @Test
    public void getDefaultLong_defRes() throws Exception {
        Field field = DefaultLongModel.class.getDeclaredField("defRes");
        assertEquals(123456789, PreferenceUtils.getDefaultLong(field, resources, 111111111));
    }

    @Test
    public void getDoubleValue_noScale() throws Exception {
        Field field = DefaultDoubleModel.class.getDeclaredField("noScale");
        assertEquals(123.4567, PreferenceUtils.getDoubleValue(field, "123.4567"), 0.00001);
        assertEquals(0.0, PreferenceUtils.getDoubleValue(field, "0"), 0.00001);
    }

    @Test
    public void getDoubleValue_scaleDef() throws Exception {
        Field field = DefaultDoubleModel.class.getDeclaredField("scaleDef");
        assertEquals(123.46, PreferenceUtils.getDoubleValue(field, "123.4556"), 0.00001);
        assertEquals(123.45, PreferenceUtils.getDoubleValue(field, "123.4545"), 0.00001);
    }

    @Test
    public void getDoubleValue_scale3() throws Exception {
        Field field = DefaultDoubleModel.class.getDeclaredField("scale3");
        assertEquals(123.456, PreferenceUtils.getDoubleValue(field, "123.4555"), 0.00001);
        assertEquals(123.454, PreferenceUtils.getDoubleValue(field, "123.4544"), 0.00001);
    }

    @Test
    public void getDoubleValue_floor() throws Exception {
        Field field = DefaultDoubleModel.class.getDeclaredField("floor");
        assertEquals(123.45, PreferenceUtils.getDoubleValue(field, "123.4555"), 0.00001);
        assertEquals(123.45, PreferenceUtils.getDoubleValue(field, "123.4544"), 0.00001);
    }

    @Test
    public void getBigDecimalValue_noScale() throws Exception {
        Field field = DefaultBigDecimalModel.class.getDeclaredField("noScale");
        assertEquals(BigDecimal.valueOf(123.4567), PreferenceUtils.getBigDecimalValue(field, "123.4567"));
        assertEquals(BigDecimal.valueOf(0), PreferenceUtils.getBigDecimalValue(field, "0"));
    }

    @Test
    public void getBigDecimalValue_scaleDef() throws Exception {
        Field field = DefaultBigDecimalModel.class.getDeclaredField("scaleDef");
        assertEquals(BigDecimal.valueOf(123.46), PreferenceUtils.getBigDecimalValue(field, "123.4556"));
        assertEquals(BigDecimal.valueOf(123.45), PreferenceUtils.getBigDecimalValue(field, "123.4545"));
    }

    @Test
    public void getBigDecimalValue_scale3() throws Exception {
        Field field = DefaultBigDecimalModel.class.getDeclaredField("scale3");
        assertEquals(BigDecimal.valueOf(123.456), PreferenceUtils.getBigDecimalValue(field, "123.4555"));
        assertEquals(BigDecimal.valueOf(123.454), PreferenceUtils.getBigDecimalValue(field, "123.4544"));
    }

    @Test
    public void getBigDecimalValue_floor() throws Exception {
        Field field = DefaultBigDecimalModel.class.getDeclaredField("floor");
        assertEquals(BigDecimal.valueOf(123.45), PreferenceUtils.getBigDecimalValue(field, "123.4555"));
        assertEquals(BigDecimal.valueOf(123.45), PreferenceUtils.getBigDecimalValue(field, "123.4544"));
    }

    static class TestingKeyModel {

        private String fieldKey;

        @Key(name = "hello")
        private String nameKey;

        @Key(resId = 1)
        private String resKey;

    }

    static class DefaultStringModel {

        String noDef;

        @Default("hello")
        String defValue;

        @Default(resId = 1)
        String defRes;

    }

    static class DefaultBoolModel {

        boolean noDef;

        @Default("true")
        boolean defValue;

        @Default(resId = 2)
        boolean defRes;

    }

    static class DefaultIntModel {

        int noDef;

        @Default("54321")
        int defValue;

        @Default(resId = 3)
        int defRes;

    }

    static class DefaultLongModel {

        long noDef;

        @Default("987654321")
        long defValue;

        @Default(resId = 4)
        long defRes;

    }

    static class DefaultDoubleModel {

        double noScale;

        @Precision
        double scaleDef;

        @Precision(scale = 3)
        double scale3;

        @Precision(roundingMode = RoundingMode.FLOOR)
        double floor;

    }

    static class DefaultBigDecimalModel {

        BigDecimal noScale;

        @Precision
        BigDecimal scaleDef;

        @Precision(scale = 3)
        BigDecimal scale3;

        @Precision(roundingMode = RoundingMode.FLOOR)
        BigDecimal floor;

    }

}