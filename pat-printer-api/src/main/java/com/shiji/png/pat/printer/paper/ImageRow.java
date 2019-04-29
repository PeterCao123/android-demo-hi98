package com.shiji.png.pat.printer.paper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * @author bruce.wu
 * @since 2019/1/30 16:28
 */
public class ImageRow extends PrintRow {

    private final Bitmap bitmap;

    private final int width;

    private final int height;

    public ImageRow(Bitmap bitmap) {
        this(bitmap, bitmap.getWidth());
    }

    public ImageRow(Bitmap bitmap, int maxWidth) {
        this.bitmap = bitmap;
        boolean zoomOut = bitmap.getWidth() > maxWidth;
        if (zoomOut) {
            this.width = maxWidth;
            this.height = maxWidth * bitmap.getHeight() / bitmap.getWidth();
        } else {
            this.width = bitmap.getWidth();
            this.height = bitmap.getHeight();
        }
    }

    @Override
    public int measureWidth() {
        return this.width;
    }

    @Override
    public int measureHeight() {
        return this.height;
    }

    @Override
    public void draw(Canvas canvas, Rect bounds) {
        Rect src = new Rect(0, 0, measureWidth(), measureHeight());
        Rect dst = new Rect(src);
        dst.offset(bounds.centerX() - src.centerX(), bounds.top);
        canvas.drawBitmap(bitmap, src, dst, getPaint());
    }

}
