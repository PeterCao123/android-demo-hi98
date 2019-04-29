package com.shiji.png.payment.annotation;

/**
 * @author bruce.wu
 * @since 2018/11/15 10:36
 */
public enum ServiceType {

    /**
     * traditional bank card
     */
    BANK_CARD,

    /**
     * wechat merchant scan qr code
     */
    WX_M_SCAN,

    /**
     * wechat guest scan qr code
     */
    WX_G_SCAN,

    /**
     * alipay merchant scan qr code
     */
    ALI_M_SCAN,

    /**
     * alipay guest scan qr code
     */
    ALI_G_SCAN,

    /**
     * electronic wallet
     */
    E_WALLET,

    QR_CODE,

}
