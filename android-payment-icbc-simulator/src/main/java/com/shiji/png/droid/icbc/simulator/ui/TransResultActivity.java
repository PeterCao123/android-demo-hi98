package com.shiji.png.droid.icbc.simulator.ui;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shiji.png.droid.icbc.simulator.R;
import com.shiji.png.droid.icbc.simulator.model.ReceiptBean;
import com.shiji.png.droid.icbc.simulator.model.TransResponse;
import com.shiji.png.droid.icbc.simulator.model.TransResponseData;
import com.shiji.png.droid.icbc.simulator.util.HiPrinter;

/**
 * @author Peter.Cao
 * @date 2019/4/22
 */
public class TransResultActivity extends AppCompatActivity {


    private ImageView line_auth_number;
    private TextView card_no;
    private TextView card_no_tv;
    private TextView voucher_no_tv;
    private TextView auth_no_tv;
    private TextView reference_no_tv;
    private TextView date_time_tv;

    private LinearLayout bankcard_number_linearlayout;
    private LinearLayout auth_number_linearlayout;
    private LinearLayout reference_number_linearlayout;
    private LinearLayout date_time_linearlayout;

    private String amount;
    private String cardNo;
    private String type;
    private Long entry;
    private String transType;
    private ReceiptBean bean;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog;
    private TransResponseData transResponseData;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_result_payment);
        initViews();
    }

    private String getCurrencyText() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String currencyText = sp.getString("currency_text", null);
        if (TextUtils.isEmpty(currencyText)) {
            return "CNY";
        }
        return currencyText;
    }

    private String getEntryMode() {
        if (entry == 1) {
            return "(S)";
        } else if (entry == 0) {
            return "(I)";
        } else {
            return "";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void printReceipt() {
        new Thread(() -> {
            HiPrinter hiPrinter = HiPrinter.create()
                    .appendEmptyLine()
                    .append("MERCHANT COPY")
                    .split()
                    .append("MERCHANT NAME: " + getMerchantName())
                    .append("MERCHANT NO: " + getMerchantId())
                    .append("TERMINAL NO: " + getTerminalId())
                    .append("ACQUIRE: 00000000")
                    .append(type.equals("BANKCARD") ? "CARD NO:" : "BARCODE ORDER NO:")
                    .textSize(HiPrinter.TEXT_SIZE_XL).fontStyle(HiPrinter.FONT_BOLD).align(HiPrinter.ALIGN_CENTER)
                    .append(type.equals("BANKCARD") ? cardNo + getEntryMode() : transResponseData.getTransTime().substring(0, 8) + System.currentTimeMillis())
                    .reset();
            if (type.equals("BANKCARD") || type.equals("WeChat Pay / 微信支付") || type.equals("AliPay / 支付宝") || type.equals("UnionPay / 银联二维码")) {
                hiPrinter.append(type.equals("BANKCARD") ? "CARD TYPE: " + bean.getCardType() : "BARCODE TYPE: ");
                if (!type.equals("BANKCARD")) {
                    hiPrinter.align(HiPrinter.ALIGN_CENTER)
                            .append(type)
                            .reset();
                }
            }
            hiPrinter.append("TRANS TYPE:")
                    .textSize(HiPrinter.TEXT_SIZE_XL).fontStyle(HiPrinter.FONT_BOLD).align(HiPrinter.ALIGN_CENTER)
                    .append(transType)
                    .reset()
                    .append("AUTHCODE: " + transResponseData.getAuthId())
                    .append("REFER NO: " + transResponseData.getRefNo())
                    .append("TRACE NO: " + transResponseData.getTraceNo())
                    .append("VOUCHER NO: " + transResponseData.getTraceNo())
                    .append("BATCH NO: " + transResponseData.getBatchNum() == null ? bean.getBatchNo() : transResponseData.getBatchNum())
                    .append("DATE/TIME: " + transResponseData.getTransTime())
                    .append("AMOUNT:")
                    .textSize(HiPrinter.TEXT_SIZE_XL).fontStyle(HiPrinter.FONT_BOLD).align(HiPrinter.ALIGN_CENTER)
                    .append(amount)
                    .reset()
                    .split()
                    .append("REFERENCE")
                    .append("---------------")
                    .append("Signature")
                    .appendEmptyLine()
                    .append("---------------------")
                    .textSize(HiPrinter.TEXT_SIZE_S)
                    .append("I ACKNOWLEDGE SATISFATORY RECEIPT OF RELATIVE")
                    .appendEmptyLine()
                    .print();
            runOnUiThread(this::hideLoading);

            this.finish();
        }).start();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showLoading(String message) {
        hideLoading();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void initViews() {
        line_auth_number = findViewById(R.id.line_auth_number);

        card_no = findViewById(R.id.card_no);
        card_no_tv = findViewById(R.id.card_no_tv);
        voucher_no_tv = findViewById(R.id.voucher_no_tv);
        auth_no_tv = findViewById(R.id.auth_no_tv);
        reference_no_tv = findViewById(R.id.reference_no_tv);
        date_time_tv = findViewById(R.id.date_time_tv);

        bankcard_number_linearlayout = findViewById(R.id.bankcard_number_linearlayout);
        date_time_linearlayout = findViewById(R.id.date_time_linearlayout);
        auth_number_linearlayout = findViewById(R.id.auth_number_linearlayout);
        reference_number_linearlayout = findViewById(R.id.reference_number_linearlayout);

        TextView typeNoTv = findViewById(R.id.type_no_tv);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        Bundle baseResult = getIntent().getBundleExtra("baseResult");
        Bundle transResult = getIntent().getBundleExtra("transResult");
        Bundle extraInfo = getIntent().getBundleExtra("extraInfo");
        TransResponse response = new TransResponse();
        response.setBaseResult(baseResult);
        response.setExtraInfo(extraInfo);
        response.setTransResult(transResult);
        transResponseData = new TransResponseData(response);
        cardNo = bundle.getString("pan");
        entry = bundle.getLong("entryMode");
        amount = getCurrencyText() + " " + bundle.getString("amt");
        type = bundle.getString("type");
        transType = bundle.getString("transType");
        bean = ReceiptBean.builder()
                .entryMode("S")
                .pan(cardNo)
                .build();
        if (!type.equals("BANKCARD")) {
            auth_number_linearlayout.setVisibility(View.GONE);
            card_no.setText("Bar Code");
            typeNoTv.setText(type);
        } else {
            typeNoTv.setText(bean.getCardType());
        }
        if (type.equals("BANKCARD")) {
            cardNo = cardNo.substring(0, 6) + "******" + cardNo.substring(cardNo.length() - 4);
        }
        card_no_tv.setText(cardNo);
        voucher_no_tv.setText(transResponseData.getTraceNo());
        date_time_tv.setText(transResponseData.getTransTime());
        reference_no_tv.setText(transResponseData.getRefNo());
        auth_no_tv.setText(transResponseData.getAuthId());
        TextView amountText = findViewById(R.id.amount);
        amountText.setText(amount);
        TextView transTypeTv = findViewById(R.id.trans_type);
        transTypeTv.setText(transType);
        showLoading(" Printing Receipt...");
        handler.postDelayed(this::printReceipt, 3000);
    }

    @Override
    protected void onDestroy() {
        hideLoading();
        super.onDestroy();
    }

    private String getMerchantName() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String currencyText = sp.getString("merchant_name", null);
        if (TextUtils.isEmpty(currencyText)) {
            return "";
        }
        return currencyText;
    }

    private String getMerchantId() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String mer_id = sp.getString("mer_id", null);
        if (TextUtils.isEmpty(mer_id)) {
            return "";
        }
        return mer_id;
    }

    private String getTerminalId() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String mer_id = sp.getString("ter_id", null);
        if (TextUtils.isEmpty(mer_id)) {
            return "";
        }
        return mer_id;
    }
}
