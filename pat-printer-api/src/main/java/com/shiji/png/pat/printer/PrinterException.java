package com.shiji.png.pat.printer;

/**
 * @author bruce.wu
 * @since 2019/1/31 9:56
 */
public class PrinterException extends RuntimeException {
    public PrinterException(String message) {
        super(message);
    }

    public PrinterException(String message, Throwable cause) {
        super(message, cause);
    }
}
