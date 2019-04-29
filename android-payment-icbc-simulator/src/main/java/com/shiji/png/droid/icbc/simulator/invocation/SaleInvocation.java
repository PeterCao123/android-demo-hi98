package com.shiji.png.droid.icbc.simulator.invocation;

import android.os.Bundle;

import com.shiji.png.droid.icbc.simulator.model.TransType;
import com.shiji.png.droid.icbc.simulator.util.AmtUtils;
import com.shiji.png.payment.message.TxRequest;

/**
 * @author bruce.wu
 * @since 2018/11/22 15:53
 */
public class SaleInvocation extends AbstractInvocation {
    public SaleInvocation(TxRequest request) {
        super(request);
    }

    @Override
    String getTransType() {
        return TransType.SALE;
    }

    @Override
    void initTransData(Bundle bundle) {
        bundle.putLong("AMOUNT", AmtUtils.y2c(request.getTotalAmt()));
    }

}
