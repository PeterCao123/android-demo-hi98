package com.shiji.png.pat.app.domain.service;

import com.shiji.png.payment.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author bruce.wu
 * @since 2018/9/4
 */
public class AmountService {

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

    public static BigDecimal toAmount(double amount) {
        return BigDecimal.valueOf(amount).setScale(SCALE, ROUNDING_MODE);
    }

    public static BigDecimal toAmount(Double amount) {
        if (amount == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(amount).setScale(SCALE, ROUNDING_MODE);
    }

    public static BigDecimal toAmount(String amount) {
        if (StringUtils.isEmpty(amount)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(amount).setScale(SCALE, ROUNDING_MODE);
    }

    public static BigDecimal toAmount(BigDecimal amount) {
        if (amount == null) {
            return BigDecimal.ZERO;
        }
        return amount.setScale(SCALE, ROUNDING_MODE);
    }

    public static BigDecimal sum(BigDecimal...values) {
        if (values == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal result = BigDecimal.ZERO;
        for (BigDecimal value : values) {
            if (value != null) {
                result = result.add(value);
            }
        }
        return result.setScale(SCALE, ROUNDING_MODE);
    }

    public static int compare(double left, double right) {
        return toAmount(left).compareTo(toAmount(right));
    }

    public static int compare(Double left, Double right) {
        return toAmount(left).compareTo(toAmount(right));
    }

    public static int compare(String left, String right) {
        return toAmount(left).compareTo(toAmount(right));
    }

    public static int compare(BigDecimal left, BigDecimal right) {
        return left.compareTo(right);
    }

    public static int compare1(BigDecimal left, BigDecimal right) {
        return toAmount(left).compareTo(toAmount(right));
    }

    public static int compare(BigDecimal left, double right) {
        return toAmount(left).compareTo(toAmount(right));
    }

    public static int compare(double left, BigDecimal right) {
        return toAmount(left).compareTo(toAmount(right));
    }

}
