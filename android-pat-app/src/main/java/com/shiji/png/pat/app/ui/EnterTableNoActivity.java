package com.shiji.png.pat.app.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.shiji.png.pat.app.R;
import com.shiji.png.pat.app.domain.InvalidPreferenceException;
import com.shiji.png.pat.app.domain.model.PrefInfo;
import com.shiji.png.pat.app.domain.service.ValidateService;
import com.shiji.png.pat.app.functional.EmptyObserver;
import com.shiji.png.pat.app.mvp.BaseActivity;
import com.shiji.png.pat.app.domain.persistence.CheckStore;
import com.shiji.png.pat.app.router.ARouter;
import com.shiji.png.pat.app.router.Route;
import com.shiji.png.pat.app.spat.Transform;
import com.shiji.png.pat.app.ui.dialog.DialogService;
import com.shiji.png.pat.app.ui.dialog.ProcessDialogService;
import com.shiji.png.pat.model.CheckInfo;
import com.shiji.png.pat.spat.service.ApiConfig;
import com.shiji.png.pat.spat.service.SpatClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Route(path = RoutePath.ACTIVITY_ENTER_TABLE_NO)
public class EnterTableNoActivity extends BaseActivity {

    private static final String TAG = "EnterTableNoActivity";

    private String tableNo;

    @BindView(R.id.txt_table_no)
    EditText txtTableNo;
    @BindView(R.id.btn_next)
    Button btnNext;

    @OnTextChanged(R.id.txt_table_no)
    void onChangedTableNo(CharSequence tableNo) {
        this.tableNo = tableNo.toString();
        btnNext.setEnabled(!TextUtils.isEmpty(tableNo));
    }

    @OnClick(R.id.btn_settings)
    void onClickSetting() {
        ARouter.getInstance().build(RoutePath.ACTIVITY_SETTINGS).navigation();
    }
    @OnClick(R.id.btn_cancel)
    void onClickCancel() {
        txtTableNo.setText("");
        txtTableNo.requestFocus();
    }
    @OnClick(R.id.btn_next)
    void onClickNext() {
        PrefInfo prefInfo = getAppPreferences();
        try {
            ValidateService.validate(prefInfo);
            getChecks(prefInfo.getApiConfig(), tableNo);
        } catch (InvalidPreferenceException e) {
            onPreferenceInvalid(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_table_no);

        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        logoInto(R.id.img_logo);
    }

    private void onPreferenceInvalid(InvalidPreferenceException e) {
        Log.e(TAG, "onPreferenceInvalid", e);
        new DialogService(this).alert(e.getResId());
    }

    private void getChecks(ApiConfig config, String tableNo) {
        final DialogService dialogService = new DialogService(this);
        final ProcessDialogService processDialogService = new ProcessDialogService(this)
                .message(R.string.getting_checks);
        SpatClient client = SpatClient.builder().config(config).build();
        client.getChecksByTableNo(tableNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> processDialogService.show())
                .doOnComplete(processDialogService::hide)
                .doOnError(tr -> processDialogService.hide())
                .doOnError(tr -> Log.e(TAG, "getChecks", tr))
                .doOnError(dialogService::alert)
                .doOnNext(dto -> {
                    if (!dto.approved()) {
                        dialogService.alert(dto.getErrorMessage());
                        return;
                    }
                    handleChecks(Transform.getChecks(dto.getData()));
                })
                .subscribe(new EmptyObserver<>());
    }

    private void handleChecks(List<CheckInfo> checks) {
        if (checks.isEmpty()) {
            new DialogService(this).alert(R.string.no_valid_checks);
            return;
        }

        if (checks.size() == 1) {
            select(checks.get(0));
            return;
        }

        new DialogService(this)
                .title(R.string.select_check_to_pay)
                .cancelable()
                .choice(i -> select(checks.get(i)))
                .singleChoice(choices(checks));
    }

    private String[] choices(List<CheckInfo> checks) {
        String[] items = new String[checks.size()];
        for (int i = 0; i < checks.size(); i++) {
            CheckInfo check = checks.get(i);
            String item = check.getCheckNo();
            if (!TextUtils.isEmpty(check.getSeatNo())) {
                item += " - " + check.getSeatNo();
            }
            item += " - " + check.getTotalAmt();
            items[i] = item;
        }
        return items;
    }

    private void select(CheckInfo check) {
        Log.d(TAG, "select: " + check.toString());
        new CheckStore(getApplicationContext()).save(check);
        ARouter.getInstance().build(RoutePath.ACTIVITY_CHECK_AMOUNT).navigation();
    }

}
