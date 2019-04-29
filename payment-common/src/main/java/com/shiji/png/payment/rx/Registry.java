package com.shiji.png.payment.rx;

/**
 * @author bruce.wu
 * @since 2018/11/19 13:08
 */
public interface Registry<K, V> {

    void register(K key, V value);

    void unregister(K key);

    V find(K key);

    int size();

}
