package com.shiji.png.payment.rx;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;

/**
 * @author bruce.wu
 * @since 2018/11/19 13:00
 */
public class IdentifiedObservable<K, V> extends Observable<V> {

    public static <K, V> Observable<V> create(IdentifiedSource<K, V> source, Registry<K, Emitter<V>> registry) {
        return create(source.id(), source, registry);
    }

    public static <K, V> Observable<V> create(K id, ObservableSource<V> source, Registry<K, Emitter<V>> registry) {
        if (source == null) {
            throw new NullPointerException("source is null");
        }
        if (registry == null) {
            throw new NullPointerException("registry is null");
        }
        if (id == null) {
            throw new NullPointerException("id is null");
        }
        return new IdentifiedObservable<>(id, source, registry);
    }

    private final K id;
    private final ObservableSource<V> source;
    private final Registry<K, Emitter<V>> registry;

    private IdentifiedObservable(K id, ObservableSource<V> source, Registry<K, Emitter<V>> registry) {
        this.id = id;
        this.source = source;
        this.registry = registry;
    }

    @Override
    protected void subscribeActual(Observer<? super V> observer) {
        IdentifiedEmitter<K, V> parent = new IdentifiedEmitter<>(this.id, observer, registry);
        observer.onSubscribe(parent);
        try {
            source.subscribe(parent);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            parent.onError(ex);
        }
    }

    static class IdentifiedEmitter<K, V> implements Emitter<V>, Disposable {

        private final K id;
        private final Observer<? super V> observer;
        private final Registry<K, Emitter<V>> registry;

        private boolean disposed;

        IdentifiedEmitter(K id, Observer<? super V> observer, Registry<K, Emitter<V>> registry) {
            this.id = id;
            this.observer = observer;
            this.registry = registry;

            register();
        }

        private void register() {
            this.registry.register(this.id, this);
        }

        private void unregister() {
            this.registry.unregister(this.id);
        }

        @Override
        public void onNext(V value) {
            if (value == null) {
                onError(new NullPointerException("onNext called with null"));
                return;
            }
            observer.onNext(value);
        }

        @Override
        public void onError(Throwable error) {
            if (error == null) {
                error = new NullPointerException("onError called with null");
            }
            try {
                observer.onError(error);
            } finally {
                unregister();
            }
        }

        @Override
        public void onComplete() {
            try {
                observer.onComplete();
            } finally {
                unregister();
            }
        }

        @Override
        public void dispose() {
            disposed = true;
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }
    }

}
