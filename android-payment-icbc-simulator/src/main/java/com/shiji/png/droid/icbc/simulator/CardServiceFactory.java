package com.shiji.png.droid.icbc.simulator;

import com.shiji.png.payment.PaymentService;
import com.shiji.png.payment.ServiceFactory;

/**
 * @author bruce.wu
 * @since 2018/11/22 14:02
 */
public class CardServiceFactory implements ServiceFactory {
    @Override
    public PaymentService create() {
        return new CardPaymentService();
    }
}
