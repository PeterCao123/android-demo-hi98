package com.shiji.png.pat.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shiji.png.pat.app.R;
import com.shiji.png.pat.app.domain.service.AmountService;
import com.shiji.png.pat.app.domain.model.PrefInfo;
import com.shiji.png.pat.app.mvp.BaseActivity;
import com.shiji.png.pat.app.domain.persistence.CheckStore;
import com.shiji.png.pat.app.router.ARouter;
import com.shiji.png.pat.app.router.Route;
import com.shiji.png.pat.model.CheckInfo;
import com.shiji.png.payment.ServiceManager;
import com.shiji.png.payment.annotation.ServiceType;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

@Route(path = RoutePath.ACTIVITY_CARD_TYPE)
public class CardTypeActivity extends BaseActivity {

    private static final String TAG = "CardTypeActivity";

    @BindView(R.id.txt_total_amount)
    EditText txtTotalAmt;
    @BindView(R.id.lbl_surcharge)
    TextView lblSurcharge;
    @BindView(R.id.btn_credit_card)
    Button btnCreditCard;
    @BindView(R.id.btn_debit_card)
    Button btnDebitCard;
    @BindView(R.id.btn_wechat)
    Button btnWeChat;

    @OnTextChanged(R.id.txt_total_amount)
    void onTotalAmountChanged(CharSequence text) {
        btnCreditCard.setEnabled(text.length() > 0);
        btnDebitCard.setEnabled(text.length() > 0);
    }
    @OnClick(R.id.btn_credit_card)
    void onClickCreditCard() {
        prepay(getAppPreferences().surchargeRateAsDouble(), ServiceType.BANK_CARD);
    }
    @OnClick(R.id.btn_debit_card)
    void onClickDebitCard() {
        prepay(0D, ServiceType.BANK_CARD);
    }
    @OnClick(R.id.btn_wechat)
    void onClickWeChat() {
        prepay(0D, ServiceType.WX_M_SCAN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_type);

        ButterKnife.bind(this);

        logoInto(R.id.img_logo);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        init();
    }

    private void init() {
        CheckInfo checkInfo = new CheckStore(getApplicationContext()).load();
        if (checkInfo == null) {
            throw new NullPointerException("checkInfo");
        }
        txtTotalAmt.setEnabled(checkInfo.isEnablePartialPayment());
        setAmountView(txtTotalAmt, checkInfo.getAmt());

        PrefInfo prefInfo = getAppPreferences();
        if (prefInfo.isEnableSurcharge()) {
            lblSurcharge.setText(getString(R.string.attracts_surcharge, prefInfo.getSurchargeRate()));
        } else {
            lblSurcharge.setText(R.string.no_surcharge_applied);
        }

        if (ServiceManager.exists(ServiceType.WX_M_SCAN)) {
            btnWeChat.setVisibility(View.VISIBLE);
        }
    }

    private void prepay(double surchargeRate, ServiceType serviceType) {
        BigDecimal amount = AmountService.toAmount(txtTotalAmt.getText().toString());
        BigDecimal totalAmount = new CheckStore(getApplicationContext()).load().getAmt();

        if (AmountService.compare(amount, totalAmount) > 0) {
            toast(R.string.pay_amount_ngt_total);
            return;
        }

        if (AmountService.compare(amount, 0D) <= 0) {
            toast(R.string.positive_amount_required);
            return;
        }

        BigDecimal surchargeAmt = AmountService.toAmount((amount.doubleValue() * surchargeRate) / 100);

        Log.i(TAG, "prepay txnAmt=" + amount + ", surchargeAmt=" + surchargeAmt);

        ARouter.getInstance()
                .build(RoutePath.ACTIVITY_ADD_TIP)
                .withDouble(RoutePath.EXTRA_TXN_AMT, amount.doubleValue())
                .withDouble(RoutePath.EXTRA_SURCHARGE_AMT, surchargeAmt.doubleValue())
                .withString(RoutePath.EXTRA_CHANNEL_TYPE, serviceType.name())
                .navigation();
    }
}
