package com.shiji.png.droid.icbc.simulator.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
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
import com.shiji.png.droid.icbc.simulator.util.SharedPreferenceUtils;
import com.shiji.png.droid.icbc.simulator.util.StringUtils;
import com.shiji.png.payment.model.IssuerCode;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Falcon.cao
 * @date 2019/1/7
 */
public class CardPaymentActivity extends AppCompatActivity {

    private static final String TAG = "CardPaymentActivity";
    private BigDecimal amount;
    private String name = "";
    private String cardNo = "";
    private String transType;
    private String orgAuthNo;
    private String orgRefNo;
    private ProcessDialogService processDialogService;
    private DialogService dialogService;
    private IssuerCode issuerCode;
    private String getAmount() {
        return getCurrencyText() + " " + amount.setScale(getPrecision(), RoundingMode.HALF_EVEN).toPlainString();
    }

    private String getCurrencyText() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String currencyText = sp.getString("currency_text", null);
        if (TextUtils.isEmpty(currencyText)) {
            return "CNY";
        }
        return currencyText;
    }

    private int getPrecision() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String precision = sp.getString("currency_precision", "2");
        if (TextUtils.isEmpty(precision)) {
            return 2;
        }
        return Integer.valueOf(precision);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_payment);
        double amt = getIntent().getDoubleExtra("AMT", 0D);
         transType = getIntent().getStringExtra("transType");
         orgAuthNo = getIntent().getStringExtra("orgAuthNo");
         orgRefNo = getIntent().getStringExtra("orgRefNo");
        this.amount = new BigDecimal(amt);

        ((TextView)findViewById(R.id.lbl_amt)).setText(getAmount());
        findViewById(R.id.qr_linearlayout).setOnClickListener(v->{
            doScan();
        });
        findViewById(R.id.bankcard_linearlayout).setOnClickListener(v->{
            Intent intent = new Intent(CardPaymentActivity.this, BankCardsActivity.class);
            intent.putExtra("AMT", getAmount());
            startActivityForResult(intent, CodeUtils.REQUEST_CODE_BANK);
        });
    }

    private void disposeCapture(Intent data) throws Exception {
        if (data != null && data.getIntExtra(CodeUtils.RESULT_TYPE, 0) == CodeUtils.RESULT_SUCCESS) {
            String result = data.getStringExtra(CodeUtils.RESULT_STRING);
            if (result != null) {
                 distinguishQrCode(result);
            }
        }else{
            name = getString(R.string.qr_code_err);
            throw new Exception(name);
        }
    }

    private void distinguishQrCode(String result) throws Exception {
        if(result.length()>16) {
            String header = result.substring(0, 2);
            switch (header) {
                case "10":
                case "11":
                case "12":
                case "13":
                case "14":
                case "15":
                    name = getString(R.string.qr_code_wechat_pay);
                    issuerCode = IssuerCode.WECHATPAY;
                    break;
                case "25":
                case "26":
                case "27":
                case "28":
                case "29":
                case "30":
                    name = getString(R.string.qr_code_ali_pay);
                    issuerCode = IssuerCode.Alipay;
                    break;
                case "62":
                    name = getString(R.string.qr_code_union_pay);
                    issuerCode = IssuerCode.LIQUIDPAY;
                    break;
                default:
                    name = getString(R.string.qr_code_err);
                    issuerCode =IssuerCode.Other;
                    throw  new Exception(name);
            }
            if (!name.equals(getString(R.string.qr_code_err))) {
                cardNo = result;
            }
        }else{
            name = getString(R.string.qr_code_err);
            throw  new Exception(name);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            onDeclared(1, cardNo);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        DeviceUtils.wakeUp(this);
        if (CodeUtils.REQUEST_CODE_SCAN == requestCode) {
            try {
                disposeCapture(dataIntent);
                onPayments();
                return;
            } catch (Exception e) {
                issuerCode =IssuerCode.Other;
                Observable.just("Demo-hi98")
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .doOnNext(d->dialogService = new DialogService(CardPaymentActivity.this))
                        .doOnNext(d->dialogService.alert(getString(R.string.qr_code_err)))
                        .delay(2,TimeUnit.SECONDS)
                        .doOnNext(d->dialogService.cancellDialog())
                        .doOnNext(d->onDeclared(3, cardNo))
                        .subscribe();
                return;
            }
        }
        if (requestCode == CodeUtils.REQUEST_CODE_BANK&&resultCode == RESULT_OK && dataIntent != null) {
            cardNo = dataIntent.getStringExtra("PAN");
            String type=ReceiptBean.builder()
                    .entryMode("S")
                    .pan(cardNo)
                    .build().getCardType();
            issuerCode = Converter.getIssuerCode(type);
            name = "BANKCARD";
            Observable.just("Demo-hi98")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(d->{processDialogService = new ProcessDialogService(CardPaymentActivity.this,name);})
                    .doOnNext(
                            d->processDialogService.show()
                    )
                    .delay(2,TimeUnit.SECONDS)
                    .doOnNext(d->processDialogService.hide())
                    .doOnNext(d->onApproved(dataIntent.getLongExtra("ENTRY", 1), cardNo))
                    .subscribe();
        } else {
            onDeclared(1, cardNo);
        }
        super.onActivityResult(requestCode, resultCode, dataIntent);
    }
    private void doScan(){
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, CodeUtils.REQUEST_CODE_SCAN);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void onPayments() throws Exception {
        String testMode = SharedPreferenceUtils.get(this,"test_mode");
        if(testMode.equals("Manually Select")) {
            new AlertDialog.Builder(CardPaymentActivity.this, R.style.appalertdialog)
                    .setTitle("Results")
                    .setCancelable(false)
                    .setSingleChoiceItems(new String[]{"Approve", "Declare", "Timeout"}, 0, (dialog, which) -> {
                        dialog.dismiss();
                        if (which == 0) {
                            approval();
                            return;
                        }
                        if (which == 1) {
                            onDeclared(1, cardNo);
                        }
                        if (which == 2) {
                            onTimeouts();
                        }
                    })
                    .show();
        }else if(testMode.equals("All Approval")||testMode.equals("")){
            approval();
        }else{
            throw new Exception("Test Mode Err");
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void approval(){
        Observable.just("Demo-hi98")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(d->{processDialogService = new ProcessDialogService(CardPaymentActivity.this,name);})
                .doOnNext(
                        d->processDialogService.show()
                )
                .delay(2,TimeUnit.SECONDS)
                .doOnNext(d->processDialogService.hide())
                .doOnNext(d->onApproved(3, cardNo))
                .subscribe();
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
        baseResult.putString("EXT_ORDER_NO", year + onGenerateRandom(8));
        baseResult.putString("TRANS_TIME", onGeneratingDate(0));
        baseResult.putLong("TRANS_SEQUENCE", getNumber(100000));

        Bundle transResult = new Bundle();
        transResult.putString("PAY_TYPE", "QRPURCHASE");
        transResult.putString("REF_NO", onGenerateRandom(10));
        transResult.putString("AUTH_ID", onGenerateRandom(6));
        transResult.putString("QRCODE_ORDER", onGeneratingDate(0) + onGenerateRandom(14));
        transResult.putString("TRACE_NO", onGenerateRandom(6));

        Bundle extraInfo = new Bundle();
        extraInfo.putString("CARD_ORG", cards);
        extraInfo.putString("CARD_ISSUER", issuer);
        extraInfo.putLong("INPUT_MODE", entry);
        if ("BANKCARD".equals(name)) {
            extraInfo.putString("SHILDED_PAN", onGenerateShilds(pan));
            extraInfo.putString("PAN", pan);
            extraInfo.putString("type", "BANKCARD");
        } else {
            extraInfo.putString("SHILDED_PAN", onGenerateShilds(pan));
            extraInfo.putString("PAN", pan);
        }
        extraInfo.putLong("BANK_REDUCE_AMT", 0);
        extraInfo.putString("EXP_DATE", "2012");

        response.setBaseResult(baseResult);
        response.setTransResult(transResult);
        response.setExtraInfo(extraInfo);
        return response;
    }

    private void onApproved(long entry, String pan) {
        Bundle bundle = new Bundle();
        bundle.putString("pan",pan);
        if(!name.equals("BANKCARD")){
            bundle.putString("pan",cardNo);
            pan = cardNo;
        }
        TransResponse transResponse = onBuilding(issuerCode.getCode(), issuerCode.getName(), 0, entry, pan);
        EventsUtils.getInstance().response(transResponse);
        Intent intent = new Intent(this,TransResultActivity.class);
        String amt = StringUtils.toDecimalAmount(amount.toString(),2);
        bundle.putString("amt",amt);
        bundle.putString("type",name);
        bundle.putLong("entryMode",entry);
        bundle.putString("transType",transType);
        intent.putExtra("bundle",bundle);
        intent.putExtra("baseResult",transResponse.getBaseResult());
        intent.putExtra("transResult",transResponse.getTransResult());
        intent.putExtra("extraInfo",transResponse.getExtraInfo());
        TransResponseData responseData = new TransResponseData(transResponse);
        RecentUtils.getUtils(this).saveResponseData(responseData);
        startActivity(intent);
        finish();
    }

    private void onDeclared(long entry, String pan) {
        EventsUtils.getInstance().response(onBuilding(issuerCode.getName(), issuerCode.getCode(), 1, entry, pan));
        finish();
    }


    private void onTimeouts() {
        EventsUtils.getInstance().response(null);
        finish();
    }

    private String onGenerateShilds(String pan) {
        if (pan == null || pan.length() < 10) {
            return pan;
        }
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < pan.length(); index++) {
            if (index >= 6 && index < pan.length() - 4) {
                builder.append("*");
            } else {
                builder.append(pan.charAt(index));
            }
        }
        return builder.toString();
    }

    private String onGenerateRandom(int length) {
        StringBuilder builder = new StringBuilder();
        Random rm = new Random();
        for (int index = 0; index < length; index++) {
            builder.append(rm.nextInt(10));
        }
        return builder.toString();
    }

    private String onGeneratingDate(long delay) {
        Date date = new Date(System.currentTimeMillis() + delay);
        return new SimpleDateFormat("yyyyMMddHHmmSS", Locale.getDefault()).format(date);
    }

    private long getNumber(int bound) {
        return new Random().nextInt(bound);
    }
}
