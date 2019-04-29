package com.shiji.png.payment.rx;

/**
 * @author bruce.wu
 * @since 2018/11/15 14:05
 */
public interface IdentifiedSource<K, V> extends ObservableSource<V> {

    K id();

}
