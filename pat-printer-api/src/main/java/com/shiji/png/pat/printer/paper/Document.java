package com.shiji.png.pat.printer.paper;

import android.graphics.Bitmap;

import com.shiji.png.pat.printer.paper.model.DocumentStyle;
import com.shiji.png.pat.printer.paper.model.SplitMode;

/**
 * @author bruce.wu
 * @since 2019/1/30 15:53
 */
public interface Document {

    Document apply(DocumentStyle style);

    Document append(PrintRow row);

    Document append(String text);

    Document append(Bitmap bitmap);

    Document split(SplitMode mode);

    Document split(SplitMode mode, int height);

    Document appendEmptyLine(int lines);

    Document appendEmptyLine();

}
