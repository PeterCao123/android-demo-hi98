package com.shiji.png.payment;

import io.reactivex.Observable;

/**
 * @author bruce.wu
 * @since 2018/12/5 15:55
 */
public interface ServiceScanner {

    Observable<Class<?>> scan();

}
