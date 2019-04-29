package com.shiji.png.pat.printer.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.shiji.png.pat.printer.paper.Paper;

/**
 * @author bruce.wu
 * @since 2019/1/30 15:52
 */
public abstract class PaintUtils {

    public static Bitmap render(Paper paper) {
        Rect bounds = new Rect(0, 0, paper.measureWidth(), paper.measureHeight());
        Bitmap bitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        paper.draw(canvas, bounds);
        return bitmap;
    }

}
