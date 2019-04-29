package com.shiji.png.pat.printer.document;

import com.shiji.png.pat.printer.paper.model.FontSize;

import java.util.List;

/**
 * @author bruce.wu
 * @since 2019/1/31 13:11
 */
public abstract class DocumentUtils {

    public static FontSize determineFontSize(List<String> lines) {
        for (String line : lines) {
            if (line != null && line.length() > 32) {
                return FontSize.Small;
            }
        }
        return FontSize.Normal;
    }

}
