package com.shiji.png.payment.util;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * @author bruce.wu
 * @since 2018/12/13 14:23
 */
public class JsonTest {

    @Test
    public void json_encoder() {
        TestModel m = new TestModel();
        m.value = new BigDecimal("123.45");
        String jsonStr = Json.getEncoder().encode(m);
        assertEquals("{\"value\":123.45}", jsonStr);
    }

    @Test
    public void json_decoder() {
        String jsonStr = "{\"value\":123.45}";
        TestModel m = Json.getDecoder().decode(jsonStr, TestModel.class);
        assertEquals("123.45", m.value.toString());
    }

    private static class TestModel {

        private BigDecimal value;

    }

}