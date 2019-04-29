package com.shiji.png.droid.icbc.simulator.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.shiji.png.droid.icbc.simulator.R;
import com.shiji.png.droid.icbc.simulator.model.Converter;
import com.shiji.png.droid.icbc.simulator.model.ReceiptBean;
import com.shiji.png.droid.icbc.simulator.model.TransResponse;
import com.shiji.png.droid.icbc.simulator.model.TransResponseData;
import com.shiji.png.droid.icbc.simulator.ui.dialog.DialogService;
import com.shiji.png.droid.icbc.simulator.ui.dialog.ProcessDialogService;
import com.shiji.png.droid.icbc.simulator.util.DeviceUtils;
import com.shiji.png.droid.icbc.simulator.util.EventsUtils;
import com.shiji.png.droid.icbc.simulator.util.RecentUtils;
import com.shiji.png.droid.icbc.simulator.util.StringUtils;
import com.shiji.png.payment.model.IssuerCode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Peter.Cao
 * @date 2019/4/27
 */
public class InfoCofirmActivity extends AppCompatActivity {
    private ProcessDialogService processDialogService;
    private IssuerCode issuerCode;
    private double amount;
    private double tipAmt;
    private String transType;
    private String type;
    private String pan;
    private int cardType;
    private String orgRefNo;
    private String orgAuthNo;
    private DialogService dialogService;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_info_payment);
        orgRefNo = getIntent().getStringExtra("orgRefNo");
        orgAuthNo = getIntent().getStringExtra("orgAuthNo");
        amount = getIntent().getDoubleExtra("AMT", 0D);
        tipAmt = getIntent().getDoubleExtra("tipAmt", 0D);
        transType = getIntent().getStringExtra("transType");
        TransResponseData transResponseData = RecentUtils.getUtils(this).getResponseData(orgRefNo);
        if (transType.equals("VOID AUTH") || transType.equals("CAPTURE") || transType.equals("VOID CAPTURE")) {
            transResponseData = RecentUtils.getUtils(this).getResponseDataByAuthNo(orgAuthNo);
        }

        if (transResponseData != null) {
            orgAuthNo = transResponseData.getAuthId();
            pan = transResponseData.getPan();
            type = transResponseData.getType();
            cardType = isBankCard(pan);
            if (transType.equals("TIP ADJUSTMENT") || transType.equals("ADJUSTMENT SALES")) {
                if (!StringUtils.isNullOrEmpty("" + transResponseData.getAmount())) {
//                     amount = transResponseData.getAmount();
                }
                amount = tipAmt + amount;
            }
        } else {
            Observable.just("Demo-hi98")
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .doOnNext(d -> dialogService = new DialogService(InfoCofirmActivity.this))
                    .doOnNext(d -> dialogService.alert("Trans doesn't exist"))
                    .delay(2, TimeUnit.SECONDS)
                    .doOnNext(d -> dialogService.cancellDialog())
                    .doOnNext(d -> onDeclared("银行卡", "CUP", 1, ""))
                    .subscribe();
            return;
        }
        pan = StringUtils.onGenerateShilds(pan);
        TextView transTypeTv = findViewById(R.id.transtype_no_tv);
        transTypeTv.setText(transType);
        TextView orgRefTv = findViewById(R.id.reference_no);
        TextView orgRefNoTv = findViewById(R.id.reference_no_tv);
        orgRefNoTv.setText(orgRefNo);
        if (transType.equals("VOID AUTH") || transType.equals("CAPTURE") || transType.equals("VOID CAPTURE")) {
            orgRefTv.setText("Auth No.");
            orgRefNoTv.setText(orgAuthNo);
        }

        TextView amtTv = findViewById(R.id.amount_tv);
        amtTv.setText(amount + "");
        TextView panTv = findViewById(R.id.card_no_tv);
        TextView card_no = findViewById(R.id.card_no);
        if (cardType != 3) {
            card_no.setText("Barcode No.");
        }
        panTv.setText(pan);
        findViewById(R.id.cancel).setOnClickListener(v -> onFinish("cancel"));
        findViewById(R.id.confirm).setOnClickListener(v -> onFinish("confirm"));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        DeviceUtils.wakeUp(this);
        if (resultCode == RESULT_OK && dataIntent != null) {
            String cardNo = dataIntent.getStringExtra("PAN");
            String type = ReceiptBean.builder()
                    .entryMode("S")
                    .pan(cardNo)
                    .build().getCardType();
            issuerCode = Converter.getIssuerCode(type);
//            if(cardNo.equals(pan)) {
            Observable.just("Demo-hi98")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(d -> {
                        processDialogService = new ProcessDialogService(this, "");
                    })
                    .doOnNext(
                            d -> processDialogService.show()
                    )
                    .delay(2, TimeUnit.SECONDS)
                    .doOnNext(d -> processDialogService.hide())
                    .doOnNext(d -> onApproved(dataIntent.getLongExtra("ENTRY", 1), cardNo))
                    .subscribe();
//            }else{
//                Observable.just("Demo-hi98")
//                        .subscribeOn(AndroidSchedulers.mainThread())
//                        .doOnNext(d->dialogService = new DialogService(InfoCofirmActivity.this))
//                        .doOnNext(d->dialogService.alert(getString(R.string.card_err)))
//                        .delay(2,TimeUnit.SECONDS)
//                        .doOnNext(d->dialogService.cancellDialog())
//                        .doOnNext(d->onDeclared("银行卡", "CUP", 1, ""))
//                        .subscribe();
//            }
        } else {
            Observable.just("Demo-hi98")
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .doOnNext(d -> dialogService = new DialogService(InfoCofirmActivity.this))
                    .doOnNext(d -> dialogService.alert(getString(R.string.card_err)))
                    .delay(2, TimeUnit.SECONDS)
                    .doOnNext(d -> dialogService.cancellDialog())
                    .doOnNext(d -> onDeclared("银行卡", "CUP", 1, ""))
                    .subscribe();
        }
        super.onActivityResult(requestCode, resultCode, dataIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void onFinish(String type) {
        String cardTypes = ReceiptBean.builder()
                .entryMode("S")
                .pan(pan)
                .build().getCardType();
        issuerCode = Converter.getIssuerCode(cardTypes);
        if (type.equals("cancel")) {
            onDeclared("cancel", "cancel", 1, "");
            return;
        } else {
            if (transType.equals("REFUND") || transType.equals("TIP ADJUSTMENT") || transType.equals("ADJUSTMENT SALES")) {
                if (cardType == 3) {
                    Intent intent = new Intent(this, BankCardsActivity.class);
                    intent.putExtra("AMT", amount);
                    startActivityForResult(intent, 10);
                    return;
                }
            }
            Observable.just("Demo-hi98")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(d -> {
                        processDialogService = new ProcessDialogService(this, "");
                    })
                    .doOnNext(
                            d -> processDialogService.show()
                    )
                    .delay(2, TimeUnit.SECONDS)
                    .doOnNext(d -> processDialogService.hide())
                    .doOnNext(d -> onApproved(1, pan))
                    .subscribe();
            return;
        }
    }

    private int isBankCard(String cardNo) {
        String header = cardNo.substring(0, 2);
        switch (header) {
            case "10":
            case "11":
            case "12":
            case "13":
            case "14":
            case "15":
                return 1;
            case "25":
            case "26":
            case "27":
            case "28":
            case "29":
            case "30":
                return 2;
            default:
                return 3;
        }
    }

    private void onApproved(long entry, String pan) {
        TransResponse transResponse = onBuilding(issuerCode.getCode(), issuerCode.getName(), 0, entry, pan);
        EventsUtils.getInstance().response(transResponse);
        Intent intent = new Intent(this, TransResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("pan", pan);
        bundle.putLong("entryMode", entry);
        String amt = StringUtils.toDecimalAmount(amount + "", 2);
        bundle.putString("amt", amt);

        if (cardType == 3) {
            bundle.putString("type", "BANKCARD");
        } else if (cardType == 2) {
            bundle.putString("type", getString(R.string.qr_code_ali_pay));
        } else {
            bundle.putString("type", getString(R.string.qr_code_wechat_pay));
        }
        bundle.putString("transType", transType);
        intent.putExtra("bundle", bundle);
        intent.putExtra("baseResult", transResponse.getBaseResult());
        intent.putExtra("transResult", transResponse.getTransResult());
        intent.putExtra("extraInfo", transResponse.getExtraInfo());
        startActivity(intent);
        TransResponseData responseData = new TransResponseData(transResponse);
        RecentUtils.getUtils(this).saveResponseData(responseData);
        finish();
    }

    private void onDeclared(String issuer_name, String issuer_code, long entry, String pan) {
        EventsUtils.getInstance().response(onBuilding(issuer_name, issuer_code, 1, entry, pan));
        finish();
    }

    private long getNumber(int bound) {
        return new Random().nextInt(bound);
    }

    private TransResponse onBuilding(String issuer, String cards, long result, long entry, String pan) {
        TransResponse response = new TransResponse();
        response.setSn(10000000010L);

        String year = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());

        Bundle baseResult = new Bundle();
        baseResult.putLong("RESULT", result);
        baseResult.putString("TRANS_TYPE", "MULTI_PURCHASE");
        baseResult.putString("DESCRIPTION", result == 0 ? "交易成功" : "交易失败");
        baseResult.putString("CUST_NAME", "测试商户");
        baseResult.putString("CUST_NO", "020000011000");
        baseResult.putString("RSP_NO", "00");
        baseResult.putString("TERM_NO", "020000001100000");
        baseResult.putString("EXT_ORDER_NO", year + StringUtils.onGenerateRandom(8));
        baseResult.putString("TRANS_TIME", StringUtils.onGeneratingDate(0));
        baseResult.putLong("TRANS_SEQUENCE", getNumber(100000));

        Bundle transResult = new Bundle();
        transResult.putString("PAY_TYPE", "QRPURCHASE");
        transResult.putString("REF_NO", StringUtils.onGenerateRandom(10));
        transResult.putString("AUTH_ID", StringUtils.isNullOrEmpty(orgAuthNo) ? StringUtils.onGenerateRandom(6) : orgAuthNo);
        transResult.putString("QRCODE_ORDER", StringUtils.onGeneratingDate(0) + StringUtils.onGenerateRandom(14));
        transResult.putString("TRACE_NO", StringUtils.onGenerateRandom(6));

        Bundle extraInfo = new Bundle();
        extraInfo.putString("CARD_ORG", cards);
        extraInfo.putString("CARD_ISSUER", issuer);
        extraInfo.putLong("INPUT_MODE", entry);
        extraInfo.putString("SHILDED_PAN", StringUtils.onGenerateShilds(pan));
        extraInfo.putString("PAN", pan);
        extraInfo.putLong("BANK_REDUCE_AMT", 0);
        extraInfo.putString("EXP_DATE", "2012");

        response.setBaseResult(baseResult);
        response.setTransResult(transResult);
        response.setExtraInfo(extraInfo);
        return response;
    }
}
