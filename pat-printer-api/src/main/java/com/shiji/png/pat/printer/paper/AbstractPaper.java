package com.shiji.png.pat.printer.paper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.shiji.png.pat.printer.paper.model.DocumentStyle;
import com.shiji.png.pat.printer.paper.model.SplitMode;

import java.util.LinkedList;
import java.util.List;

/**
 * @author bruce.wu
 * @since 2019/1/30 17:08
 */
public abstract class AbstractPaper implements Paper {

    private static final int EMPTY_LINE_HEIGHT = 36;

    private int width;

    private int paddingLeft = 0;
    private int paddingRight = 0;
    private int paddingTop = 0;
    private int paddingBottom = 0;

    private int lineSpacing = 0;

    private int emptyLineHeight = EMPTY_LINE_HEIGHT;

    private final List<PrintRow> rows = new LinkedList<>();

    private transient DocumentStyle documentStyle = DocumentStyle.DEFAULT;

    public AbstractPaper(int width) {
        this.width = width;
    }

    @Override
    public Document apply(DocumentStyle style) {
        this.documentStyle = style;
        return this;
    }

    @Override
    public Document append(PrintRow row) {
        if (row == null) {
            throw new NullPointerException("row is null");
        }
        rows.add(row);
        return this;
    }

    @Override
    public Document append(String text) {
        if (text == null) {
            throw new NullPointerException("text is null");
        }
        TextRow row = new TextRow(text)
                .align(documentStyle.getAlign())
                .fontSize(documentStyle.getFontSize())
                .underline(documentStyle.isUnderline())
                .fontStyle(documentStyle.getFontStyle());
        rows.add(row);
        return this;
    }

    @Override
    public Document append(Bitmap bitmap) {
        if (bitmap == null) {
            throw new NullPointerException("bitmap is null");
        }
        ImageRow row = new ImageRow(bitmap, width - paddingLeft - paddingRight);
        rows.add(row);
        return this;
    }

    @Override
    public Document split(SplitMode mode) {
        return split(mode, SplitLine.DEFAULT_HEIGHT);
    }

    @Override
    public Document split(SplitMode mode, int height) {
        SplitLine row = new SplitLine().height(height).mode(mode);
        rows.add(row);
        return this;
    }

    @Override
    public Document appendEmptyLine(int lines) {
        for (int i = 0; i < lines; i++) {
            SplitLine row = new SplitLine().height(emptyLineHeight);
            rows.add(row);
        }
        return this;
    }

    @Override
    public Document appendEmptyLine() {
        return appendEmptyLine(1);
    }

    public AbstractPaper lineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
        return this;
    }

    public AbstractPaper emptyLineHeight(int height) {
        this.emptyLineHeight = height;
        return this;
    }

    public AbstractPaper padding(int padding) {
        return padding(padding, padding);
    }

    public AbstractPaper padding(int horizontal, int vertical) {
        paddingLeft(horizontal);
        paddingRight(horizontal);
        paddingTop(vertical);
        paddingBottom(vertical);
        return this;
    }

    public AbstractPaper paddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
        return this;
    }

    public AbstractPaper paddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
        return this;
    }

    public AbstractPaper paddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
        return this;
    }

    public AbstractPaper paddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
        return this;
    }

    @Override
    public int measureWidth() {
        return this.width;
    }

    @Override
    public int measureHeight() {
        int height = paddingTop + paddingBottom;
        if (rows.isEmpty()) {
            return height;
        }
        for (PrintRow row : rows) {
            height += row.measureHeight();
            height += lineSpacing;
        }
        return height - lineSpacing;
    }

    @Override
    public Rect getBounds() {
        return new Rect(0, 0, measureWidth(), measureHeight());
    }

    @Override
    public void draw(Canvas canvas, Rect bounds) {
        if (rows.isEmpty()) {
            return;
        }
        int left = paddingLeft + bounds.left;
        int right = bounds.right - paddingRight;
        int top = bounds.top + paddingTop;
        int bottom;

        for (PrintRow row : rows) {
            bottom = top + row.measureHeight();
            Rect rc = new Rect(left, top, right, bottom);
            row.draw(canvas, rc);
            top += rc.height() + lineSpacing;
        }
    }
}
