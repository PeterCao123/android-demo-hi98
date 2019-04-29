package com.shiji.png.pinpad.ui;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.shiji.png.pinpad.agent.AgentConfig;
import com.shiji.png.pinpad.agent.MyService;
import com.shiji.png.pinpad.agent.R;
import com.shiji.png.pinpad.util.PinpadDeviceUtils;

import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AgentStateActivity extends AppCompatActivity {

    private TextView lblState;
    private TextView lblTiming;
    private TextView merchant_name;

    private ScheduledExecutorService scheduler;

    private String getMerchantName() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String currencyText = sp.getString("merchant_name", null);
        if (TextUtils.isEmpty(currencyText)) {
            return "";
        }
        return currencyText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_state);
        lblState = findViewById(R.id.lbl_state);
        lblTiming = findViewById(R.id.lbl_timing);
        merchant_name = findViewById(R.id.merchant_name);
        initVersion();
        initActionBar();


    }

    private void initVersion() {
        TextView lblVersion = findViewById(R.id.lbl_version);
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            lblVersion.setText("v" + packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException ignored) {

        }
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    protected void onPause() {
        scheduler.shutdown();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.agent_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        if (itemId == R.id.action_setting) {
            AgentSettingsActivity.show(this);
        } else if (itemId == R.id.action_start) {
            AgentConfig config = new AgentConfig(getApplicationContext());
            if (config.validate()) {
                MyService.start(this);
            } else {
                new AlertDialog.Builder(this)
                        .setMessage(R.string.invalid_config_settings)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> AgentSettingsActivity.show(this))
                        .show();
            }
        } else if (itemId == R.id.action_stop) {
            MyService.stop(this);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String merchantName = getMerchantName();
        merchant_name.setText(merchantName);
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::delayCheck, 50, 500, TimeUnit.MILLISECONDS);
    }

    private void delayCheck() {
        runOnUiThread(this::checkState);
    }

    private void checkState() {
        int state = MyService.getState();
        if (!PinpadDeviceUtils.isWifiConnected(this)) {
            state = MyService.STATE_STOPPED;
        }
        switch (state) {
            case MyService.STATE_RUNNING:
                lblState.setText(R.string.state_running);
                lblState.setTextColor(Color.GREEN);
                break;
            case MyService.STATE_STOPPED:
                lblState.setText(R.string.state_stopped);
                lblState.setTextColor(Color.RED);
                break;
        }
        updateTiming();
    }

    private void updateTiming() {
        long duration = MyService.getRunningTime();
        duration = duration / 1000;
        long h = duration / 3600;
        long m = duration % 3600 / 60;
        long s = duration % 60;
        String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", h, m, s);
        lblTiming.setText(time);
    }

}
