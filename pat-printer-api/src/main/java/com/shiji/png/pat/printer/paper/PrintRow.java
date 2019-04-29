package com.shiji.png.pat.printer.paper;

import android.graphics.Paint;
import android.graphics.Rect;

/**
 * @author bruce.wu
 * @since 2019/1/30 16:27
 */
public abstract class PrintRow implements Measurable, Paintable {

    @Override
    public Rect getBounds() {
        return new Rect(0, 0, measureWidth(), measureHeight());
    }

    Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        customizePaint(paint);
        return paint;
    }

    protected void customizePaint(Paint paint) {}

}
