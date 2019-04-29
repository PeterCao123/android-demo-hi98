package com.shiji.png.pat.printer.paper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.shiji.png.pat.printer.paper.model.Align;
import com.shiji.png.pat.printer.paper.model.FontSize;
import com.shiji.png.pat.printer.paper.model.FontStyle;

/**
 * @author bruce.wu
 * @since 2019/1/30 16:50
 */
public class TextRow extends PrintRow {

    private static final int FONT_SIZE_SMALL = 24;

    private static final int FONT_SIZE_NORMAL = 36;

    private static final int FONT_SIZE_LARGE = 48;

    private static final String DEFAULT_FONT_FAMILY = "monospace";

    private final String text;
    private int fontSize = FONT_SIZE_NORMAL;
    private boolean underline = false;
    private Align align = Align.Left;
    private FontStyle fontStyle = FontStyle.Normal;

    public TextRow(String text) {
        this.text = text;
    }

    public TextRow fontSize(FontSize fontSize) {
        switch (fontSize) {
            case Normal:
                this.fontSize = FONT_SIZE_NORMAL;
                break;
            case Small:
                this.fontSize = FONT_SIZE_SMALL;
                break;
            case Large:
                this.fontSize = FONT_SIZE_LARGE;
                break;
        }
        return this;
    }

    public TextRow underline() {
        return underline(true);
    }

    public TextRow underline(boolean underline) {
        this.underline = underline;
        return this;
    }

    public TextRow align(Align align) {
        this.align = align;
        return this;
    }

    public TextRow fontStyle(FontStyle style) {
        this.fontStyle = style;
        return this;
    }

    @Override
    public int measureWidth() {
        Paint paint = getPaint();
        if (isEmpty()) {
            return (int) paint.measureText(" ");
        } else {
            return (int) paint.measureText(text);
        }
    }

    @Override
    public int measureHeight() {
        Paint paint = getPaint();
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        return fm.descent - fm.ascent;
    }

    @Override
    public void draw(Canvas canvas, Rect bounds) {
        if (isEmpty()) {
            return;
        }
        Paint paint = getPaint();
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        int x = getX(bounds, align);
        canvas.drawText(text, x, bounds.top + fm.leading - fm.ascent, paint);
    }

    @Override
    protected void customizePaint(Paint paint) {
        paint.setUnderlineText(this.underline);
        paint.setTextAlign(getAlign());
        paint.setTypeface(getTypeface());
        paint.setColor(Color.BLACK);
        paint.setTextSize(fontSize);
    }

    private boolean isEmpty() {
        return this.text == null || this.text.trim().isEmpty();
    }

    private int getX(Rect bounds, Align align) {
        switch (align) {
            case Left:
                return bounds.left;
            case Center:
                return bounds.centerX();
            case Right:
                return bounds.right;
            default:
                return bounds.left;
        }
    }

    private Paint.Align getAlign() {
        switch (align) {
            case Left:
                return Paint.Align.LEFT;
            case Center:
                return Paint.Align.CENTER;
            case Right:
                return Paint.Align.RIGHT;
            default:
                return Paint.Align.LEFT;

        }
    }

    private Typeface getTypeface() {
        switch (fontStyle) {
            case Normal:
                return Typeface.create(DEFAULT_FONT_FAMILY, Typeface.NORMAL);
            case Italic:
                return Typeface.create(DEFAULT_FONT_FAMILY, Typeface.ITALIC);
            case Bold:
                return Typeface.create(DEFAULT_FONT_FAMILY, Typeface.BOLD);
            default:
                return Typeface.create(DEFAULT_FONT_FAMILY, Typeface.NORMAL);
        }
    }

}
