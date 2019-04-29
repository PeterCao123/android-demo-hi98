package com.shiji.png.payment.rx;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author bruce.wu
 * @since 2018/11/15 9:25
 */
public class EmptyObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
