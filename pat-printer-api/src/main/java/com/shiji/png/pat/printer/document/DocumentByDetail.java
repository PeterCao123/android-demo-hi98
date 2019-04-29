package com.shiji.png.pat.printer.document;

import android.content.res.Resources;
import android.text.TextUtils;

import com.shiji.png.pat.printer.PrinterContext;
import com.shiji.png.pat.printer.R;
import com.shiji.png.pat.printer.model.PrintingCheck;
import com.shiji.png.pat.printer.paper.Document;
import com.shiji.png.pat.printer.paper.model.DocumentStyle;
import com.shiji.png.pat.printer.paper.model.SplitMode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

/**
 * @author bruce.wu
 * @since 2019/1/30 18:26
 */
public class DocumentByDetail implements DocumentBuilder {

    private final Resources resources;

    DocumentByDetail() {
        this.resources = PrinterContext.app().getResources();
    }

    @Override
    public void build(PrintingCheck check, Document paper, Currency currency) {
        buildPaper(check.getDetail(), paper, currency);
    }


    private void buildPaper(PrintingCheck.CheckDetail check, Document paper, Currency currency) {
        buildTitle(check, paper);

        buildHeaders(check, paper);

        buildItems(check, paper, currency);

        buildPayments(check, paper);

        buildFooter(check, paper);

        buildSeparator(paper);
    }

    private void buildTitle(PrintingCheck.CheckDetail check, Document paper) {
        PrintingCheck.PrintingRvc rvc = check.getRvcInfo();

        paper.apply(new DocumentStyle().fontStyleBold().alignCenter());
        if (!TextUtils.isEmpty(rvc.getPosMerName())) {
            paper.append(rvc.getPosMerName());
        }
        paper.append(getString(R.string.print_text_abn, rvc.getFax()));
        paper.append(getString(R.string.print_text_tax_invoice));

        paper.appendEmptyLine();
    }

    private void buildHeaders(PrintingCheck.CheckDetail check, Document paper) {
        PrintingCheck.PrintingRvc rvc = check.getRvcInfo();

        paper.apply(DocumentStyle.DEFAULT);

        paper.append(getString(R.string.print_head_template,
                getString(R.string.print_text_outlet),
                rvc.getPosRvcName()));
        paper.append(getString(R.string.print_head_template,
                getString(R.string.print_text_table),
                check.getTableNo() + "/" + check.getCheckNo()));
        paper.append(getString(R.string.print_head_template,
                getString(R.string.print_text_employee),
                check.getCheckOpenEmpId()));
        paper.append(getString(R.string.print_head_template,
                getString(R.string.print_text_open_time),
                check.getCheckOpenTime()));
        paper.append(getString(R.string.print_head_template,
                getString(R.string.print_text_close_time),
                check.getCheckCloseTime() == null ? "" : check.getCheckCloseTime()));
        paper.append(getString(R.string.print_head_template,
                getString(R.string.print_text_pos_id),
                check.getCheckOpenStationCode()));
        paper.append(getString(R.string.print_head_template,
                getString(R.string.print_text_guests),
                check.getGuests() > 0 ? Integer.toString(check.getGuests()) : ""));

        paper.appendEmptyLine();
    }

    private void buildItems(PrintingCheck.CheckDetail check, Document paper, Currency currency) {
        String header = getString(R.string.print_item_template,
                getString(R.string.print_text_name),
                getString(R.string.print_text_qty),
                getString(R.string.print_text_price),
                getString(R.string.print_text_amount));
        paper.apply(new DocumentStyle().underline().fontStyleBold()).append(header);

        paper.apply(DocumentStyle.DEFAULT);
        for (PrintingCheck.PrintingItem item : check.getItems()) {
            paper.append(item.getName());
            String text = getString(R.string.print_item_template,
                    "",
                    toPrintQuantity(item.getQty()),
                    toPrintAmount(item.getPrice()),
                    toPrintAmount(item.getAmount()));
            paper.append(text);
        }
        paper.apply(new DocumentStyle().underline()).append(getString(R.string.print_summary_template, "", ""));

        paper.apply(DocumentStyle.DEFAULT);
        String serviceCharge = getString(R.string.print_summary_template,
                getString(R.string.print_text_service_charge_total),
                toPrintAmount(check.getSvcTotalAmt()));
        paper.append(serviceCharge);

        for (PrintingCheck.PrintingTax tax : check.getTaxes()) {
            String taxAmount = getString(R.string.print_summary_template,
                    tax.getName(), toPrintAmount(tax.getAmount()));
            paper.append(taxAmount);
        }

        paper.split(SplitMode.Dashed);

        String summary = getString(R.string.print_summary_template,
                getString(R.string.print_text_total),
                currency.getSymbol() + " " + toPrintAmount(check.getTotalAmt()));
        paper.apply(new DocumentStyle().fontStyleBold()).append(summary);

        paper.appendEmptyLine();
    }

    private void buildPayments(PrintingCheck.CheckDetail check, Document paper) {
        List<PrintingCheck.PrintingPayment> payments = check.getPayments();
        if (payments == null || payments.isEmpty()) {
            return;
        }

        paper.apply(DocumentStyle.DEFAULT);

        for (PrintingCheck.PrintingPayment payment : payments) {
            paper.split(SplitMode.Dashed);
            paper.append(getString(R.string.print_payment_template,
                    getString(R.string.print_text_payment_surround),
                    payment.getName()));
            paper.append(getString(R.string.print_summary_template,
                    getString(R.string.print_text_amount),
                    toPrintAmount(payment.getTxnAmt())));
            paper.append(getString(R.string.print_summary_template,
                    getString(R.string.print_text_tips),
                    toPrintAmount(payment.getTipAmt())));
            paper.append(getString(R.string.print_summary_template,
                    getString(R.string.print_text_surcharge),
                    toPrintAmount(payment.getSurchargeAmt())));
            paper.append(getString(R.string.print_summary_template,
                    getString(R.string.print_text_total),
                    toPrintAmount(payment.getTotalAmt())));
        }

        paper.split(SplitMode.Dashed);

        paper.append(getString(R.string.print_summary_template,
                getString(R.string.print_text_subtotal),
                toPrintAmount(check.getTotalAmt())));
        paper.append(getString(R.string.print_summary_template,
                getString(R.string.print_text_payment),
                toPrintAmount(check.getPaidTotalAmt())));
        paper.append(getString(R.string.print_summary_template,
                getString(R.string.print_text_amount_due),
                toPrintAmount(check.getAmt())));

        paper.appendEmptyLine();
    }

    private void buildFooter(PrintingCheck.CheckDetail check, Document paper) {
        PrintingCheck.PrintingRvc rvc = check.getRvcInfo();

        paper.apply(new DocumentStyle().alignCenter());

        if(!TextUtils.isEmpty(rvc.getAddress1())) {
            //String[] addresses = rvc.getAddress1().split("[\r][\n]");
            String address1 = rvc.getAddress1().replace("\r\n", " ");
            String[] addresses = splitLine(address1);
            for (String address : addresses) {
                paper.append(address);
            }
        }
        if(!TextUtils.isEmpty(rvc.getPhone())) {
            paper.append(getString(R.string.print_phone, rvc.getPhone()));
        }
        if(!TextUtils.isEmpty(rvc.getWebsite())) {
            paper.append(rvc.getWebsite());
        }

        paper.appendEmptyLine();
    }

    private void buildSeparator(Document paper) {
        paper.split(SplitMode.Dashed);
    }

    private String getString(int resId, Object... formatArgs) {
        return resources.getString(resId, formatArgs);
    }

    private String getString(int id) {
        return resources.getString(id);
    }

    private String[] splitLine(String text) {
        List<String> result = new ArrayList<>();
        int offset = 0;
        int length = text.length();
        while (offset < length) {
            int end = (length - offset) > 32 ? 32 : (length - offset);
            result.add(text.substring(offset, offset + end));
            offset += 32;
        }
        return result.toArray(new String[0]);
    }

    private static String toPrintAmount(BigDecimal amount) {
        if (amount == null) {
            return BigDecimal.ZERO.toString();
        }
        return amount.setScale(2, RoundingMode.HALF_EVEN).toString();
    }

    private static String toPrintQuantity(Double qty) {
        return  qty == null ? "" : Long.toString(qty.longValue());
    }

}
