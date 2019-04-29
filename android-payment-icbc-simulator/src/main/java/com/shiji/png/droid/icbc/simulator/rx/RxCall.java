package com.shiji.png.droid.icbc.simulator.rx;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.shiji.png.droid.icbc.simulator.model.TransResponse;
import com.shiji.png.droid.icbc.simulator.model.TransResponseData;
import com.shiji.png.droid.icbc.simulator.parser.AuthParser;
import com.shiji.png.droid.icbc.simulator.parser.Parser;
import com.shiji.png.droid.icbc.simulator.parser.SaleParser;
import com.shiji.png.droid.icbc.simulator.parser.VoidParser;
import com.shiji.png.droid.icbc.simulator.ui.CardPaymentActivity;
import com.shiji.png.droid.icbc.simulator.ui.InfoCofirmActivity;
import com.shiji.png.droid.icbc.simulator.util.DeviceUtils;
import com.shiji.png.droid.icbc.simulator.util.EventsUtils;
import com.shiji.png.droid.icbc.simulator.util.RecentUtils;
import com.shiji.png.droid.payment.ApplicationHolder;
import com.shiji.png.payment.message.TxRequest;
import com.shiji.png.payment.message.TxResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author bruce.wu
 * @since 2018/11/22 15:21
 */
public final class RxCall {

    public static Observable<TxResponse> invoke(final TxRequest request, Parser parser) {
        return Observable.create(new RpcObservableSource())
                .doOnSubscribe(disposable -> {
                    Application app = ApplicationHolder.app();
                    if(!DeviceUtils.siScreenOn(app)) {
                        DeviceUtils.wakeUp(app);
                    }
                    Intent intent = new Intent(app, CardPaymentActivity.class);
                    intent.putExtra("AMT", request.getTotalAmt());
                    intent.putExtra("transType","SALE");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    app.startActivity(intent);
                })
                .subscribeOn(Schedulers.io())
                .map(new Function<TransResponse, TransResponseData>() {
                    @Override
                    public TransResponseData apply(TransResponse response) throws Exception {
                        return new TransResponseData(response);
                    }
                })
                .map(new Function<TransResponseData, TxResponse>() {
                    @Override
                    public TxResponse apply(TransResponseData data) throws Exception {
                        return parser.parse(request, data);
                    }
                });
    }

    public static Observable<TxResponse> cancel(final TxRequest request) {
        return Observable.create(new RpcObservableSource())
                .doOnSubscribe(disposable -> {
                    Application app = ApplicationHolder.app();
                    if(!DeviceUtils.siScreenOn(app)) {
                        DeviceUtils.wakeUp(app);
                    }
                    Intent intent = new Intent(app, InfoCofirmActivity.class);
                    intent.putExtra("AMT", request.getTxnAmt());
                    intent.putExtra("transType","VOID SALE");
                    intent.putExtra("orgRefNo",request.getOrgRefNo());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    app.startActivity(intent);
                })
                .subscribeOn(Schedulers.io())
                .map(new Function<TransResponse, TransResponseData>() {
                    @Override
                    public TransResponseData apply(TransResponse response) throws Exception {
                        return new TransResponseData(response);
                    }
                })
                .map(new Function<TransResponseData, TxResponse>() {
                    @Override
                    public TxResponse apply(TransResponseData data) throws Exception {
                        return new VoidParser().parse(request, data);
                    }
                });
    }

    private static TransResponseData findResponseDataByRefNo(String orgRefNo) {
        return new TransResponseData(new TransResponse());
    }

    public static Observable<TxResponse> refund(final TxRequest request) {
        return Observable.create(new RpcObservableSource())
                .doOnSubscribe(disposable -> {
                    Application app = ApplicationHolder.app();
                    if(!DeviceUtils.siScreenOn(app)) {
                        DeviceUtils.wakeUp(app);
                    }
                    Intent intent = new Intent(app, InfoCofirmActivity.class);
                    intent.putExtra("AMT", request.getTxnAmt());
                    intent.putExtra("transType","REFUND");
                    intent.putExtra("pan","6214832107541232");
                    intent.putExtra("orgRefNo",request.getOrgRefNo());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    app.startActivity(intent);
                })
                .subscribeOn(Schedulers.io())
                .map(new Function<TransResponse, TransResponseData>() {
                    @Override
                    public TransResponseData apply(TransResponse response) throws Exception {
                        return new TransResponseData(response);
                    }
                })
                .map(new Function<TransResponseData, TxResponse>() {
                    @Override
                    public TxResponse apply(TransResponseData data) throws Exception {
                        return new VoidParser().parse(request, data);
                    }
                });
    }

    public static Observable<TxResponse> auth(final TxRequest request) {
        return Observable.create(new RpcObservableSource())
                .doOnSubscribe(disposable -> {
                    Application app = ApplicationHolder.app();
                    if(!DeviceUtils.siScreenOn(app)) {
                        DeviceUtils.wakeUp(app);
                    }
                    Intent intent = new Intent(app, CardPaymentActivity.class);
                    intent.putExtra("AMT", request.getTotalAmt());
                    intent.putExtra("transType","AUTH");
                    intent.putExtra("CURRENCY CODE",request.getTxnCurrCode());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    app.startActivity(intent);
                })
                .subscribeOn(Schedulers.io())
                .map(new Function<TransResponse, TransResponseData>() {
                    @Override
                    public TransResponseData apply(TransResponse response) throws Exception {
                        return new TransResponseData(response);
                    }
                })
                .map(new Function<TransResponseData, TxResponse>() {
                    @Override
                    public TxResponse apply(TransResponseData data) throws Exception {
                        return new AuthParser().parse(request, data);
                    }
                });
    }

    public static Observable<TxResponse> voidAuth(final TxRequest request) {
        return Observable.create(new RpcObservableSource())
                .doOnSubscribe(disposable -> {
                    Application app = ApplicationHolder.app();
                    if(!DeviceUtils.siScreenOn(app)) {
                        DeviceUtils.wakeUp(app);
                    }
                    Intent intent = new Intent(app, InfoCofirmActivity.class);
                    intent.putExtra("AMT", request.getTxnAmt());
                    intent.putExtra("transType","VOID AUTH");
                    intent.putExtra("orgAuthNo",request.getOrgAuthCode());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    app.startActivity(intent);
                })
                .subscribeOn(Schedulers.io())
                .map(new Function<TransResponse, TransResponseData>() {
                    @Override
                    public TransResponseData apply(TransResponse response) throws Exception {
                        return new TransResponseData(response);
                    }
                })
                .map(new Function<TransResponseData, TxResponse>() {
                    @Override
                    public TxResponse apply(TransResponseData data) throws Exception {
                        return new VoidParser().parse(request, data);
                    }
                });
    }

    public static Observable<TxResponse> capture(final TxRequest request) {
        return Observable.create(new RpcObservableSource())
                .doOnSubscribe(disposable -> {
                    Application app = ApplicationHolder.app();
                    if(!DeviceUtils.siScreenOn(app)) {
                        DeviceUtils.wakeUp(app);
                    }
                    Intent intent = new Intent(app, InfoCofirmActivity.class);
                    intent.putExtra("AMT", request.getTotalAmt());
                    intent.putExtra("transType","CAPTURE");
                    intent.putExtra("pan","6214832107541232");
                    intent.putExtra("orgAuthNo",request.getOrgAuthCode());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    app.startActivity(intent);
                })
                .subscribeOn(Schedulers.io())
                .map(new Function<TransResponse, TransResponseData>() {
                    @Override
                    public TransResponseData apply(TransResponse response) throws Exception {
                        return new TransResponseData(response);
                    }
                })
                .map(new Function<TransResponseData, TxResponse>() {
                    @Override
                    public TxResponse apply(TransResponseData data) throws Exception {
                        return new AuthParser().parse(request, data);
                    }
                });
    }

    public static Observable<TxResponse> voidCapture(final TxRequest request) {
        return Observable.create(new RpcObservableSource())
                .doOnSubscribe(disposable -> {
                    Application app = ApplicationHolder.app();
                    if(!DeviceUtils.siScreenOn(app)) {
                        DeviceUtils.wakeUp(app);
                    }

                    Intent intent = new Intent(app, InfoCofirmActivity.class);
                    intent.putExtra("AMT", request.getTotalAmt());
                    intent.putExtra("transType","VOID CAPTURE");
                    intent.putExtra("pan","6214832107541232");
                    intent.putExtra("orgAuthNo",request.getOrgAuthCode());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    app.startActivity(intent);
                })
                .subscribeOn(Schedulers.io())
                .map(new Function<TransResponse, TransResponseData>() {
                    @Override
                    public TransResponseData apply(TransResponse response) throws Exception {
                        return new TransResponseData(response);
                    }
                })
                .map(new Function<TransResponseData, TxResponse>() {
                    @Override
                    public TxResponse apply(TransResponseData data) throws Exception {
                        return new AuthParser().parse(request, data);
                    }
                });
    }

    public static Observable<TxResponse> topUpAuth(final TxRequest request) {
        return Observable.create(new RpcObservableSource())
                .doOnSubscribe(disposable -> {
                    Application app = ApplicationHolder.app();
                    if(!DeviceUtils.siScreenOn(app)) {
                        DeviceUtils.wakeUp(app);
                    }
                    Intent intent = new Intent(app, InfoCofirmActivity.class);
                    intent.putExtra("AMT", request.getTotalAmt());
                    intent.putExtra("transType","TOP UP AUTH");
                    intent.putExtra("orgAuthNo",request.getOrgAuthCode());
                    intent.putExtra("orgRefNo",request.getOrgRefNo());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    app.startActivity(intent);
                })
                .subscribeOn(Schedulers.io())
                .map(new Function<TransResponse, TransResponseData>() {
                    @Override
                    public TransResponseData apply(TransResponse response) throws Exception {
                        return new TransResponseData(response);
                    }
                })
                .map(new Function<TransResponseData, TxResponse>() {
                    @Override
                    public TxResponse apply(TransResponseData data) throws Exception {
                        return new AuthParser().parse(request, data);
                    }
                });
    }

    public static Observable<TxResponse> adjustTips(final TxRequest request) {
        return Observable.create(new RpcObservableSource())
                .doOnSubscribe(disposable -> {
                    Application app = ApplicationHolder.app();
                    if(!DeviceUtils.siScreenOn(app)) {
                        DeviceUtils.wakeUp(app);
                    }
                    Intent intent = new Intent(app, InfoCofirmActivity.class);
                    intent.putExtra("tipAmt", request.getTipAmt());
                    intent.putExtra("transType","TIP ADJUSTMENT");
                    intent.putExtra("orgAuthNo",request.getOrgAuthCode());
                    intent.putExtra("orgRefNo",request.getOrgRefNo());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    app.startActivity(intent);
                })
                .subscribeOn(Schedulers.io())
                .map(new Function<TransResponse, TransResponseData>() {
                    @Override
                    public TransResponseData apply(TransResponse response) throws Exception {
                        return new TransResponseData(response);
                    }
                })
                .map(new Function<TransResponseData, TxResponse>() {
                    @Override
                    public TxResponse apply(TransResponseData data) throws Exception {
                        return new AuthParser().parse(request, data);
                    }
                });
    }

    public static Observable<TxResponse> adjustSales(final TxRequest request) {
        return Observable.create(new RpcObservableSource())
                .doOnSubscribe(disposable -> {
                    Application app = ApplicationHolder.app();
                    if(!DeviceUtils.siScreenOn(app)) {
                        DeviceUtils.wakeUp(app);
                    }
                    Intent intent = new Intent(app, InfoCofirmActivity.class);
                    intent.putExtra("AMT", request.getTxnAmt());
                    intent.putExtra("transType","ADJUSTMENT SALES");
                    intent.putExtra("orgAuthNo",request.getOrgAuthCode());
                    intent.putExtra("orgRefNo",request.getOrgRefNo());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    app.startActivity(intent);
                })
                .subscribeOn(Schedulers.io())
                .map(new Function<TransResponse, TransResponseData>() {
                    @Override
                    public TransResponseData apply(TransResponse response) throws Exception {
                        return new TransResponseData(response);
                    }
                })
                .map(new Function<TransResponseData, TxResponse>() {
                    @Override
                    public TxResponse apply(TransResponseData data) throws Exception {
                        return new AuthParser().parse(request, data);
                    }
                });
    }

    public static Observable<TxResponse> reversal(final TxRequest request) {
        return Observable.create(new RpcObservableSource())
                .doOnSubscribe(disposable -> {
                    EventsUtils.getInstance().response(getResponse());
                })
                .subscribeOn(Schedulers.io())
                .map(TransResponseData::new)
                .map(data -> new VoidParser().parse(request, data));
    }

    private static TransResponse getResponse() {
        TransResponse response = new TransResponse();
        response.setSn(10000000010L);

        String year = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());

        Bundle baseResult = new Bundle();
        baseResult.putLong("RESULT", 0);
        baseResult.putString("TRANS_TYPE", "MULTI_PURCHASE");
        baseResult.putString("DESCRIPTION", "交易成功");
        baseResult.putString("CUST_NAME", "测试商户");
        baseResult.putString("CUST_NO", "020000011000");
        baseResult.putString("RSP_NO", "00");
        baseResult.putString("TERM_NO", "020000001100000");
        baseResult.putString("EXT_ORDER_NO", year + "12345678");
        baseResult.putString("TRANS_TIME", "201903251314201");
        baseResult.putLong("TRANS_SEQUENCE", 18345L);

        Bundle transResult = new Bundle();
        transResult.putString("PAY_TYPE", "QRPURCHASE");
        transResult.putString("REF_NO", "1234567890");
        transResult.putString("AUTH_ID", "123456");
        transResult.putString("QRCODE_ORDER", "20190325" + "1234567890");
        transResult.putString("TRACE_NO", "123456");

        Bundle extraInfo = new Bundle();
        extraInfo.putString("CARD_ORG", "CUP");
        extraInfo.putString("CARD_ISSUER", "CUP");
        extraInfo.putLong("INPUT_MODE", 5L);
        extraInfo.putString("SHILDED_PAN", "622202******11111");
        extraInfo.putString("PAN", "62220200001111111");
        extraInfo.putLong("BANK_REDUCE_AMT", 0);
        extraInfo.putString("EXP_DATE", "2012");

        response.setBaseResult(baseResult);
        response.setTransResult(transResult);
        response.setExtraInfo(extraInfo);
        return response;
    }
}
