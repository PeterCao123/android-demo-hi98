package com.shiji.png.droid.icbc.simulator.util;


import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

public class StringUtils {
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
    public static boolean isValidDate(String date, String fromat) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(fromat);
            dateFormat.setLenient(false);
            dateFormat.parse(date);
            return true;
        } catch (Exception var3) {
            return false;
        }
    }
    private static int getCountsInString(String s,char c){
        char[] cha=s.toCharArray();
        int a=0;
        for (int i = 0; i < cha.length; i++) {
            if(cha[i] == c){
                a++;
            }
        }
        return a;
    }
    public static String onGenerateShilds(String pan) {
        if (pan == null || pan.length() < 10) {
            return pan;
        }
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < pan.length(); index++) {
            if (index >= 6 && index < pan.length() - 4) {
                if(getCountsInString(pan, '*')>=4){
                    continue;
                }
                builder.append("*");
            } else {
                builder.append(pan.charAt(index));
            }
        }
        return builder.toString();
    }
    public static String onGeneratingDate(long delay) {
        Date date = new Date(System.currentTimeMillis() + delay);
        return new SimpleDateFormat("yyyyMMddHHmmSS", Locale.getDefault()).format(date);
    }
    public static String onGenerateRandom(int length) {
        StringBuilder builder = new StringBuilder();
        Random rm = new Random();
        for (int index = 0; index < length; index++) {
            builder.append(rm.nextInt(10));
        }
        return builder.toString();
    }





    public static boolean validAmount(String amt) {
        if (TextUtils.isEmpty(amt)) {
            return false;
        }
        amt = amt.replaceAll(",", "");
        amt = amt.replace(".", "");
        try {
            long amtLong = Long.parseLong(amt);
            return amtLong > 0;
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * 格式化金额，long -> display; decimal -> display; display -> display
     * 如RMB 000001234567 -> 12,345.67   123456789->1,234,567.89
     * 日元  000000001234 -> 1,234
     */
    public static String toDisplayAmount(String amount) {
        return formatAmount(",##0", amount, 2);
    }

    /**
     * 格式化金额，long -> display; decimal -> display
     * 如RMB 000001234567 -> 12,345.67   123456789->1,234,567.89  1234.56 -> 1,234.56
     * 日元  000000001234 -> 1,234
     */
    public static String toDisplayAmount(String amount, int decimalNum) {
        return formatAmount(",##0", amount, decimalNum);
    }

    /**
     * 格式化金额，long -> decimal; display -> decimal
     * 如RMB 000000001234 -> 12.34   1234->12.34
     * 日元  000000001234 -> 1234
     */
    public static String toDecimalAmount(String amount) {
        return toDecimalAmount(amount, 2);
    }

    /**
     * 格式化金额，long -> decimal; display -> decimal
     * 如RMB 000000001234 -> 12.34   1234->12.34
     * 日元  000000001234 -> 1234
     */
    public static String toDecimalAmount(String amount, int decimalNum) {
        return formatAmount("0", amount, decimalNum);
    }
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }
    private static String formatAmount(String format, String amount, int decimalNum) {
        if (amount!=null&&!TextUtils.isEmpty(amount)) {
            amount = amount.replaceAll(",", "");
        }
        boolean isLong = false;
        Double amountDouble = 0.00;
        try {
            Long amountLong = Long.parseLong(amount)*100 ;
            amountDouble = amountLong / Math.pow(10, decimalNum);
            isLong = true;
        } catch (Exception ignored) {
        }
        if (!isLong) {
            try {
                amountDouble = Double.parseDouble(amount);
            } catch (Exception ignored) {
            }
        }
        return new DecimalFormat("0.00").format(amountDouble);
    }

}
