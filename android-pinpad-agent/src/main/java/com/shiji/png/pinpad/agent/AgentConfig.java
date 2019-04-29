package com.shiji.png.pinpad.agent;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.shiji.png.sdk.java.link.clientSDK.config.WsConfig;

import lombok.Getter;

/**
 * @author bruce.wu
 * @since 2018/11/26 16:25
 */
@Getter
public final class AgentConfig {

    private final String currencyText;

    private final String merId;

    private final String terId;

    private final String wsServerUrl;

    private final String wsClientId;

    private final String wsSecretKey;

    public AgentConfig(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        this.merId = sp.getString(key(context, R.string.pref_key_mer_id), null);
        this.terId = sp.getString(key(context, R.string.pref_key_ter_id), null);
        this.currencyText = sp.getString(key(context, R.string.pref_key_currency_text), null);

        this.wsServerUrl = sp.getString(key(context, R.string.pref_key_ws_server_url), null);
        this.wsClientId = sp.getString(key(context, R.string.pref_key_ws_client_id), null);
        this.wsSecretKey = sp.getString(key(context, R.string.pref_key_ws_secret_key), null);
    }

    private static String key(Context context, @StringRes int resId) {
        return context.getString(resId);
    }

    public WsConfig getWsConfig() {
        return WsConfig.builder()
                .serverUrl(wsServerUrl)
                .clientId(wsClientId)
                .secretKey(wsSecretKey)
                .build();
    }

    public boolean validate() {
        if (currencyText == null || currencyText.isEmpty()) {
            return false;
        }
        if (TextUtils.isEmpty(wsServerUrl)) {
            return false;
        }
        if (TextUtils.isEmpty(wsClientId)) {
            return false;
        }
        if (TextUtils.isEmpty(wsSecretKey)) {
            return false;
        }
        return true;
    }

}
