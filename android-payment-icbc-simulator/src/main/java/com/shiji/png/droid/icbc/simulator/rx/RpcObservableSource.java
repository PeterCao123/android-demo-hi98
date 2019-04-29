package com.shiji.png.droid.icbc.simulator.rx;

import com.shiji.png.droid.icbc.simulator.model.TransResponse;
import com.shiji.png.droid.icbc.simulator.util.EventsUtils;

import java.util.Random;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * @author bruce.wu
 * @since 2018/11/22 14:24
 */
class RpcObservableSource implements ObservableOnSubscribe<TransResponse> {
    @Override
    public void subscribe(ObservableEmitter<TransResponse> emitter) throws Exception {
        EventsUtils.getInstance().register(emitter);
        while (true) {
            if (EventsUtils.getInstance().waiting()) {
                Thread.sleep(100);
            } else {
                break;
            }
        }
        if (EventsUtils.getInstance().delivery()) {
            throw new RuntimeException("waiting for response timeout");
        }
//        Thread.sleep(6000);
//
//        TransResponse response = new TransResponse();
//        response.setSn(10000000010L);
//
//        String date = new SimpleDateFormat("yyyyMMddHHmmSS", Locale.getDefault())
//                .format(new Date(System.currentTimeMillis() + 3600000L));
//        String year = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
//
//        Bundle baseResult = new Bundle();
//        baseResult.putLong("RESULT", 0L);
//        baseResult.putString("TRANS_TYPE", "MULTI_PURCHASE");
//        baseResult.putString("DESCRIPTION", "交易成功");
//        baseResult.putString("CUST_NAME", "测试商户");
//        baseResult.putString("CUST_NO", "020000000000");
//        baseResult.putString("RSP_NO", "00");
//        baseResult.putString("TERM_NO", "020000000000000");
//        baseResult.putString("EXT_ORDER_NO", year + getRandom(8));
//        baseResult.putString("TRANS_TIME", date);
//        baseResult.putLong("TRANS_SEQUENCE", getNumber(1000));
//
//        Bundle transResult = new Bundle();
//        transResult.putString("PAY_TYPE", "QRPURCHASE");
//        transResult.putString("REF_NO", getRandom(10));
//        transResult.putString("AUTH_ID", getRandom(6));
//        transResult.putString("QRCODE_ORDER", date + getRandom(14));
//        transResult.putString("TRACE_NO", getRandom(6));
//
//        Bundle extraInfo = new Bundle();
//        extraInfo.putString("CARD_ORG", "CUP");
//        extraInfo.putString("SHILDED_PAN", "586912*********9567");
//        extraInfo.putString("PAN", "5869121010101009567");
//        extraInfo.putString("CARD_ISSUER", "微信支付-零钱");
//        extraInfo.putLong("BANK_REDUCE_AMT", 0);
//        extraInfo.putString("EXP_DATE", date);
//
//        response.setBaseResult(baseResult);
//        response.setTransResult(transResult);
//        response.setExtraInfo(extraInfo);
//
//        emitter.onNext(response);
//        emitter.onComplete();
    }

    private String getRandom(int length) {
        StringBuilder builder = new StringBuilder();
        Random rm = new Random();
        for (int index = 0; index < length; index++) {
            builder.append(rm.nextInt(10));
        }
        return builder.toString();
    }

    private long getNumber(int bound) {
        return new Random().nextInt(bound);
    }
}