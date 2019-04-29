package com.shiji.png.pat.spat.dto;

/**
 * @author bruce.wu
 * @date 2018/9/4
 */
public final class RespCode {

    public static final String Approve = "00";

    public static final String TableLocker = "TL";

    public static String getRespText(String code) {
        switch (code.toUpperCase()) {
            case Approve:
                return "Approve";
            default:
                return "Undefined code";
        }
    }

}
