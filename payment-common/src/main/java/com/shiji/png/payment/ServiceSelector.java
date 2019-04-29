package com.shiji.png.payment;

import io.reactivex.Observable;

/**
 * @author bruce.wu
 * @since 2018/12/5 16:08
 */
public interface ServiceSelector {

    Observable<ServiceInfo> select();

    Observable<ServiceInfo> select(String amount);

}
