package com.shiji.png.pat.app.ui;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.widget.EditText;

import com.shiji.png.pat.app.R;
import com.shiji.png.pat.app.domain.InvalidPreferenceException;
import com.shiji.png.pat.app.domain.model.PrefInfo;
import com.shiji.png.pat.app.domain.service.ValidateService;
import com.shiji.png.pat.app.functional.EmptyObserver;
import com.shiji.png.pat.app.mvp.BaseActivity;
import com.shiji.png.pat.app.domain.persistence.ConfigurationStore;
import com.shiji.png.pat.app.router.ARouter;
import com.shiji.png.pat.app.ui.dialog.ProcessDialogService;
import com.shiji.png.pat.app.util.EmojiFilter;
import com.shiji.png.pat.model.ConfigurationInfo;
import com.shiji.png.pat.spat.dto.UserValidationDTO;
import com.shiji.png.pat.spat.model.UserValidationModel;
import com.shiji.png.pat.spat.service.ApiConfig;
import com.shiji.png.pat.spat.service.SpatClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.txt_user_id)
    EditText txtUserId;
    @BindView(R.id.txt_password)
    EditText txtPassword;

    @OnClick(R.id.btn_settings)
    void onClickSettings() {
        ARouter.getInstance().build(RoutePath.ACTIVITY_SETTINGS).navigation();
    }
    @OnClick(R.id.btn_login)
    void onClickLogin() {
        String userId = txtUserId.getText().toString();
        if (TextUtils.isEmpty(userId)) {
            toast(R.string.invalid_user_id);
            return;
        }
        String password = txtPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            toast(R.string.invalid_password);
            return;
        }
        try {
            PrefInfo prefInfo = getAppPreferences();
            ValidateService.validate(prefInfo);

            doLogin(prefInfo.getApiConfig(), userId, password);
        } catch (InvalidPreferenceException e) {
            toast(e.getResId());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        txtPassword.setFilters(new InputFilter[] {new EmojiFilter()});
    }

    @Override
    protected void onResume() {
        super.onResume();

        ConfigurationInfo info = new ConfigurationStore(getApplicationContext()).load();
        if (info != null && !info.isEnableLogon()) {
            ARouter.getInstance().build(RoutePath.ACTIVITY_ENTER_TABLE_NO).navigation();
            finish();
        } else {
            logoInto(R.id.img_logo);
        }
    }

    private void doLogin(ApiConfig config, String userId, String password) {
        final ProcessDialogService processDialogService = new ProcessDialogService(this)
                .message(R.string.logging);
        SpatClient client = SpatClient.builder().config(config).build();
        UserValidationModel model = UserValidationModel.builder()
                .operator(userId)
                .password(password)
                .build();
        client.userValidation(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> processDialogService.show())
                .doOnNext(this::handleUserValidationResult)
                .doOnError(tr -> processDialogService.hide())
                .doOnError(this::toast)
                .doOnComplete(processDialogService::hide)
                .subscribe(new EmptyObserver<>());
    }

    private void handleUserValidationResult(UserValidationDTO dto) {
        if (!dto.approved()) {
            toast(dto.getErrorMessage());
            return;
        }
        ARouter.getInstance().build(RoutePath.ACTIVITY_ENTER_TABLE_NO).navigation();
        finish();
    }

}
