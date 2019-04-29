package com.shiji.png.pat.app.util;

/**
 * @author bruce.wu
 * @since 2018/12/12 18:45
 */
@Deprecated
public final class NumberUtils {

    private static final double DEFAULT_DELTA = 0.001;

    public static int compare(double left, double right) {
        return compare(left, right, DEFAULT_DELTA);
    }

    public static int compare(double left, double right, double delta) {
        int r = Double.compare(left, right);
        if (r == 0) {
            return 0;
        }
        if (Math.abs(left - right) < delta) {
            return 0;
        }
        return r;
    }

}
