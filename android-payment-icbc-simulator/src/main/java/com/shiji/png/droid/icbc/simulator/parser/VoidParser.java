package com.shiji.png.droid.icbc.simulator.parser;

import com.shiji.png.droid.icbc.simulator.model.Converter;
import com.shiji.png.droid.icbc.simulator.model.TransResponseData;
import com.shiji.png.droid.icbc.simulator.util.AmtUtils;
import com.shiji.png.payment.message.TxRequest;
import com.shiji.png.payment.message.TxResponse;
import com.shiji.png.payment.model.IssuerCode;

/**
 * @author bruce.wu
 * @since 2018/11/26 9:58
 */
public class VoidParser extends AbstractParser {
    @Override
    void fill(TxResponse.TxResponseBuilder builder, TxRequest request, TransResponseData data) {
        IssuerCode issuerCode = Converter.getIssuerCode(data.getCardOrg());
        builder.txnAmt(AmtUtils.c2y(data.getAmount()).doubleValue())
                .tipAmt(AmtUtils.c2y(data.getFeeAmt()).doubleValue())
                .acqTxnTimestamp(data.getTransTime())
                .maskedPan(data.getShildedPan())
                .issuerCode(issuerCode)
                .cardType(issuerCode.getName())
                .issuerName(data.getCardIssuer())
                .entryMode(Converter.getEntryMode(data.getInputMode()))
                .traceNo(data.getTraceNo())
                .batchNo(data.getBatchNum())
                .merRef(data.getExtOrderNo())
                .refNo(data.getRefNo())
                .authCode(data.getAuthId())
                .txnCurrText(request.getTxnCurrCode());

        fillDcc(builder, data);
    }
}
