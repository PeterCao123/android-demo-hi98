package com.shiji.png.pat.app.storage;

/**
 * @author bruce.wu
 * @since 2019/2/13 16:03
 */
public interface PreferenceStorage<T> {

    boolean isPersistence();

    void save(T model);

    T load();

    void clear();

}
