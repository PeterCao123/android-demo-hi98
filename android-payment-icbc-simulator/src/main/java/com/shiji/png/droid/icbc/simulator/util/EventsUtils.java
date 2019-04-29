package com.shiji.png.droid.icbc.simulator.util;

import com.shiji.png.droid.icbc.simulator.model.TransResponse;

import io.reactivex.ObservableEmitter;

/**
 * @author Falcon.cao
 * @date 2019/1/10
 */
public class EventsUtils {
    public static EventsUtils getInstance() {
        if (utils == null) {
            synchronized (EventsUtils.class) {
                if (utils == null) {
                    utils = new EventsUtils();
                }
            }
        }
        return utils;
    }

    private static EventsUtils utils = null;
    private ObservableEmitter<TransResponse> emitter;
    private TransResponse response;
    private int results = -1;

    public void register(ObservableEmitter<TransResponse> oe) {
        emitter = oe;
    }

    public void response(TransResponse tr) {
        response = tr;
        results = (tr == null) ? 1 : -1;
    }


    public boolean delivery() {
        if (response != null && emitter != null) {
            emitter.onNext(response);
            emitter.onComplete();
            emitter = null;
            response = null;
            results = -1;
            return false;
        }
        return true;
    }

    public boolean waiting() {
        return response == null && results == -1;
    }
}
