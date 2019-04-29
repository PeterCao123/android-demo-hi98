package com.shiji.png.payment.rx;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author bruce.wu
 * @since 2018/11/19 13:10
 */
public class ObservableRegistry<K, V> extends ConcurrentHashMap<K, V> implements Registry<K, V> {
    @Override
    public void register(K key, V value) {
        if (containsKey(key)) {
            throw new IllegalStateException("duplicated: " + key);
        }
        this.put(key, value);
    }

    @Override
    public void unregister(K key) {
        this.remove(key);
    }

    @Override
    public V find(Object key) {
        if (!containsKey(key)) {
            throw new IllegalStateException("not exist: " + key);
        }
        return get(key);
    }
}
