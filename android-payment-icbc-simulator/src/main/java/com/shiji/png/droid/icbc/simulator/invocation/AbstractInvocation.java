package com.shiji.png.droid.icbc.simulator.invocation;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;

import com.shiji.png.droid.payment.ApplicationHolder;
import com.shiji.png.payment.message.TxRequest;

/**
 * @author bruce.wu
 * @since 2018/11/22 14:21
 */
public abstract class AbstractInvocation implements Invocation {

    final TxRequest request;

    public AbstractInvocation(TxRequest request) {
        this.request = request;
    }

    abstract String getTransType();

    String getExtOrderNo() {
        String extOrderNo = request.getMerRef();
        return TextUtils.isEmpty(extOrderNo) ? "" : extOrderNo;
    }

    @Override
    public final long invoke() throws RemoteException {
        Bundle ctrlData = new Bundle();
        initCtrlData(ctrlData);
        Bundle transData = new Bundle();
        initTransData(transData);
        return 0;
    }

    void initCtrlData(Bundle bundle) {
        bundle.putString("APP_NAME", getAppName());
        bundle.putString("AIDL_VER", "V1.0.2");
        bundle.putString("EXT_ORDER_NO", getExtOrderNo());
    }

    void initTransData(Bundle bundle) {

    }

    private String getAppName() {
        Context context = ApplicationHolder.app();
        int identify = context.getResources().getIdentifier("app_name", "string", context.getPackageName());
        if (identify != 0) {
            return context.getString(identify);
        }
        return "Pay@Table";
    }

    static String getTransDate(String transDate) {
        return (transDate != null && transDate.length() >= 8) ? transDate.substring(0, 8) : transDate;
    }
}
