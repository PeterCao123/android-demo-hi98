package com.shiji.png.payment.model;

/**
 * PNG Entry Mode
 *
 * See https://dev-confluence.shijicloud.com/display/PNG/Entry+Mode
 *
 * @author bruce.wu
 * @date 2018/5/15
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public enum EntryMode {

    CHIP_CARD("0"),

    SWIP_CARD("1"),

    CARD_NOT_PRESENT("2"),

    QR("3"),

    NFC("4"),

    ECOMMERCE("5"),

    FALLBACK("6"),

    MANUAL_INPUT("7"),

    UNKNOWN("99");

    private String value;

    EntryMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getSpaValue() {
        switch (value) {
            case "1": return "S";
            case "7": return "M";
            case "0": return "C";
            case "6": return "F";
            case "4": return "Q";
            default: return "M";
        }
    }

}
