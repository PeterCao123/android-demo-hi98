package com.shiji.png.payment.rx;

import io.reactivex.Emitter;
import io.reactivex.annotations.NonNull;

/**
 * @author bruce.wu
 * @since 2018/11/15 14:03
 */
public interface ObservableSource<T> {

    void subscribe(@NonNull Emitter<T> emitter) throws Exception;

}
