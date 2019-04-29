package com.shiji.png.payment;

import com.shiji.png.payment.message.TxRequest;
import com.shiji.png.payment.message.TxResponse;

import io.reactivex.Observable;

/**
 * @author bruce.wu
 * @since 2018/11/15 11:14
 */
public interface PaymentService {

    default Observable<TxResponse> signIn(TxRequest request) {
        return notImplementation(request);
    }

    default Observable<TxResponse> sale(TxRequest request) {
        return notImplementation(request);
    }

    default Observable<TxResponse> adjustTips(TxRequest request) {
        return notImplementation(request);
    }

    default Observable<TxResponse> adjustSales(TxRequest request) {
        return notImplementation(request);
    }

    default Observable<TxResponse> voidSale(TxRequest request) {
        return notImplementation(request);
    }

    default Observable<TxResponse> refund(TxRequest request) {
        return notImplementation(request);
    }

    default Observable<TxResponse> reversal(TxRequest request) {
        return notImplementation(request);
    }

    default Observable<TxResponse> auth(TxRequest request) {
        return notImplementation(request);
    }

    default Observable<TxResponse> topUpAuth(TxRequest request) {
        return notImplementation(request);
    }

    default Observable<TxResponse> voidAuth(TxRequest request) {
        return notImplementation(request);
    }

    default Observable<TxResponse> authCompletion(TxRequest request) {
        return notImplementation(request);
    }

    default Observable<TxResponse> voidAuthCompletion(TxRequest request) {
        return notImplementation(request);
    }

    default Observable<TxResponse> cashOut(TxRequest request) {
        return notImplementation(request);
    }

    default Observable<TxResponse> settle(TxRequest request) {
        return notImplementation(request);
    }

    default Observable<TxResponse> inquiry(TxRequest request) {
        return notImplementation(request);
    }

    default Observable<TxResponse> printLast(TxRequest request) {
        return notImplementation(request);
    }

    static Observable<TxResponse> notImplementation(TxRequest request) {
        return Observable.error(new NotImplementedException("Not implementation"));
    }

}
