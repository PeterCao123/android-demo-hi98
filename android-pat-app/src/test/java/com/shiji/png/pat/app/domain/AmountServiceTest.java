package com.shiji.png.pat.app.domain;

import com.shiji.png.pat.app.domain.service.AmountService;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author bruce.wu
 * @since 2018/12/12 18:12
 */
public class AmountServiceTest {

    @Test
    public void testDoubleCompare() {
        double amount = AmountService.toAmount("140.00").doubleValue();
        double totalAmount = 271.4 - 131.4;
        assertFalse(Double.compare(amount, AmountService.toAmount(totalAmount).doubleValue()) > 0);
    }

    @Test
    public void testAmountCompare() {
        assertEquals(0, AmountService.compare(140.00, 271.4-131.4));
        assertEquals(0, AmountService.compare(1.0000, 1.0001));
        assertEquals(0, AmountService.compare(1.0000, 0.9999));
        assertEquals(-1, AmountService.compare(1.0000, 1.0100));
        assertEquals(1, AmountService.compare(1.0000, 0.9900));

        BigDecimal left = new BigDecimal("1.0000");
        assertEquals(0, AmountService.compare(left, 1.0001));
        assertEquals(0, AmountService.compare(left, 0.9999));
        assertEquals(-1, AmountService.compare(left, 1.0100));
        assertEquals(1, AmountService.compare(left, 0.9900));
    }

    @Test
    public void compareBigDecimal() {
        BigDecimal a1 = AmountService.toAmount((String)null);
        assertEquals(BigDecimal.valueOf(0), a1);
        BigDecimal a2 = AmountService.toAmount((Double) null);
        assertEquals(BigDecimal.valueOf(0), a2);

        assertEquals(new BigDecimal("1.00"), AmountService.toAmount(1.000001));
        assertEquals(new BigDecimal("1.00"), AmountService.toAmount(0.999999));
    }

}