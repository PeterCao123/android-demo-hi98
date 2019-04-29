package com.shiji.png.droid.icbc.simulator;

import com.shiji.png.droid.icbc.simulator.parser.SaleParser;
import com.shiji.png.droid.icbc.simulator.rx.RxCall;
import com.shiji.png.payment.PaymentService;
import com.shiji.png.payment.annotation.ServiceDef;
import com.shiji.png.payment.annotation.ServiceType;
import com.shiji.png.payment.message.TxRequest;
import com.shiji.png.payment.message.TxResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Observable;

/**
 * @author bruce.wu
 * @since 2018/11/22 14:02
 */
@ServiceDef(
        name = "icbc-simulator",
        resource = CardServiceResource.class,
        type = ServiceType.BANK_CARD,
        factory = CardServiceFactory.class
)
public class CardPaymentService implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger("CardPaymentService");

    @Override
    public Observable<TxResponse> sale(TxRequest request) {
        logger.trace("sale: {}", request);
        return RxCall.invoke(request, new SaleParser());
    }

    @Override
    public Observable<TxResponse> voidSale(TxRequest request) {
        return RxCall.cancel(request);
    }

    @Override
    public Observable<TxResponse> refund(TxRequest request) {
        return RxCall.refund(request);
    }

    @Override
    public Observable<TxResponse> auth(TxRequest request) {
        return RxCall.auth(request);
    }

    @Override
    public Observable<TxResponse> authCompletion(TxRequest request) {
        return RxCall.capture(request);
    }

    @Override
    public Observable<TxResponse> voidAuth(TxRequest request) {
        return RxCall.voidAuth(request);
    }

    @Override
    public Observable<TxResponse> topUpAuth(TxRequest request) {
        return RxCall.topUpAuth(request);
    }

    @Override
    public Observable<TxResponse> voidAuthCompletion(TxRequest request) {
        return RxCall.voidCapture(request);
    }


    @Override
    public Observable<TxResponse> adjustTips(TxRequest request) {
        return RxCall.adjustTips(request);
    }

    @Override
    public Observable<TxResponse> adjustSales(TxRequest request) {
        return RxCall.adjustSales(request);
    }

    @Override
    public Observable<TxResponse> reversal(TxRequest request) {
        return RxCall.reversal(request);
    }

}
