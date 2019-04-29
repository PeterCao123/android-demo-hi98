package com.shiji.png.pat.app.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.shiji.png.pat.app.BuildConfig;
import com.shiji.png.pat.app.R;
import com.shiji.png.pat.app.domain.service.AmountService;
import com.shiji.png.pat.app.domain.model.PrefInfo;
import com.shiji.png.pat.app.functional.EmptyObserver;
import com.shiji.png.pat.app.mvp.BaseActivity;
import com.shiji.png.pat.app.domain.persistence.CheckStore;
import com.shiji.png.pat.app.router.ARouter;
import com.shiji.png.pat.app.router.Autowired;
import com.shiji.png.pat.app.router.Route;
import com.shiji.png.pat.app.spat.Transform;
import com.shiji.png.pat.app.ui.dialog.DialogService;
import com.shiji.png.pat.app.ui.dialog.ProcessDialogService;
import com.shiji.png.pat.model.CheckInfo;
import com.shiji.png.pat.spat.dto.RespCode;
import com.shiji.png.pat.spat.model.PaymentModel;
import com.shiji.png.pat.spat.service.SpatClient;
import com.shiji.png.payment.PaymentService;
import com.shiji.png.payment.ServiceInfo;
import com.shiji.png.payment.ServiceManager;
import com.shiji.png.payment.annotation.ServiceType;
import com.shiji.png.payment.message.TxRequest;
import com.shiji.png.payment.message.TxResponse;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.widget.AdapterView.INVALID_POSITION;
import static com.shiji.png.payment.model.RespCode.AppError;

@Route(path = RoutePath.ACTIVITY_ADD_TIP)
public class AddTipActivity extends BaseActivity {

    private static final String TAG = "AddTipActivity";

    private static final String FIELD_RATE = "rate";
    private static final String FIELD_TEXT = "text";

    private static final String STATE_TIP_AMOUNT = "tipAmt";

    @Autowired(name = RoutePath.EXTRA_TXN_AMT, required = true)
    double extraTxnAmt;
    @Autowired(name = RoutePath.EXTRA_SURCHARGE_AMT, required = true)
    double extraSurchargeAmt;
    @Autowired(name = RoutePath.EXTRA_CHANNEL_TYPE, required = true)
    String channelTypeName;
    ServiceType serviceType;

    private BigDecimal txnAmt = BigDecimal.ZERO;
    private BigDecimal surchargeAmt = BigDecimal.ZERO;
    private BigDecimal tipAmt = BigDecimal.ZERO;
    private BigDecimal grandTotal = BigDecimal.ZERO;
    private BigDecimal maxTipAmount = BigDecimal.ZERO;

    private CheckInfo checkInfo;

    private int selectedTipPosition = INVALID_POSITION;

    @BindView(R.id.lbl_txn_amt)
    TextView lblTxnAmt;
    @BindView(R.id.lbl_surcharge_amt)
    TextView lblSurchargeAmt;
    @BindView(R.id.lbl_tip_amt)
    TextView lblTipAmt;
    @BindView(R.id.lbl_grand_total)
    TextView lblGrandTotal;
    @BindView(R.id.tip_grid)
    GridView tipGrid;
    @BindView(R.id.btn_next)
    Button btnNext;

    @SuppressWarnings("unchecked")
    @OnItemClick(R.id.tip_grid)
    void onClickTip(AdapterView<?> parent, View view, int position) {
        selectTip(view, position);
        Map<String, Object> item = (Map<String, Object>)parent.getItemAtPosition(position);
        changeTipRate((double)item.get(FIELD_RATE));
    }
    @OnClick(R.id.btn_other_tip)
    void onClickOtherTip() {
        new DialogService(this)
                .input(text -> {
                    if (!TextUtils.isEmpty(text)) {
                        clearTipSelection();
                        changeTipAmount(AmountService.toAmount(text));
                    }
                })
                .showInput(R.layout.dlg_other_tip_amount);
    }
    @OnClick(R.id.btn_next)
    void onClickNext() {
        ServiceInfo[] services = ServiceManager.getServiceList(serviceType)
                .toArray(new ServiceInfo[0]);
        if (services.length == 1) {
            startPay(services[0]);
            return;
        }
        String[] displayText = new String[services.length];
        for (int i = 0; i < services.length; i++) {
            displayText[i] = services[i].getDisplayText();
        }
        new DialogService(this)
                .choice(i -> startPay(services[i]))
                .singleChoice(displayText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tip);

        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);

        serviceType = ServiceType.valueOf(channelTypeName);
        txnAmt = AmountService.toAmount(extraTxnAmt);
        surchargeAmt = AmountService.toAmount(extraSurchargeAmt);

        logoInto(R.id.img_logo);

        init();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_TIP_AMOUNT)) {
            changeTipAmount(savedInstanceState.getDouble(STATE_TIP_AMOUNT));
        } else {
            if (tipGrid.getCount() > 0) {
                final int position = 0;
                tipGrid.post(() -> onClickTip(tipGrid, tipGrid.getChildAt(position), position));
            }
            preparePay();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putDouble(STATE_TIP_AMOUNT, tipAmt.doubleValue());
        super.onSaveInstanceState(outState);
    }

    private void enableNextButton() {
        btnNext.setEnabled(true);
    }
    private void disableNextButton() {
        btnNext.setEnabled(false);
    }

    private void startPay(ServiceInfo serviceInfo) {
        Log.i(TAG, "startPay serviceName=" + serviceInfo.getName());
        try {
            PaymentService service = ServiceManager.create(serviceInfo);
            final TxRequest request = createSaleRequest();
            service.sale(request)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(d -> disableNextButton())
                    .onErrorReturn(tr -> {
                        Log.e(TAG, "onErrorReturn", tr);
                        return TxResponse.builder()
                                .respCode(AppError)
                                .respText(tr.getMessage())
                                .build();
                    })
                    .doOnNext(response -> {
                        if (!response.isApprove()) {
                            String error = "" + response.getRespCode() + " - " + response.getRespText();
                            new DialogService(AddTipActivity.this)
                                    .positiveTextRes(R.string.retry)
                                    .positive(this::onClickNext)
                                    .negative(() -> ARouter.getInstance().build(RoutePath.ACTIVITY_ENTER_TABLE_NO).navigation())
                                    .confirm(getString(R.string.bank_transaction_failed, error));
                            return;
                        }
                        PrefInfo prefInfo = getAppPreferences();
                        PaymentModel m = PaymentModel.builder()
                                .txnAmt(request.getTxnAmt())
                                .surchargeAmt(response.getSurchargeAmt())
                                .tipAmt(response.getTipAmt())
                                .totalAmt(response.getTotalAmt())
                                .txnCurrText(response.getTxnCurrText())
                                .txnCurrCode(response.getTxnCurrCode())
                                .merId(checkInfo.getMerId())
                                .rvcId(checkInfo.getRvcId())
                                .terId(prefInfo.getTerId())
                                .refNo(response.getRefNo())
                                .merRef(response.getMerRef())
                                .traceNo(response.getTraceNo())
                                .batchNo(response.getBatchNo())
                                .issuerCode(response.getIssuerCode().getCode())
                                .issuerName(response.getIssuerCode().getName())
                                .cardType(response.getCardType())
                                .entryMode(response.getEntryMode().getValue())
                                .maskedPan(response.getMaskedPan())
                                .cardType(response.getCardType())
                                .authCode(response.getAuthCode())
                                .acqMerId(response.getAcqMerId())
                                .acqTerId(response.getAcqTerId())
                                .acqTxnDatetime(response.getAcqTxnTimestamp())
                                .acqTxnNo(response.getAcqTxnNo())
                                .acqRespCode(response.getAcqRespCode())
                                .dccFlag(response.getDccFlag().getValue())
                                .dccAmt(response.getDccAmt())
                                .dccRate(response.getDccRate())
                                .markup(response.getDccMarkup())
                                .dccCurrCode(response.getDccCurrCode())
                                .dccCurrText(response.getDccCurrText())
                                .source(BuildConfig.SOURCE)
                                .deviceBrand(Build.MANUFACTURER)
                                .deviceModel(Build.PRODUCT)
                                .build();
                        pushPaymentResult(m, false);
                    })
                    .doOnComplete(this::enableNextButton)
                    .subscribe(new EmptyObserver<>());
        } catch (Exception e) {
            Log.e(TAG, "create service", e);
            new DialogService(this).alert(e.getMessage());
        }
    }

    private TxRequest createSaleRequest() {
        PrefInfo prefInfo = getAppPreferences();
        return TxRequest.builder()
                .txnCurrCode(prefInfo.getCurrencyCode())
                .txnAmt(txnAmt.doubleValue())
                .surchargeAmt(surchargeAmt.doubleValue())
                .tipAmt(tipAmt.doubleValue())
                .cashOutAmt(0D)
                .totalAmt(grandTotal.doubleValue())
                .merRef(buildMerRef(checkInfo.getTableNo(), checkInfo.getCheckNo()))
                .build();
    }

    private void pushPaymentResult(final PaymentModel model, final boolean retry) {
        PrefInfo prefInfo = getAppPreferences();
        SpatClient client = SpatClient.builder().config(prefInfo.getApiConfig()).build();

        final ProcessDialogService processDialogService = new ProcessDialogService(this)
                .message(R.string.posting_payment);

        client.payments(checkInfo.getTableNo(), checkInfo.getCheckNo(), model, retry)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> processDialogService.show())
                .doOnComplete(processDialogService::hide)
                .doOnError(tr -> processDialogService.hide())
                .doOnError(tr -> Log.e(TAG, "payments", tr))
                .doOnError(tr -> onPushFailed(model, false, tr.getMessage()))
                .doOnNext(dto -> {
                    if (!dto.approved()) {
                        Log.e(TAG, "payments: " + dto.toString());
                        onPushFailed(model, RespCode.TableLocker.equals(dto.getRespCode()), dto.getErrorMessage());
                        return;
                    }
                    onPushSuccess();
                })
                .subscribe(new EmptyObserver<>());
    }

    private void onPushFailed(final PaymentModel model, boolean isTableLocked, String message) {
        DialogService dialogService = new DialogService(this)
                .positive(() -> pushPaymentResult(model, true));
        if (isTableLocked) {
            dialogService.alert(getString(R.string.post_result_failed, message));
        } else {
            dialogService.confirm(getString(R.string.post_result_failed, message));
        }
    }

    private void onPushSuccess() {
        new DialogService(this)
                .overridePositive()
                .negative(() -> ARouter.getInstance().build(RoutePath.ACTIVITY_ENTER_TABLE_NO).navigation())
                .positive(this::printPreview)
                .confirm(R.string.print_final_check_message);
    }

    private void printPreview() {
        ARouter.getInstance().build(RoutePath.ACTIVITY_PRINT_PREVIEW)
                .withString(RoutePath.EXTRA_TABLE_NO, checkInfo.getTableNo())
                .withString(RoutePath.EXTRA_CHECK_NO, checkInfo.getCheckNo())
                .withBoolean(RoutePath.EXTRA_RECEIPT, true)
                .navigation();
    }

    private void preparePay() {
        final DialogService dialogService = new DialogService(this);
        final ProcessDialogService processDialogService = new ProcessDialogService(this)
                .message(R.string.preparing_pay);
        SpatClient client = SpatClient.builder()
                .config(getAppPreferences().getApiConfig())
                .build();
        client.getChecksByCheckNo(checkInfo.getCheckNo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> processDialogService.show())
                .doOnComplete(processDialogService::hide)
                .doOnError(tr -> processDialogService.hide())
                .doOnError(tr -> Log.e(TAG, "getChecksByCheckNo: " + checkInfo.getCheckNo(), tr))
                .doOnError(dialogService::alert)
                .doOnNext(dto -> {
                    if (!dto.approved()) {
                        dialogService.alert(dto.getErrorMessage());
                        return;
                    }
                    verifyCheckStatus(checkInfo, Transform.getChecks(dto.getData()).get(0));
                })
                .subscribe(new EmptyObserver<>());
    }

    private void verifyCheckStatus(CheckInfo old, CheckInfo current) {
        if (!current.isOpened()
                || AmountService.compare(old.getAmt(), current.getAmt()) != 0) {
            new DialogService(this)
                    .positive(this::finish)
                    .alert(R.string.check_status_changed);
        }
    }

    private void changeTipRate(double rate) {
        changeTipAmount(txnAmt.doubleValue() * rate);
    }

    private void changeTipAmount(double amount) {
        this.changeTipAmount(AmountService.toAmount(amount));
    }

    private void changeTipAmount(BigDecimal amount) {
        this.tipAmt = amount;
        if (AmountService.compare(amount, maxTipAmount) > 0) {
            this.tipAmt = maxTipAmount;
        }
        grandTotal = AmountService.sum(txnAmt, surchargeAmt, tipAmt);

        setAmountView(lblTipAmt, tipAmt);
        setAmountView(lblGrandTotal, grandTotal);
    }

    private void init() {
        checkInfo = new CheckStore(getApplicationContext()).load();
        if (checkInfo == null) {
            throw new NullPointerException("checkInfo");
        }
        setAmountView(lblTxnAmt, txnAmt);
        setAmountView(lblSurchargeAmt, surchargeAmt);

        PrefInfo prefInfo = getAppPreferences();
        this.maxTipAmount = prefInfo.maxTipAmount();

        initTips();
    }

    private void initTips() {
        Double[] tipRates = getAppPreferences().tipRateAsArray();
        SimpleAdapter adapter = new SimpleAdapter(this,
                toRenderTipRates(tipRates),
                R.layout.grid_item_tip_rate,
                new String[] { FIELD_TEXT },
                new int[] {R.id.lbl_text});
        tipGrid.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void selectTip(View view, int position) {
        clearTipSelection();
        view.setBackgroundResource(R.drawable.item_bg_tip_selected);
        selectedTipPosition = position;
    }

    private void clearTipSelection() {
        if (selectedTipPosition != INVALID_POSITION) {
            tipGrid.getChildAt(selectedTipPosition).setBackgroundResource(R.drawable.item_bg_tip_unselected);
            selectedTipPosition = INVALID_POSITION;
        }
    }

    private List<Map<String, ?>> toRenderTipRates(Double[] values) {
        List<Map<String, ?>> items = new ArrayList<>();
        for (Double value : values) {
            Map<String, Object> item = new HashMap<>(values.length);
            item.put(FIELD_RATE, value / 100);
            item.put(FIELD_TEXT, String.valueOf(value) + "%");
            items.add(item);
        }
        return items;
    }

    private static String buildMerRef(String tableNo, String checkNo) {
        String ts = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        return ts + "-" + tableNo + "-" + checkNo;
    }

}
