package com.shiji.png.droid.icbc.simulator.util;

import android.graphics.Bitmap;

import com.hisense.pos.spiprinter.SpiPrinter;

import static com.hisense.pos.spiprinter.SpiPrinter.FONT_ATTR_BOLD;
import static com.hisense.pos.spiprinter.SpiPrinter.FONT_ATTR_NORMAL;
import static com.hisense.pos.spiprinter.SpiPrinter.PRINTER_OK;
import static com.hisense.pos.spiprinter.SpiPrinter.PRN_CENTER;
import static com.hisense.pos.spiprinter.SpiPrinter.PRN_LEFT;
import static com.hisense.pos.spiprinter.SpiPrinter.PRN_RIGHT;

/**
 * @author bruce.wu
 * @since 2019/4/16 13:17
 */
public final class HiPrinter {

    public static final int TEXT_SIZE_S = 0;
    public static final int TEXT_SIZE_M = 1;
    public static final int TEXT_SIZE_L = 2;
    public static final int TEXT_SIZE_XL = 3;
    public static final int TEXT_SIZE_XXL = 4;

    public static final int ALIGN_LEFT = PRN_LEFT;
    public static final int ALIGN_CENTER = PRN_CENTER;
    public static final int ALIGN_RIGHT = PRN_RIGHT;

    public static final int FONT_NORMAL = FONT_ATTR_NORMAL;
    public static final int FONT_BOLD = FONT_ATTR_BOLD;

    public static HiPrinter create() {
        return new HiPrinter();
    }

    private int textSize = TEXT_SIZE_M;

    private int align = ALIGN_LEFT;

    private int fontStyle = FONT_NORMAL;

    public HiPrinter textSize(int textSize) {
        this.textSize = textSize;
        return this;
    }

    public HiPrinter align(int align) {
        this.align = align;
        return this;
    }

    public HiPrinter fontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
        return this;
    }

    public HiPrinter reset() {
        this.textSize = TEXT_SIZE_M;
        this.align = ALIGN_LEFT;
        this.fontStyle = FONT_NORMAL;
        return this;
    }

    public HiPrinter append(String text) {
        printer.Printer_TextStr(text, textSize, fontStyle, align);
        return this;
    }

    public HiPrinter append(Bitmap bitmap) {
        printer.Printer_Image(bitmap, align);
        return this;
    }

    public HiPrinter appendEmptyLine() {
        return appendEmptyLine(1);
    }

    public HiPrinter appendEmptyLine(int n) {
        for (int i = 0; i < n; i++) {
            printer.Printer_TextStr(" ", TEXT_SIZE_M, FONT_NORMAL, ALIGN_LEFT);
        }
        return this;
    }

    public HiPrinter feed(int n) {
        for (int i = 0; i < n; i++) {
            printer.Printer_feedPaper(n, 0);
        }
        return this;
    }

    public HiPrinter feed() {
        return feed(1);
    }

    public HiPrinter split() {
        printer.Printer_TextStr("--------------------------------", TEXT_SIZE_M, FONT_NORMAL, ALIGN_CENTER);
        return this;
    }

    public HiPrinter print() {
        printer.Printer_Start();
        printer.Printer_cutPaper();
        return this;
    }

    private final SpiPrinter printer;

    private HiPrinter() {
        printer = new SpiPrinter();
        init();
    }

    private void init() {
        int status = printer.Printer_init();
        if (status != PRINTER_OK) {
            throw new IllegalStateException("printer is not ready");
        }
        printer.Printer_setGray(6);
    }

}
