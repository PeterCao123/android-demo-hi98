package com.shiji.png.pat.printer.paper;

import android.graphics.Rect;

/**
 * @author bruce.wu
 * @since 2019/1/30 15:53
 */
public interface Measurable {

    int measureWidth();

    int measureHeight();

    Rect getBounds();

}
