package com.shiji.png.payment.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author bruce.wu
 * @since 2018/11/15 15:31
 */
public final class AmtUtils {

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    public static BigDecimal toBigDecimal(double amount) {
        return new BigDecimal(amount).setScale(SCALE, ROUNDING_MODE);
    }

    public static BigDecimal toBigDecimal(String amount) {
        return new BigDecimal(amount).setScale(SCALE, ROUNDING_MODE);
    }

    public static double toAmount(String text) {
        return toBigDecimal(text).doubleValue();
    }

    public static double toAmount(double val) {
        return toBigDecimal(val).doubleValue();
    }

    public static String toString(Double amt) {
        if (amt == null) {
            return null;
        }
        return Double.toString(amt);
    }

}
