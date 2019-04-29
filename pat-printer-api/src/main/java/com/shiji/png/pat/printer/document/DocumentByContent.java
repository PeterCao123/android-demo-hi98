package com.shiji.png.pat.printer.document;

import com.shiji.png.pat.printer.model.PrintingCheck;
import com.shiji.png.pat.printer.paper.Document;
import com.shiji.png.pat.printer.paper.model.DocumentStyle;
import com.shiji.png.pat.printer.util.Base64;

import java.util.Arrays;
import java.util.Currency;

/**
 * @author bruce.wu
 * @since 2019/1/30 18:27
 */
public class DocumentByContent implements DocumentBuilder {

    @Override
    public void build(PrintingCheck check, Document paper, Currency currency) {
        String content = Base64.getDecoder().decodeToString(check.getReceiptContent());
        String[] lines = content.split("\n");
        paper.apply(new DocumentStyle().fontSize(DocumentUtils.determineFontSize(Arrays.asList(lines))));
        for (String line : lines) {
            paper.append(line);
        }
    }
}
