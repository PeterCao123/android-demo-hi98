package com.shiji.png.droid.icbc.simulator;

import com.shiji.png.droid.payment.ApplicationHolder;
import com.shiji.png.payment.annotation.ServiceResource;

/**
 * @author bruce.wu
 * @since 2018/11/22 14:03
 */
public class CardServiceResource implements ServiceResource {
    @Override
    public String getDisplayText() {
        return ApplicationHolder.app().getString(R.string.icbc_simulator_bank_card);
    }
}
