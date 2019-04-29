package com.shiji.png.droid.icbc.simulator.model;

import com.shiji.png.payment.model.EntryMode;
import com.shiji.png.payment.model.IssuerCode;

/**
 * @author bruce.wu
 * @since 2018/11/23 9:57
 */
public final class Converter {

    public static String getCurrencyText(String currencyCode) {
        if (currencyCode != null && isDigitsOnly(currencyCode)) {
            switch (currencyCode) {
            case "446":
                return "MOP";
            case "156":
                return "CNY";
            default:
                return currencyCode;
            }
        }
        return currencyCode;
    }

    public static String getCurrencyCode(String currencyText) {
        if (currencyText == null || isDigitsOnly(currencyText)) {
            return currencyText;
        }
        switch (currencyText.toUpperCase()) {
        case "MOP":
            return "446";
        case "CNY":
            return "156";
        default:
            return currencyText;
        }
    }

    public static String getCardType(IssuerCode code) {
        try {
            if (IssuerCode.Alipay == code) {
                return "支付宝";
            }
            if (IssuerCode.WECHATPAY == code) {
                return "微信支付";
            }
        } catch (Exception e) {
            // Nothing needs to do
        }
        return code.getName();
    }

    public static IssuerCode getIssuerCode(String issuer) {
        if (IssuerCode.Acquirer.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.Acquirer;
        }
        if (IssuerCode.Acquirer_Debit.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.Acquirer_Debit;
        }
        if (IssuerCode.Acquirer_Credit.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.Acquirer_Credit;
        }
        if (IssuerCode.CUP.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.CUP;
        }
        if (IssuerCode.CUP_Debit.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.CUP_Debit;
        }
        if (IssuerCode.CUP_Credit.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.CUP_Credit;
        }
        if (IssuerCode.Visa.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.Visa;
        }
        if (IssuerCode.Visa_Debit.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.Visa_Debit;
        }
        if (IssuerCode.Visa_Credit.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.Visa_Credit;
        }
        if (IssuerCode.OCBC_Visa.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.OCBC_Visa;
        }
        if (IssuerCode.MasterCard.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.MasterCard;
        }
        if (IssuerCode.MasterCard_Debit.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.MasterCard_Debit;
        }
        if (IssuerCode.MasterCard_Credit.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.MasterCard_Credit;
        }
        if (IssuerCode.Mastero.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.Mastero;
        }
        if (IssuerCode.OCBC_MC.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.OCBC_MC;
        }
        if (IssuerCode.AE.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.AE;
        }
        if (IssuerCode.AE_Debit.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.AE_Debit;
        }
        if (IssuerCode.AE_Credit.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.AE_Credit;
        }
        if (IssuerCode.DC.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.DC;
        }
        if (IssuerCode.DC_Debit.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.DC_Debit;
        }
        if (IssuerCode.DC_Credit.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.DC_Credit;
        }
        if (IssuerCode.JCB.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.JCB;
        }
        if (IssuerCode.JCB_Debit.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.JCB_Debit;
        }
        if (IssuerCode.JCB_Credit.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.JCB_Credit;
        }
        if (IssuerCode.Discover.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.Discover;
        }
        if (IssuerCode.Discover_Debit.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.Discover_Debit;
        }
        if (IssuerCode.Discover_Credit.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.Discover_Credit;
        }
        if (IssuerCode.Laser.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.Laser;
        }
        if (IssuerCode.Solo.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.Solo;
        }
        if (IssuerCode.Switch.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.Switch;
        }
        if (IssuerCode.Proprietary.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.Proprietary;
        }
        if (IssuerCode.PayPal.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.PayPal;
        }
        if (IssuerCode.EFTPOS.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.EFTPOS;
        }
        if (IssuerCode.Alipay.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.Alipay;
        }
        if (IssuerCode.ALIPAY_INTERNATIONAL.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.ALIPAY_INTERNATIONAL;
        }
        if (IssuerCode.BOC_ALIPAY.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.BOC_ALIPAY;
        }
        if (IssuerCode.SMARTPAY_ALIPAY.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.SMARTPAY_ALIPAY;
        }
        if (IssuerCode.ALIPAY_HONGKONG.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.ALIPAY_HONGKONG;
        }
        if (IssuerCode.CANCAN.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.CANCAN;
        }
        if (IssuerCode.ICBC_QR.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.ICBC_QR;
        }
        if (IssuerCode.IPSPAY.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.IPSPAY;
        }
        if (IssuerCode.JDPAY.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.JDPAY;
        }
        if (IssuerCode.LINEPAY.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.LINEPAY;
        }
        if (IssuerCode.LIQUIDPAY.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.LIQUIDPAY;
        }
        if (IssuerCode.QQPAY.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.QQPAY;
        }
        if (IssuerCode.WECHATPAY.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.WECHATPAY;
        }
        if (IssuerCode.BOC_WECHATPAY.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.BOC_WECHATPAY;
        }
        if (IssuerCode.WEEPAY_WECHATPAY.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.WEEPAY_WECHATPAY;
        }
        if (IssuerCode.WECHATPAY_INTERNATIONAL.getName().equalsIgnoreCase(issuer)) {
            return IssuerCode.WECHATPAY_INTERNATIONAL;
        }
        return IssuerCode.Other;
    }

    public static EntryMode getEntryMode(Long inputsMode) {
        if (inputsMode == null) {
            return EntryMode.UNKNOWN;
        }
        switch (inputsMode.intValue()) {
        case 1:
            return EntryMode.SWIP_CARD;
        case 2:
            return EntryMode.CHIP_CARD;
        case 3:
            return EntryMode.NFC;
        case 4:
            return EntryMode.MANUAL_INPUT;
        case 5:
            return EntryMode.QR;
        default:
            return EntryMode.UNKNOWN;
        }
    }

    /**
     * Returns whether the given CharSequence contains only digits.
     */
    private static boolean isDigitsOnly(CharSequence str) {
        final int len = str.length();
        for (int cp, i = 0; i < len; i += Character.charCount(cp)) {
            cp = Character.codePointAt(str, i);
            if (!Character.isDigit(cp)) {
                return false;
            }
        }
        return true;
    }
}
