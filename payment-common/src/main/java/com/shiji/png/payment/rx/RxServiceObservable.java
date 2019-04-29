package com.shiji.png.payment.rx;

import com.shiji.png.payment.ServiceInfo;

import io.reactivex.Emitter;
import io.reactivex.Observable;

/**
 * @author bruce.wu
 * @since 2018/11/19 15:03
 */
public final class RxServiceObservable {

    private static class RxServiceRegistry {
        private static final Registry<String, Emitter<ServiceInfo>> INSTANCE = new ObservableRegistry<>();
    }

    public static Registry<String, Emitter<ServiceInfo>> getRegistry() {
        return RxServiceRegistry.INSTANCE;
    }

    public static Observable<ServiceInfo> create(String id, ObservableSource<ServiceInfo> source) {
        return IdentifiedObservable.create(id, source, RxServiceRegistry.INSTANCE);
    }

}
