package com.shiji.png.pat.printer.paper;

/**
 * @author bruce.wu
 * @since 2019/1/30 17:18
 */
public class DefaultPaper extends AbstractPaper {

    /**
     * printing pager width: 57mm/720px
     */
    private static final int PAPER_WIDTH = 720;

    public DefaultPaper() {
        this(PAPER_WIDTH);
    }

    public DefaultPaper(int width) {
        super(width);
    }
}
