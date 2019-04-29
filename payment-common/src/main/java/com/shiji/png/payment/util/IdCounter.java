package com.shiji.png.payment.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author bruce.wu
 * @since 2018/11/14 14:42
 */
public class IdCounter {

    private final AtomicLong counter = new AtomicLong();

    public long next() {
        return counter.getAndIncrement() & 0x7FFFFFFFFFFFFFFFL;
    }

}
