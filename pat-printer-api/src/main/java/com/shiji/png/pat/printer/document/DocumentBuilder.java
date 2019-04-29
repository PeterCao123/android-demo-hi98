package com.shiji.png.pat.printer.document;

import com.shiji.png.pat.printer.model.PrintingCheck;
import com.shiji.png.pat.printer.paper.Document;

import java.util.Currency;

/**
 * @author bruce.wu
 * @since 2019/1/30 18:25
 */
public interface DocumentBuilder {

    void build(PrintingCheck check, Document paper, Currency currency);

}
