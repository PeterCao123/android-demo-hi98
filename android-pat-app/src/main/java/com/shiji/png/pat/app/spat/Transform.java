package com.shiji.png.pat.app.spat;

import com.shiji.png.pat.printer.model.PrintingCheck;
import com.shiji.png.pat.model.CheckInfo;
import com.shiji.png.pat.spat.dto.ChecksDTO;
import com.shiji.png.pat.spat.dto.GetConfigurationDTO;
import com.shiji.png.pat.spat.dto.GetPrintCheckDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author bruce.wu
 * @since 2018/9/4
 */
public final class Transform {

    public static List<CheckInfo> getChecks(ChecksDTO.Data data) {
        GetConfigurationDTO.Data config = data.getConfiguration();
        List<CheckInfo> result = new ArrayList<>(data.getCount());
        if (data.getCount() > 0) {
            for (ChecksDTO.Data.Check check : data.getChecks()) {
                result.add(getCheckInfo(check, config));
            }
        }
        return Collections.unmodifiableList(result);
    }

    private static CheckInfo getCheckInfo(ChecksDTO.Data.Check check, GetConfigurationDTO.Data config) {
        return CheckInfo.builder()
                .merId(check.getMerId())
                .rvcId(check.getRvcId())
                .tableNo(check.getTableNo())
                .checkNo(check.getCheckNo())
                .seatNo(check.getSeatNo())
                .totalAmt(check.getTotalAmt())
                .checkAmt(check.getCheckAmt())
                .svcTotalAmt(check.getSvcTotalAmt())
                .surchargeAmt(check.getSurchargeAmt())
                .amt(check.getAmt())
                .paidTotalAmt(check.getPaidTotalAmt())
                .checkStatus(check.getCheckStatus())
                .enablePartialPayment(config != null && config.isEnablePartialPayment())
                .enableTip(config == null || config.isEnableTip())
                .build();
    }

    private static PrintingCheck.CheckDetail getCheckDetail(GetPrintCheckDTO.Data data) {
        if (data.getCount() != 1) {
            return null;
        }
        ChecksDTO.Data.Check check = data.getChecks().get(0);

        GetPrintCheckDTO.Data.RvcInfo rvcInfo = data.getRvcInfo();
        PrintingCheck.PrintingRvc.PrintingRvcBuilder printingRvcBuilder = PrintingCheck.PrintingRvc.builder();
        if (rvcInfo != null) {
            printingRvcBuilder.posMerId(rvcInfo.getPosMerId())
                    .posMerName(rvcInfo.getPosMerName())
                    .posRvcId(rvcInfo.getPosRvcId())
                    .posRvcName(rvcInfo.getPosRvcName())
                    .info(rvcInfo.getInfo())
                    .address1(rvcInfo.getAddress1())
                    .phone(rvcInfo.getPhone())
                    .fax(rvcInfo.getFax())
                    .website(rvcInfo.getWebsite());
        }
        PrintingCheck.PrintingRvc printingRvc = printingRvcBuilder.build();

        List<PrintingCheck.PrintingItem> printingItems = new ArrayList<>();
        if (check.getItems() != null) {
            for (ChecksDTO.Data.Check.Item item : check.getItems()) {
                PrintingCheck.PrintingItem printingItem = PrintingCheck.PrintingItem.builder()
                        .name(item.getName())
                        .qty(item.getQty())
                        .price(item.getUnitPrice())
                        .amount(item.getTotal())
                        .build();
                printingItems.add(printingItem);
            }
        }

        List<PrintingCheck.PrintingTax> printingTaxes = new ArrayList<>();
        if (check.getTaxItems() != null) {
            for (ChecksDTO.Data.Check.Tax tax : check.getTaxItems()) {
                PrintingCheck.PrintingTax printingTax = PrintingCheck.PrintingTax.builder()
                        .name(tax.getName())
                        .amount(tax.getAmount())
                        .build();
                printingTaxes.add(printingTax);
            }
        }

        List<PrintingCheck.PrintingPayment> printingPayments = new ArrayList<>();
        if (check.getPayments() != null) {
            for (ChecksDTO.Data.Check.Payment payment : check.getPayments()) {
                PrintingCheck.PrintingPayment printingPayment = PrintingCheck.PrintingPayment.builder()
                        .name(payment.getName())
                        .totalAmt(payment.getTotalAmt())
                        .txnAmt(payment.getTxnAmt())
                        .tipAmt(payment.getTipAmt())
                        .surchargeAmt(payment.getSurchargeAmt())
                        .build();
                printingPayments.add(printingPayment);
            }
        }

        return PrintingCheck.CheckDetail.builder()
                .tableNo(check.getTableNo())
                .checkNo(check.getCheckNo())
                .checkOpenEmpId(check.getCheckOpenEmpId())
                .checkOpenTime(check.getCheckOpenTime())
                .checkCloseTime(check.getCheckCloseTime())
                .checkOpenStationCode(check.getCheckOpenStationCode())
                .guests(check.getGuests())
                .totalAmt(check.getTotalAmt())
                .svcTotalAmt(check.getSvcTotalAmt())
                .surchargeAmt(check.getSurchargeAmt())
                .amt(check.getAmt())
                .paidTotalAmt(check.getPaidTotalAmt())
                .rvcInfo(printingRvc)
                .items(printingItems)
                .taxes(printingTaxes)
                .payments(printingPayments)
                .build();
    }

    public static PrintingCheck getPrintingCheck(GetPrintCheckDTO.Data data) {
        PrintingCheck.PrintingCheckBuilder builder = PrintingCheck.builder();
        if (data.getReceiptContent() != null) {
            builder.receiptContent(data.getReceiptContent());
        }
        if (data.getPrintLines() != null) {
            builder.printLines(data.getPrintLines());
        }
        if (data.getCount() == 1) {
            builder.detail(getCheckDetail(data));
        }
        return builder.build();
    }

}
