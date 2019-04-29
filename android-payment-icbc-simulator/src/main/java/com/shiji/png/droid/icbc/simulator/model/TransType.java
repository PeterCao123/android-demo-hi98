package com.shiji.png.droid.icbc.simulator.model;

/**
 * @author bruce.wu
 * @since 2018/11/22 15:23
 */
public interface TransType {

    String SIGN_IN = "LOGON";

    String SALE = "MULTI_PURCHASE";

    String VOID_SALE = "POS_VOID";

    String REFUND = "REFUND";

    String SETTLE = "SETTLEMENT";

    String INQUERY = "INQUERY";

    String INTEGRAL_QUERY = "INTEGRAL_QUERY";

}
