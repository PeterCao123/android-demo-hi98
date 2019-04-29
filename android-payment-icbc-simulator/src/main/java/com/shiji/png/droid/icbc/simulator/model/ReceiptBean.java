package com.shiji.png.droid.icbc.simulator.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Builder;
import lombok.Getter;

/**
 * @author bruce.wu
 * @since 2019/4/16 14:41
 */
@Getter
@Builder
public final class ReceiptBean {

    private final String entryMode;

    private final String pan;

    @Builder.Default
    private final String traceNo = newTraceNo();

    @Builder.Default
    private final String authCode = newAuthCode();

    @Builder.Default
    private final String refNo = newRefNo();

    @Builder.Default
    private final String datetime = now();

    @Builder.Default
    private final String merchantNo = "100000001";

    @Builder.Default
    private final String terminalNo = "201";

    @Builder.Default
    private final String batchNo = "000002";

    public String getCardNo() {
        String cardNo = pan;
        if (pan == null || pan.length() < 12) {
            cardNo = "0000000000";
        }
        return cardNo.substring(0, 6)
                + "******"
                + cardNo.substring(cardNo.length() - 4)
                + " (" + entryMode + ")";
    }

    public String getCardType() {
        if (pan == null || pan.length() == 0) {
            return "";
        }
        if (pan.startsWith("62")) {
            return "CUP";
        }
        if (pan.startsWith("4903") || pan.startsWith("4905") || pan.startsWith("4911") || pan.startsWith("4936")
                || pan.startsWith("564182")|| pan.startsWith("633110")|| pan.startsWith("6333")|| pan.startsWith("6759")) {
            return "Switch";
        }
        if (pan.startsWith("4")) {
            return "Visa";
        }
        if (pan.startsWith("34") || pan.startsWith("37")) {
            return "AMEX";
        }
        if (pan.startsWith("6304") || pan.startsWith("6706") || pan.startsWith("6771") || pan.startsWith("6709")) {
            return "Laser";
        }
        int n2 = Integer.valueOf(pan.substring(0, 2));
        int n3 = Integer.valueOf(pan.substring(0, 3));
        int n4 = Integer.valueOf(pan.substring(0, 4));
        if (n3 >= 300 && n3 <= 305) {
            return "Diners";
        }
        if (pan.startsWith("2014") || pan.startsWith("2149")) {
            return "Diners";
        }
        if (pan.startsWith("36")) {
            return "Diners";
        }
        if (pan.startsWith("54") || pan.startsWith("55")) {
            return "Diners";
        }
        if (n4 >= 3528 && n4 <= 3589) {
            return "JCB";
        }
        if ((n4 >= 2221 && n4 <= 2720) || (n2 >= 51 && n2 <= 55)) {
            return "MasterCard";
        }
        if (pan.startsWith("6334") || pan.startsWith("6767")) {
            return "Solo";
        }
        if (pan.startsWith("5610") || pan.startsWith("560221") || pan.startsWith("560222")
                || pan.startsWith("560223") || pan.startsWith("560224") || pan.startsWith("560225")) {
            return "Bankcard";
        }
        if (pan.startsWith("50") || (n2 >= 56 && n2 <= 58) || pan.startsWith("6")) {
            return "Maestro";
        }
        if (pan.startsWith("1")) {
            return "UATP";
        }
        return "";
    }

    private static String now() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
                .format(new Date());
    }

    private static final AtomicInteger traceNoSeq = new AtomicInteger(0);

    private static String newTraceNo() {
        int seq = traceNoSeq.getAndIncrement() % 999999;
        return String.format(Locale.getDefault(), "%06d", seq);
    }

    private static String newAuthCode() {
        final String candidates = "0123456789";
        Random r = new Random();
        String s = "";
        s += candidates.charAt(r.nextInt(candidates.length()));
        s += candidates.charAt(r.nextInt(candidates.length()));
        s += candidates.charAt(r.nextInt(candidates.length()));
        s += candidates.charAt(r.nextInt(candidates.length()));
        s += candidates.charAt(r.nextInt(candidates.length()));
        s += candidates.charAt(r.nextInt(candidates.length()));
        return s;
    }

    private static String newRefNo() {
        final String candidates = "0123456789";
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append(candidates.charAt(r.nextInt(candidates.length())));
        }
        return sb.toString();
    }

}
