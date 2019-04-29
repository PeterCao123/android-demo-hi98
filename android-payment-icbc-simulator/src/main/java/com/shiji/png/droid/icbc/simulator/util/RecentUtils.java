package com.shiji.png.droid.icbc.simulator.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shiji.png.droid.icbc.simulator.model.TransResponse;
import com.shiji.png.droid.icbc.simulator.model.TransResponseData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Falcon.cao
 * @date 2019/2/25
 */
public class RecentUtils {
    public static RecentUtils getUtils(Context context) {
        return new RecentUtils(context);
    }

    private RecentUtils(Context context) {
        preferences = context.getSharedPreferences("RecentTransaction", Context.MODE_PRIVATE);
        max = PreferenceManager.getDefaultSharedPreferences(context).getInt("MaxTransaction", 100);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String date = format.format(new Date());
        if (!date.equals(preferences.getString("date", ""))) {
            preferences.edit().putString("date", date).putString("transaction", "").apply();
            onInitResponseDatas("");
        } else {
            String caches = preferences.getString("transaction", "");
            onInitResponseDatas(caches);
        }
    }

    private void onInitResponseDatas(String caches) {
        if (!TextUtils.isEmpty(caches)) {
            TypeToken<List<TransResponseData>> type = new TypeToken<List<TransResponseData>>() {};
            transResponseDatas = new Gson().fromJson(caches, type.getType());
        }
        if (transResponseDatas == null) {
            transResponseDatas = new ArrayList<>();
        }
    }

    private SharedPreferences preferences;
    private List<TransResponseData> transResponseDatas;
    private int max = 100;

    public int getMax() {
        return max;
    }

    public List<TransResponseData> getResponseDatas(String origRefNo) {
        if (TextUtils.isEmpty(origRefNo)) {
            return transResponseDatas;
        }
        List<TransResponseData> models = new ArrayList<>();
        for (TransResponseData t : transResponseDatas) {
            if (origRefNo.equals(t.getRefNo())) {
                models.add(t);
            }
        }
        return models;
    }
    public TransResponseData getResponseData(String origRefNo) {
        if (TextUtils.isEmpty(origRefNo)) {
            return null;
        }
        if(transResponseDatas!=null) {
            TransResponseData t;
            for ( int i=0;i<transResponseDatas.size();i++) {
                t = transResponseDatas.get(i);
                if (origRefNo.equals(t.getRefNo())) {
                    return t;
                }
            }
        }
        return null;
    }
    public TransResponseData getResponseDataByAuthNo(String origAuthNo) {
        if (TextUtils.isEmpty(origAuthNo)) {
            return null;
        }
        if(transResponseDatas!=null) {
            TransResponseData t;
            for ( int i=0;i<transResponseDatas.size();i++) {
                t = transResponseDatas.get(i);
                if (origAuthNo.equals(t.getAuthId())) {
                    return t;
                }
            }
        }
        return null;
    }

//    public void paysPaymentModel(PaymentModel model, String merRef) {
//        for (PaymentModel pm : payments) {
//            if (merRef.equals(pm.getMerRef())) {
//                pm.setTxnStatus(2);
//            }
//        }
//        String table = merRef.split("-")[1];
//        model.setTxnStatus(0);
//        savePaymentModel(model, table);
//    }

    public void saveResponseData(TransResponseData model) {
        model.setTxnState(0);
        transResponseDatas.add(model);
        while (transResponseDatas.size() > max) {
            transResponseDatas.remove(0);
        }
        preferences.edit().putString("transaction", new Gson().toJson(transResponseDatas)).apply();
    }

    public void voidResponseData(TransResponseData model) {
        model.setTxnState(1);
        for (TransResponseData pm : transResponseDatas) {
            if (model.equals(pm)) {
                pm.setTxnState(model.getTxnState());
            }
        }
        preferences.edit().putString("transaction", new Gson().toJson(transResponseDatas)).apply();
    }

    public void initResponseData() {
        String caches = preferences.getString("transaction", "");
        onInitResponseDatas(caches);
    }
}
