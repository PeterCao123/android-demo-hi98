package com.shiji.png.pat.printer.document;

import com.shiji.png.pat.printer.model.PrintingCheck;
import com.shiji.png.pat.printer.paper.Document;
import com.shiji.png.pat.printer.paper.model.DocumentStyle;

import java.util.Currency;
import java.util.List;

/**
 * @author bruce.wu
 * @since 2019/1/30 18:28
 */
public class DocumentByPrintLines implements DocumentBuilder {
    @Override
    public void build(PrintingCheck check, Document paper, Currency currency) {
        List<String> lines = check.getPrintLines();
        paper.apply(new DocumentStyle().fontSize(DocumentUtils.determineFontSize(lines)));
        for (String line : lines) {
            paper.append(line);
        }
    }
}
