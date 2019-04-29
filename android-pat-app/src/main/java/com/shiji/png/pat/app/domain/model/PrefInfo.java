package com.shiji.png.pat.app.domain.model;

import android.text.TextUtils;
import android.util.Log;

import com.shiji.png.pat.app.domain.service.AmountService;
import com.shiji.png.pat.app.storage.Default;
import com.shiji.png.pat.app.storage.Key;
import com.shiji.png.pat.spat.model.ReceiptFlag;
import com.shiji.png.pat.spat.service.ApiConfig;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

/**
 * @author bruce.wu
 * @date 2018/9/4
 */
public class PrefInfo {

    private static final String TAG = "PrefInfo";

    @Key(name = "enable_surcharge")
    @Default("true")
    private boolean enableSurcharge;

    @Key(name = "surcharge_rate")
    @Default("1.08")
    private String surchargeRate;

    @Key(name = "tip_rate")
    @Default("5|10|12.5|15")
    private String tipRate;

    @Key(name = "max_tip_amount")
    @Default("1000")
    private String maxTipAmount;

    @Key(name = "currency_code")
    @Default("AUD")
    private String currencyCode;

    @Key(name = "receipt_type")
    @Default("0")
    private String receiptType;

    @Key(name = "mer_id")
    private String merId;

    @Key(name = "rvc_id")
    private String rvcId;

    @Key(name = "ter_id")
    private String terId;

    @Key(name = "pos_mer_id")
    private String posMerId;

    @Key(name = "pos_rvc_id")
    private String posRvcId;

    @Key(name = "api_gateway")
    @Default("https://png-dev-api-pat-cn01.shijicloud.com/sps-pat/api/V1/")
    private String apiGateway;

    @Key(name = "api_user")
    private String apiUser;

    @Key(name = "api_bearer")
    private String apiBearer;

    @Key(name = "api_password")
    private String apiPassword;

    @Key(name = "api_connect_timeout")
    @Default("8")
    private String apiConnectTimeout;

    @Key(name = "api_read_timeout")
    @Default("50")
    private String apiReadTimeout;

    public boolean isEnableSurcharge() {
        return enableSurcharge;
    }

    public String getSurchargeRate() {
        return surchargeRate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getMerId() {
        return merId;
    }

    public String getRvcId() {
        return rvcId;
    }

    public String getTerId() {
        return terId;
    }

    public String getPosMerId() {
        return posMerId;
    }

    public String getPosRvcId() {
        return posRvcId;
    }

    public String getApiGateway() {
        return apiGateway;
    }

    public String getApiUser() {
        return apiUser;
    }

    public String getApiBearer() {
        return apiBearer;
    }

    public String getApiPassword() {
        return apiPassword;
    }

    public Currency getCurrency() {
        return Currency.getInstance(currencyCode);
    }

    public int getReceiptTypeAsInt() {
        try {
            return Integer.valueOf(receiptType);
        } catch (NumberFormatException e) {
            Log.e(TAG, "invalid receipt type: " + receiptType, e);
            return ReceiptFlag.CheckDetails;
        }
    }

    public double surchargeRateAsDouble() {
        return enableSurcharge ? Double.valueOf(surchargeRate) : 0D;
    }

    public Double[] tipRateAsArray() {
        String[] items = tipRate.split("[|]");
        List<Double> rates = new ArrayList<>(items.length);
        for (String item: items) {
            if (TextUtils.isEmpty(item)) {
                continue;
            }
            try {
                rates.add(Double.valueOf(item));
            } catch (NumberFormatException e) {
                Log.e(TAG, "invalid tip rate: " + item);
            }
        }
        return rates.toArray(new Double[0]);
    }

    public BigDecimal maxTipAmount() {
        return AmountService.toAmount(maxTipAmount);
    }

    private int apiConnectTimeout() {
        try {
            return Integer.valueOf(apiConnectTimeout);
        } catch (NumberFormatException e) {
            Log.e(TAG, "invalid api connect timeout: " + apiConnectTimeout, e);
            return 0;
        }
    }

    private int apiReadTimeout() {
        try {
            return Integer.valueOf(apiReadTimeout);
        } catch (NumberFormatException e) {
            Log.e(TAG, "invalid api read timeout: " + apiReadTimeout, e);
            return 0;
        }
    }

    public ApiConfig getApiConfig() {
        return ApiConfig.builder()
                .merId(merId)
                .rvcId(rvcId)
                .terId(terId)
                .posMerId(posMerId)
                .posRvcId(posRvcId)
                .gateway(apiGateway)
                .user(apiUser)
                .signature(apiBearer)
                .password(apiPassword)
                .connectTimeout(apiConnectTimeout() * 1000)
                .readTimeout(apiReadTimeout() * 1000)
                .build();
    }

}
