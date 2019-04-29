package com.shiji.png.payment.model;

/**
 * PNG Issuer codes
 *  See https://dev-confluence.shijicloud.com/display/PNG/Issuer+Codes
 *
 * @author bruce.wu
 * @date 2018/5/22
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public enum IssuerCode {

    Acquirer("00", "Acquirer"),
    Acquirer_Debit("01", "Acquirer Debit"),
    Acquirer_Credit("02", "Acquirer Credit"),

    /**
     * China Union Pay
     */
    CUP("10", "CUP"),
    CUP_Debit("11", "CUP Debit"),
    CUP_Credit("12", "CUP Credit"),

    Visa("20", "Visa"),
    Visa_Debit("21", "Visa Debit"),
    Visa_Credit("22", "Visa Credit"),
    OCBC_Visa("23", "OCBC VISA"),

    MasterCard("30", "MasterCard"),
    MasterCard_Debit("31", "MasterCard Debit"),
    MasterCard_Credit("32", "MasterCard Credit"),
    Mastero("33", "Mastero"),
    OCBC_MC("34", "OCBC MC"),

    /**
     * American Express
     */
    AE("00", "00"),
    AE_Debit("41", "AE Debit"),
    AE_Credit("42", "AE Credit"),

    /**
     * Dinners Club
     */
    DC("50", "DC"),
    DC_Debit("51", "DC Debit"),
    DC_Credit("52", "DC Credit"),

    JCB("60", "JCB"),
    JCB_Debit("61", "JCB Debit"),
    JCB_Credit("62", "JCB Credit"),

    Discover("70", "Discover"),
    Discover_Debit("71", "Discover Debit"),
    Discover_Credit("72", "Discover Credit"),

    Laser("75", "Laser"),

    Solo("76", "Solo"),

    Switch("77", "Switch"),

    Proprietary("78", "Proprietary"),

    PayPal("79", "PayPal"),

    EFTPOS("80", "EFTPOS"),

    Alipay("A0", "ALIPAY"),
    ALIPAY_INTERNATIONAL("A2", "ALIPAY_INTERNATIONAL"),
    BOC_ALIPAY("A5", "BOC_ALIPAY"),
    SMARTPAY_ALIPAY("A8", "SMARTPAY_ALIPAY"),
    ALIPAY_HONGKONG("A15", "ALIPAY_HONGKONG"),

    CANCAN("A11", "CANCAN"),

    ICBC_QR("A12", "ICBC_QR"),

    IPSPAY("A4", "IPSPAY"),

    JDPAY("A9", "JDPAY"),

    LINEPAY("A13", "LINEPAY"),

    LIQUIDPAY("A10", "LIQUIDPAY"),

    QQPAY("A3", "QQPAY"),

    WECHATPAY("A1", "WECHATPAY"),
    BOC_WECHATPAY("A6", "BOC_WECHATPAY"),
    WEEPAY_WECHATPAY("A7", "WEEPAY_WECHATPAY"),
    WECHATPAY_INTERNATIONAL("A14", "WECHATPAY_INTERNATIONAL"),

    Other("Zx", "Other");

    private final String code;
    private final String name;

    IssuerCode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return this.name;
    }

}
