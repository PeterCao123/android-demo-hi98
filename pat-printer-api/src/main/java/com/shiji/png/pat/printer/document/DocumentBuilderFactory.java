package com.shiji.png.pat.printer.document;

import com.shiji.png.pat.printer.PrinterService;

/**
 * @author bruce.wu
 * @since 2019/1/30 18:29
 */
public abstract class DocumentBuilderFactory {

    public static DocumentBuilder create(int receiptFlag) {
        switch (receiptFlag) {
            case PrinterService.RECEIPT_FORMATTED:
                return new DocumentByContent();
            case PrinterService.RECEIPT_PRINT_LINES:
                return new DocumentByPrintLines();
            case PrinterService.RECEIPT_DETAILS:
                return new DocumentByDetail();
        }
        throw new IllegalArgumentException("Unknown receipt flag: " + receiptFlag);
    }

}
