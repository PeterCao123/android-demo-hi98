package com.shiji.png.pat.printer.paper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;

import com.shiji.png.pat.printer.paper.model.SplitMode;

/**
 * @author bruce.wu
 * @since 2019/1/30 16:42
 */
public class SplitLine extends PrintRow {

    public static final int DEFAULT_HEIGHT = 24;

    private int height = DEFAULT_HEIGHT;

    private SplitMode mode = SplitMode.Empty;

    public SplitLine height(int height) {
        this.height = height;
        return this;
    }

    public SplitLine mode(SplitMode mode) {
        this.mode = mode;
        return this;
    }

    @Override
    public int measureWidth() {
        return 1;
    }

    @Override
    public int measureHeight() {
        return height;
    }

    @Override
    public void draw(Canvas canvas, Rect bounds) {
        if (SplitMode.Empty == mode) {
            return;
        }
        canvas.drawLine(bounds.left, bounds.centerY(), bounds.right, bounds.centerY(), getPaint());
    }

    @Override
    protected void customizePaint(Paint paint) {
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        if (SplitMode.Dashed == mode) {
            paint.setPathEffect(new DashPathEffect(new float[] {8, 8}, 0));
        }
    }
}
