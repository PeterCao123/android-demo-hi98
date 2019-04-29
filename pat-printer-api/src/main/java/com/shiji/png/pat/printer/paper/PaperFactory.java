package com.shiji.png.pat.printer.paper;

/**
 * @author bruce.wu
 * @since 2019/1/31 9:48
 */
public abstract class PaperFactory {

    public static Paper createDefaultPaper() {
        DefaultPaper paper = new DefaultPaper();
        paper.padding(8, 12);
        paper.lineSpacing(4);
        return paper;
    }

}
