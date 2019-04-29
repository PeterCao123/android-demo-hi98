package com.shiji.png.droid.payment;

import android.content.Context;

import com.shiji.png.droid.payment.ui.SelectServiceActivity;
import com.shiji.png.payment.ServiceInfo;
import com.shiji.png.payment.ServiceSelector;
import com.shiji.png.payment.rx.RxServiceObservable;

import java.util.UUID;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author bruce.wu
 * @since 2018/12/5 16:48
 */
class AndroidServiceSelector implements ServiceSelector {

    private final Context context;

    AndroidServiceSelector(Context context) {
        this.context = context;
    }

    @Override
    public Observable<ServiceInfo> select() {
        return select(null);
    }

    @Override
    public Observable<ServiceInfo> select(String amount) {
        final String id = UUID.randomUUID().toString();
        return RxServiceObservable.create(id, (Emitter<ServiceInfo> emitter) -> SelectServiceActivity.start(context, id, amount))
                .subscribeOn(AndroidSchedulers.mainThread());
    }
}
