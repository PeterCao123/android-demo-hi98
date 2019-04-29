package com.shiji.png.pat.printer.paper;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * @author bruce.wu
 * @since 2019/1/30 15:52
 */
public interface Paintable {

    void draw(Canvas canvas, Rect bounds);

}
