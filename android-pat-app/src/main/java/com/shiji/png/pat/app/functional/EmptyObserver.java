package com.shiji.png.pat.app.functional;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author bruce.wu
 * @date 2018/9/11
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
