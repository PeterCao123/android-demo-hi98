package com.shiji.png.pat.app.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author bruce.wu
 * @since 2018/12/12 18:49
 */
public class NumberUtilsTest {

    @Test
    public void compareDouble() {
        assertEquals(0, NumberUtils.compare(1.0, 1.0001));
        assertEquals(0, NumberUtils.compare(1.0, 0.9999));
        assertTrue(NumberUtils.compare(1.0, 1.01) < 0);
        assertTrue(NumberUtils.compare(1.0, 0.99) > 0);
    }

}