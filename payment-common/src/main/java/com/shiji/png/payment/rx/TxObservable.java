package com.shiji.png.payment.rx;

import com.shiji.png.payment.message.TxResponse;

import io.reactivex.Emitter;
import io.reactivex.Observable;

/**
 * @author bruce.wu
 * @since 2018/11/15 14:04
 */
public final class TxObservable {

    private static class TxRegistry {
        private static final Registry<Long, Emitter<TxResponse>> INSTANCE = new ObservableRegistry<>();
    }

    public static Registry<Long, Emitter<TxResponse>> getRegistry() {
        return TxRegistry.INSTANCE;
    }

    public static Observable<TxResponse> create(IdentifiedSource<Long, TxResponse> source) {
        return IdentifiedObservable.create(source, TxRegistry.INSTANCE);
    }

    public static Observable<TxResponse> create(Long id, ObservableSource<TxResponse> source) {
        return IdentifiedObservable.create(id, source, TxRegistry.INSTANCE);
    }

}
