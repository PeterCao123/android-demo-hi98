package com.shiji.png.pat.printer.usdk;

import android.graphics.Bitmap;

import com.shiji.png.pat.printer.PrinterService;
import com.shiji.png.pat.printer.document.DocumentBuilder;
import com.shiji.png.pat.printer.document.DocumentBuilderFactory;
import com.shiji.png.pat.printer.model.PrintingCheck;
import com.shiji.png.pat.printer.paper.Paper;
import com.shiji.png.pat.printer.paper.PaperFactory;
import com.shiji.png.pat.printer.util.PaintUtils;

import java.util.Currency;

/**
 * @author bruce.wu
 * @since 2019/1/31 9:44
 */
public class USdkPrinterService implements PrinterService {

    @Override
    public Bitmap preview(int receiptFlag, PrintingCheck check, Currency currency) {
        Paper paper = PaperFactory.createDefaultPaper();
        DocumentBuilder builder = DocumentBuilderFactory.create(receiptFlag);
        builder.build(check, paper, currency);
        return PaintUtils.render(paper);
    }

    @Override
    public void print(int receiptFlag, PrintingCheck check, Currency currency) {
        USdkDevice device = USdkDevice.create();
        try {
            device.connect();
            USdkPrinter printer = device.getPrinter();
            DocumentBuilder builder = DocumentBuilderFactory.create(receiptFlag);
            builder.build(check, printer, currency);
            printer.print();
        } finally {
            device.disconnect();
        }
    }

}
