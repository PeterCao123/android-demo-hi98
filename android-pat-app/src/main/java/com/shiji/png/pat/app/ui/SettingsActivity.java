package com.shiji.png.pat.app.ui;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.shiji.png.pat.app.R;
import com.shiji.png.pat.app.domain.InvalidPreferenceException;
import com.shiji.png.pat.app.domain.model.PrefInfo;
import com.shiji.png.pat.app.domain.service.ValidateService;
import com.shiji.png.pat.app.functional.EmptyObserver;
import com.shiji.png.pat.app.mvp.BasePreferenceActivity;
import com.shiji.png.pat.app.domain.persistence.ConfigurationStore;
import com.shiji.png.pat.app.domain.persistence.LogoStore;
import com.shiji.png.pat.app.domain.persistence.PrefStore;
import com.shiji.png.pat.app.router.ARouter;
import com.shiji.png.pat.app.router.Route;
import com.shiji.png.pat.app.ui.dialog.DialogService;
import com.shiji.png.pat.app.ui.dialog.ProcessDialogService;
import com.shiji.png.pat.model.ConfigurationInfo;
import com.shiji.png.pat.model.LogoInfo;
import com.shiji.png.pat.spat.dto.GetConfigurationDTO;
import com.shiji.png.pat.spat.dto.GetLogoDTO;
import com.shiji.png.pat.spat.service.ApiConfig;
import com.shiji.png.pat.spat.service.SpatClient;

import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Route(path = RoutePath.ACTIVITY_SETTINGS)
public class SettingsActivity extends BasePreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    private static final String TAG = "SettingsActivity";

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        //Log.v(TAG, preference.getKey() + " => " + newValue);
        String value = newValue.toString();
        if (TextUtils.isEmpty(value)) {
            return false;
        }
        String summary = value;
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference)preference;
            int index = listPreference.findIndexOfValue(value);
            summary = index >= 0 ? listPreference.getEntries()[index].toString() : null;
        } else if (key(R.string.pref_key_api_password).equals(preference.getKey())) {
            summary = value.substring(0, 3) + "********";
        } else if (key(R.string.pref_key_currency_code).equals(preference.getKey())) {
            try {
                Currency.getInstance(value);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "check currency code " + value, e);
                new DialogService(this).alert(R.string.invalid_currency_message);
                return false;
            }
        }
        preference.setSummary(summary);
        return true;
    }

    private void setReloadSummary(ConfigurationInfo configurationInfo) {
        Preference preference = findPreference(key(R.string.pref_key_reload_configuration));
        if (configurationInfo == null) {
            preference.setSummary("");
        } else {
            preference.setSummary(getString(R.string.pref_reload_configuration_summary,
                    configurationInfo.getLastUpdateAt()));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);
        bindPreferenceSummaryToValue(findPreference(key(R.string.pref_key_surcharge_rate)));
        bindPreferenceSummaryToValue(findPreference(key(R.string.pref_key_tip_rate)));
        bindPreferenceSummaryToValue(findPreference(key(R.string.pref_key_max_tip_amount)));
        bindPreferenceSummaryToValue(findPreference(key(R.string.pref_key_currency_code)));
        bindPreferenceSummaryToValue(findPreference(key(R.string.pref_key_receipt_type)));
        bindPreferenceSummaryToValue(findPreference(key(R.string.pref_key_mer_id)));
        bindPreferenceSummaryToValue(findPreference(key(R.string.pref_key_rvc_id)));
        bindPreferenceSummaryToValue(findPreference(key(R.string.pref_key_ter_id)));
        bindPreferenceSummaryToValue(findPreference(key(R.string.pref_key_pos_mer_id)));
        bindPreferenceSummaryToValue(findPreference(key(R.string.pref_key_pos_rvc_id)));
        bindPreferenceSummaryToValue(findPreference(key(R.string.pref_key_api_gateway)));
        bindPreferenceSummaryToValue(findPreference(key(R.string.pref_key_api_user)));
        bindPreferenceSummaryToValue(findPreference(key(R.string.pref_key_api_bearer)));
        bindPreferenceSummaryToValue(findPreference(key(R.string.pref_key_api_password)));
        bindPreferenceSummaryToValue(findPreference(key(R.string.pref_key_api_connect_timeout)));
        bindPreferenceSummaryToValue(findPreference(key(R.string.pref_key_api_read_timeout)));

        findPreference(key(R.string.pref_key_test_connection))
                .setOnPreferenceClickListener(this::onClickTestConnection);
        findPreference(key(R.string.pref_key_reload_configuration))
                .setOnPreferenceClickListener(this::onClickLoadConfiguration);
        findPreference(key(R.string.pref_key_reload_logo))
                .setOnPreferenceClickListener(this::onClickLoadLogo);
        findPreference(key(R.string.pref_key_about))
                .setOnPreferenceClickListener(this::onClickAbout);

        setReloadSummary(new ConfigurationStore(getApplicationContext()).load());
    }

    @Override
    public void onBackPressed() {
        ConfigurationInfo info = new ConfigurationStore(getApplicationContext()).load();
        PrefInfo prefs = new PrefStore(getApplicationContext()).load();

        String prefMerId = prefs.getMerId();
        String prefRvcId = prefs.getRvcId();
        if (prefMerId != null && prefRvcId != null) {
            if (info == null
                    || !prefMerId.equals(info.getMerId())
                    || !prefRvcId.equals(info.getRvcId())) {
                new DialogService(this)
                        .negative(this::finish)
                        .confirm(R.string.warning_pref_changed);
                return;
            }
        }

        this.finish();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (savedInstanceState == null) {
            new DialogService(this)
                    .negative(this::finish)
                    .input(text -> {
                        if (!"112233".equals(text)) {
                            finish();
                            Toast.makeText(this, R.string.invalid_password, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .showInput(R.layout.dlg_confirm_password);
        }
    }

    private String key(@StringRes int resId) {
        return getString(resId);
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);

        String def = "";
        if (key(R.string.pref_key_surcharge_rate).equals(preference.getKey())) {
            def = getString(R.string.pref_surcharge_rate_default_value);
        } else if (key(R.string.pref_key_max_tip_amount).equals(preference.getKey())) {
            def = getString(R.string.pref_max_tip_amount_default_value);
        } else if (key(R.string.pref_key_currency_code).equals(preference.getKey())) {
            def = getString(R.string.pref_currency_code_default_value);
        } else if (key(R.string.pref_key_api_gateway).equals(preference.getKey())) {
            def = getString(R.string.pref_api_gateway_default_value);
        } else if (key(R.string.pref_key_api_connect_timeout).equals(preference.getKey())) {
            def = getString(R.string.pref_api_connect_timeout_default_value);
        } else if (key(R.string.pref_key_api_read_timeout).equals(preference.getKey())) {
            def = getString(R.string.pref_api_read_timeout_default_value);
        }

        this.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), def));
    }

    private void onPreferenceInvalid(InvalidPreferenceException e) {
        Log.e(TAG, "onPreferenceInvalid", e);
        new DialogService(this).alert(e.getResId());
    }

    @SuppressWarnings({"unused", "SameReturnValue"})
    private boolean onClickTestConnection(Preference preference) {
        PrefInfo prefInfo = new PrefStore(getApplicationContext()).load();
        Log.d(TAG, "prefInfo: " + prefInfo.toString());
        try {
            ValidateService.validate(prefInfo);

            new DialogService(this)
                    .positive(() -> this.doTestConnection(prefInfo.getApiConfig()))
                    .confirm(R.string.test_confirm_msg);
        } catch (InvalidPreferenceException e) {
            onPreferenceInvalid(e);
        }
        return false;
    }

    private void doTestConnection(ApiConfig config) {
        final DialogService dialogService = new DialogService(this);
        final ProcessDialogService processDialogService = new ProcessDialogService(this)
                .message(R.string.testing);
        //TODO: crash if gateway url invalid
        SpatClient client = SpatClient.builder().config(config).build();
        client.apiValidation()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> processDialogService.show())
                .doOnError(tr -> Log.e(TAG, "apiValidation error", tr))
                .doOnError(tr -> processDialogService.hide())
                .doOnError(dialogService::alert)
                .doOnNext(dto -> {
                    if (!dto.approved()) {
                        dialogService.alert(dto.getErrorMessage());
                    } else {
                        dialogService.alert(R.string.connection_successful);
                    }
                })
                .doOnComplete(processDialogService::hide)
                .subscribe(new EmptyObserver<>());
    }

    @SuppressWarnings({"unused", "SameReturnValue"})
    private boolean onClickLoadConfiguration(Preference preference) {
        PrefInfo prefInfo = new PrefStore(getApplicationContext()).load();
        try {
            ValidateService.validate(prefInfo);

            new DialogService(this)
                    .positive(() -> this.doLoadConfiguration(prefInfo.getApiConfig()))
                    .confirm(R.string.reload_confirm_msg);
        } catch (InvalidPreferenceException e) {
            onPreferenceInvalid(e);
        }
        return false;
    }

    private void doLoadConfiguration(ApiConfig config) {
        final DialogService dialogService = new DialogService(this);
        final ProcessDialogService processDialogService = new ProcessDialogService(this);
        SpatClient client = SpatClient.builder().config(config).build();
        client.getConfiguration()
                .subscribeOn(Schedulers.io())
                .flatMap(dto -> {
                    if (!dto.approved()) {
                        return Observable.error(new Exception(dto.getErrorMessage()));
                    }
                    saveConfiguration(config.getMerId(), config.getRvcId(), dto.getData());
                    return client.getLogo();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> processDialogService.show())
                .doOnError(tr -> Log.e(TAG, "getConfiguration error", tr))
                .doOnError(tr -> processDialogService.hide())
                .doOnError(dialogService::alert)
                .doOnNext(dto -> {
                    if (!dto.approved()) {
                        dialogService.alert(dto.getErrorMessage());
                    } else {
                        saveLogo(config.getMerId(), config.getRvcId(), dto.getData());
                        dialogService.alert(R.string.reload_configuration_successful);
                    }
                })
                .doOnComplete(processDialogService::hide)
                .subscribe(new EmptyObserver<>());
    }

    private void saveConfiguration(String merId, String rvcId, GetConfigurationDTO.Data data) {
        String timestamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());
        ConfigurationInfo info = ConfigurationInfo.builder()
                .merId(merId)
                .rvcId(rvcId)
                .enableLogon(data.isEnableLogon())
                .enablePartialPayment(data.isEnablePartialPayment())
                .enableTip(data.isEnableTip())
                .enableSurcharge(data.isEnableSurcharge())
                .lastUpdateAt(timestamp)
                .build();
        new ConfigurationStore(getApplicationContext()).save(info);

        runOnUiThread(() -> setReloadSummary(info));
    }

    private void saveLogo(String merId, String rvcId, GetLogoDTO.Data data) {
        LogoInfo info = LogoInfo.builder()
                .merId(merId)
                .rvcId(rvcId)
                .data(data.getData())
                .fileType(data.getFileType())
                .build();
        new LogoStore(getApplicationContext()).save(info);
    }

    @SuppressWarnings({"unused", "SameReturnValue"})
    private boolean onClickLoadLogo(Preference preference) {
        PrefInfo prefInfo = new PrefStore(getApplicationContext()).load();
        try {
            ValidateService.validate(prefInfo);

            new DialogService(this)
                    .positive(() -> this.doLoadLogo(prefInfo.getApiConfig()))
                    .confirm(R.string.reload_confirm_msg);
        } catch (InvalidPreferenceException e) {
            onPreferenceInvalid(e);
        }
        return false;
    }

    private void doLoadLogo(ApiConfig config) {
        final DialogService dialogService = new DialogService(this);
        final ProcessDialogService processDialogService = new ProcessDialogService(this);
        SpatClient client = SpatClient.builder().config(config).build();
        client.getLogo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> processDialogService.show())
                .doOnError(tr -> Log.e(TAG, "getLogo error", tr))
                .doOnError(tr -> processDialogService.hide())
                .doOnError(dialogService::alert)
                .doOnNext(dto -> {
                    if (!dto.approved()) {
                        dialogService.alert(dto.getErrorMessage());
                    } else {
                        saveLogo(config.getMerId(), config.getRvcId(), dto.getData());
                        dialogService.alert(R.string.reload_logo_successful);
                    }
                })
                .doOnComplete(processDialogService::hide)
                .subscribe(new EmptyObserver<>());
    }

    @SuppressWarnings({"unused", "SameReturnValue"})
    private boolean onClickAbout(Preference preference) {
        ARouter.getInstance().build(RoutePath.ACTIVITY_ABOUT).navigation();
        return false;
    }

}
