package com.shiji.png.pinpad.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shiji.png.pinpad.agent.R;
import com.shiji.png.pinpad.model.ConfigurationModel;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.Currency;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class AgentSettingsActivity extends AppCompatPreferenceActivity {

    public static void show(Context context) {
        Intent intent = new Intent(context, AgentSettingsActivity.class);
        context.startActivity(intent);
    }

    private static final String TAG = "AgentSettingsActivity";

    private static Preference.OnPreferenceChangeListener preferenceListener = (preference, newValue) -> {
        Log.v(TAG, preference.getKey() + " => " + newValue);
        String value = newValue.toString();
        if (TextUtils.isEmpty(value)) {
            return false;
        }
        final Context context = preference.getContext();
        String key = preference.getKey();
        if (key.equals(context.getString(R.string.pref_key_currency_text))) {
            try {
                Currency.getInstance(value);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "invalid currency text: " + value);
                Toast.makeText(context, R.string.invalid_currency, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        preference.setSummary(value);
        return true;
    };

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(preferenceListener);
        preferenceListener.onPreferenceChange(preference,
                getSp(preference.getContext()).getString(preference.getKey(), ""));
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_agent_headers, target);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.agent_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onClickScanConfiguration();
        return super.onOptionsItemSelected(item);
    }

    /**
     * EsayPermissions接管权限处理逻辑
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, new EasyPermissions.PermissionCallbacks() {
            @Override
            public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
                onClickScanConfiguration();
            }

            @Override
            public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
                Toast.makeText(AgentSettingsActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (CodeUtils.REQUEST_CODE_SCAN == requestCode) {
            disposeCapture(data);
        }
    }

    private void disposeCapture(Intent data) {
        if (data != null && data.getIntExtra(CodeUtils.RESULT_TYPE, 0) == CodeUtils.RESULT_SUCCESS) {
            String result = data.getStringExtra(CodeUtils.RESULT_STRING);
            if (result != null) {
                Log.d(TAG, "QR data: " + result);
                doScanConfiguration(new Gson().fromJson(result, ConfigurationModel.class));
            }
        }
    }

    private void doScanConfiguration(ConfigurationModel config) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putString(getString(R.string.pref_key_mer_id), config.getMerId())
                .putString(getString(R.string.pref_key_ter_id), config.getTerId())
                .putString(getString(R.string.pref_key_currency_text), config.getCurrencyText())
                .putString(getString(R.string.pref_key_currency_precision), config.getCurrencyDecimal())
                .putString(getString(R.string.pref_key_merchant_name), config.getMerName())
                .putString(getString(R.string.pref_key_ws_server_url), config.getServerUrl())
                .putString(getString(R.string.pref_key_ws_client_id), config.getClientId())
                .putString(getString(R.string.pref_key_ws_secret_key), config.getSecretKey())
                .apply();
        this.recreate();
        Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show();
    }

    private void onClickScanConfiguration() {
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            EasyPermissions.requestPermissions(this, "Scan request Camera permission", 100, Manifest.permission.CAMERA);
            return;
        }
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, CodeUtils.REQUEST_CODE_SCAN);
    }

    private static void onClickScanConfiguration(Activity activity) {
        if (!EasyPermissions.hasPermissions(activity, Manifest.permission.CAMERA)) {
            EasyPermissions.requestPermissions(activity, "Scan request Camera permission", 100, Manifest.permission.CAMERA);
            return;
        }
        Intent intent = new Intent(activity, CaptureActivity.class);
        activity.startActivityForResult(intent, CodeUtils.REQUEST_CODE_SCAN);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || LinkClientPreferenceFragment.class.getName().equals(fragmentName);
    }

    private static SharedPreferences getSp(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            EasyPermissions.requestPermissions(this, "Scan request Camera permission", 100, Manifest.permission.CAMERA);
        }
    }

    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_agent_general);

            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_mer_id)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_ter_id)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_currency_text)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_currency_precision)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_merchant_name)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_test_mode)));

            findPreference("quick_config").setOnPreferenceClickListener(this::onClickQuickConfig);
            findPreference("reset").setOnPreferenceClickListener(this::onClickReset);
        }

        private boolean onClickQuickConfig(Preference preference) {
            AgentSettingsActivity.onClickScanConfiguration(getActivity());
            return false;
        }

        private boolean onClickReset(Preference preference) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            preferences.edit().remove(getString(R.string.pref_key_mer_id))
                    .remove(getString(R.string.pref_key_ter_id))
                    .remove(getString(R.string.pref_key_currency_text))
                    .remove(getString(R.string.pref_key_currency_precision))
                    .remove(getString(R.string.pref_key_merchant_name))
                    .remove(getString(R.string.pref_key_ws_server_url))
                    .remove(getString(R.string.pref_key_ws_client_id))
                    .remove(getString(R.string.pref_key_ws_secret_key))
                    .apply();
            getActivity().recreate();
            return false;
        }

    }

    public static class LinkClientPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_agent_linkclient);

            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_ws_server_url)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_ws_client_id)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_ws_secret_key)));
        }
    }

}
