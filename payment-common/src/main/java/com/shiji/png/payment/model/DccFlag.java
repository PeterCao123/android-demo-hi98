package com.shiji.png.payment.model;

/**
 * PNG DCC FLag
 *  See https://dev-confluence.shijicloud.com/display/PNG/DCC+Flag
 *
 * @author bruce.wu
 * @date 2018/5/22
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public enum DccFlag {

    DCC_NOT_OFFERED("0"),

    DCC_OFFERED("1"),

    DCC_YES("2"),

    DCC_NO("3");

    private String value;

    DccFlag(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
