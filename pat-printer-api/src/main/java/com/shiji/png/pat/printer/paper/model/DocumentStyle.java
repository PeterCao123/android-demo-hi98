package com.shiji.png.pat.printer.paper.model;

import lombok.Getter;

/**
 * @author bruce.wu
 * @since 2019/1/31 9:16
 */
@Getter
public class DocumentStyle {

    public static final DocumentStyle DEFAULT = new DocumentStyle();

    private Align align = Align.Left;

    private FontSize fontSize = FontSize.Normal;

    private FontStyle fontStyle = FontStyle.Normal;

    private boolean underline = false;

    private DocumentStyle align(Align align) {
        this.align = align;
        return this;
    }

    public DocumentStyle alignLeft() {
        return align(Align.Left);
    }

    public DocumentStyle alignCenter() {
        return align(Align.Center);
    }

    public DocumentStyle alignRight() {
        return align(Align.Right);
    }

    public DocumentStyle fontSize(FontSize fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public DocumentStyle fontSizeSmall() {
        return fontSize(FontSize.Small);
    }

    public DocumentStyle fontSizeNormal() {
        return fontSize(FontSize.Normal);
    }

    public DocumentStyle fontSizeLarge() {
        return fontSize(FontSize.Large);
    }

    private DocumentStyle fontStyle(FontStyle fontStyle) {
        this.fontStyle = fontStyle;
        return this;
    }

    public DocumentStyle fontStyleNormal() {
        return fontStyle(FontStyle.Normal);
    }

    public DocumentStyle fontStyleItalic() {
        return fontStyle(FontStyle.Italic);
    }

    public DocumentStyle fontStyleBold() {
        return fontStyle(FontStyle.Bold);
    }

    public DocumentStyle underline() {
        this.underline = true;
        return this;
    }

}
