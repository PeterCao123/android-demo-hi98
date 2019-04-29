package com.shiji.png.payment.util;

/**
 * @author bruce.wu
 * @since 2018/11/14 16:06
 */
public final class TxId {

    private static class Creator {
        private static IdCounter idCounter = new IdCounter();
    }

    public static long next() {
        return Creator.idCounter.next();
    }

}
