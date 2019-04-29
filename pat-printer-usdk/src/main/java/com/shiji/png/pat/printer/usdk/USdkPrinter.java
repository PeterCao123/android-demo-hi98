package com.shiji.png.pat.printer.usdk;

import android.graphics.Bitmap;
import android.os.RemoteException;
import android.util.Log;

import com.shiji.png.pat.printer.PrinterException;
import com.shiji.png.pat.printer.paper.Document;
import com.shiji.png.pat.printer.paper.PrintRow;
import com.shiji.png.pat.printer.paper.SplitLine;
import com.shiji.png.pat.printer.paper.model.Align;
import com.shiji.png.pat.printer.paper.model.DocumentStyle;
import com.shiji.png.pat.printer.paper.model.FontSize;
import com.shiji.png.pat.printer.paper.model.SplitMode;
import com.usdk.apiservice.aidl.printer.ASCScale;
import com.usdk.apiservice.aidl.printer.ASCSize;
import com.usdk.apiservice.aidl.printer.AlignMode;
import com.usdk.apiservice.aidl.printer.HZScale;
import com.usdk.apiservice.aidl.printer.HZSize;
import com.usdk.apiservice.aidl.printer.OnPrintListener;
import com.usdk.apiservice.aidl.printer.PrintFormat;
import com.usdk.apiservice.aidl.printer.PrinterError;
import com.usdk.apiservice.aidl.printer.UPrinter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author bruce.wu
 * @since 2018/9/27
 */
public class USdkPrinter implements Document {

    private static final String TAG = "USdkPrinter";

    private static final int PAPER_WIDTH = 372;

    private final UPrinter printer;

    private int align = AlignMode.LEFT;

    USdkPrinter(UPrinter printer) {
        this.printer = printer;
        if (printer == null) {
            throw new NullPointerException("printer");
        }
        init();
    }

    private void init() {
        try {
            printer.setPrnGray(6);
            printer.setPrintFormat(PrintFormat.FORMAT_MOREDATAPROC, PrintFormat.VALUE_MOREDATAPROC_PRNTOEND);
        } catch (RemoteException e) {
            throw new PrinterException("init printer failed", e);
        }
    }

    public boolean isReady() {
        try {
            int status = printer.getStatus();
            Log.i(TAG, "printer status: " + status);
            return (status == PrinterError.SUCCESS);
        } catch (RemoteException e) {
            Log.e(TAG, "get printer status failed", e);
            return false;
        }
    }

    public void print() {
        applyFontSize(FontSize.Normal);
        appendEmptyLine(5);
        AtomicInteger code = new AtomicInteger(PrinterError.SUCCESS);
        try {
            CountDownLatch latch = new CountDownLatch(1);
            printer.startPrint(new OnPrintListener.Stub() {
                @Override
                public void onFinish() throws RemoteException {
                    Log.i(TAG, "print finish");
                    latch.countDown();
                }

                @Override
                public void onError(int i) throws RemoteException {
                    Log.e(TAG, "print error: " + i);
                    code.set(i);
                    latch.countDown();
                }
            });
            try {
                latch.await();
            } catch (InterruptedException e) {
                Log.e(TAG, "wait print interrupted", e);
                code.set(PrinterError.SERVICE_CRASH);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "start print failed", e);
            code.set(PrinterError.REQUEST_EXCEPTION);
        }

        if (code.get() != PrinterError.SUCCESS) {
            throw new PrinterException("print failed: " + code.get());
        }
    }

    @Override
    public Document apply(DocumentStyle style) {
        applyAlignMode(style.getAlign());
        applyFontSize(style.getFontSize());
        return this;
    }

    @Override
    public Document append(PrintRow row) {
        return this;
    }

    @Override
    public Document append(String text) {
        return append(this.align, text);
    }

    @Override
    public Document append(Bitmap bitmap) {
        return this;
    }

    @Override
    public Document split(SplitMode mode) {
        return split(mode, SplitLine.DEFAULT_HEIGHT);
    }

    @Override
    public Document split(SplitMode mode, int height) {
        append(AlignMode.CENTER, "--------------------------------");
        return this;
    }

    @Override
    public Document appendEmptyLine(int lines) {
        try {
            printer.feedLine(lines);
        } catch (RemoteException e) {
            Log.e(TAG, "feedLine(" + lines + ") error", e);
        }
        return this;
    }

    @Override
    public Document appendEmptyLine() {
        return appendEmptyLine(1);
    }

    private USdkPrinter append(int align, String text) {
        try {
            printer.addText(align, text);
        } catch (RemoteException e) {
            Log.e(TAG, "append text(" + text + ") failed", e);
        }
        return this;
    }

    private USdkPrinter fontStyle(int hzSize, int hzScale, int ascSize, int ascScale) {
        try {
            printer.setHzSize(hzSize);
            printer.setHzScale(hzScale);
            printer.setAscSize(ascSize);
            printer.setAscScale(ascScale);
        } catch (RemoteException e) {
            Log.e(TAG, "set font style failed", e);
        }
        return this;
    }

    private void applyAlignMode(Align align) {
        switch (align) {
            case Center:
                this.align = AlignMode.CENTER;
                break;
            case Right:
                this.align = AlignMode.RIGHT;
                break;
            case Left:
            default:
                this.align = AlignMode.LEFT;
                break;
        }
    }

    private void applyFontSize(FontSize fontSize) {
        switch (fontSize) {
            case Small:
                fontStyle(HZSize.DOT16x16, HZScale.SC1x1, ASCSize.DOT16x8, ASCScale.SC1x1);
                break;
            case Large:
                fontStyle(HZSize.DOT24x24, HZScale.SC1x2, ASCSize.DOT24x12, ASCScale.SC1x2);
                break;
            case Normal:
            default:
                fontStyle(HZSize.DOT24x24, HZScale.SC1x1, ASCSize.DOT24x12, ASCScale.SC1x1);
                break;

        }
    }

}
