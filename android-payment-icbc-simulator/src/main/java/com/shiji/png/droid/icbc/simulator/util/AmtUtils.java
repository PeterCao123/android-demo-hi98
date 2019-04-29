package com.shiji.png.droid.icbc.simulator.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author bruce.wu
 * @date 2018/10/23
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

    public static long y2c(double amount) {
        return toBigDecimal(amount * 100).longValue();
    }

    public static BigDecimal c2y(Long amount) {
        if (amount == null) {
            return null;
        }
        return toBigDecimal(amount).divide(new BigDecimal(100), RoundingMode.HALF_UP);
    }

    public static String c2yString(Long amount) {
        if (amount == null) {
            return null;
        }
        return c2y(amount).toString();
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
