package com.shiji.png.pat.sp;

/**
 * @author bruce.wu
 * @date 2018/8/1
 */
public interface PreferenceStorage<T> {

    boolean isPersistence();

    void save(T model);

    T load();

    void clear();

}
