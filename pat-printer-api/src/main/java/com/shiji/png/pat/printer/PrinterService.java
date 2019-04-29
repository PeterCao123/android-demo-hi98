package com.shiji.png.pat.printer;

import android.graphics.Bitmap;

import com.shiji.png.pat.printer.model.PrintingCheck;

import java.util.Currency;

/**
 * @author bruce.wu
 * @since 2019/1/30 16:07
 */
public interface PrinterService {

    int RECEIPT_DETAILS = 0;

    int RECEIPT_FORMATTED = 1;

    int RECEIPT_PRINT_LINES = 2;

    Bitmap preview(int receiptFlag, PrintingCheck check, Currency currency);

    void print(int receiptFlag, PrintingCheck check, Currency currency);

}
