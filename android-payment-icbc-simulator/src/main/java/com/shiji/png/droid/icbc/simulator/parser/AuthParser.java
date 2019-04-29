package com.shiji.png.droid.icbc.simulator.parser;

import android.util.Log;

import com.shiji.png.droid.icbc.simulator.model.Converter;
import com.shiji.png.droid.icbc.simulator.model.TransResponseData;
import com.shiji.png.droid.icbc.simulator.util.StringUtils;
import com.shiji.png.payment.message.TxRequest;
import com.shiji.png.payment.message.TxResponse;
import com.shiji.png.payment.model.IssuerCode;

/**
 * @author bruce.wu
 * @since 2018/11/22 16:03
 */
public class AuthParser extends AbstractParser {

    @Override
    void fill(TxResponse.TxResponseBuilder builder, TxRequest request, TransResponseData data) {
        IssuerCode issuerCode = Converter.getIssuerCode(data.getCardOrg());
        Log.e("refNo",data.getRefNo());
        builder.totalAmt(request.getTotalAmt())
                .txnAmt(request.getTxnAmt())
                .surchargeAmt(request.getSurchargeAmt())
                .tipAmt(Double.parseDouble(StringUtils.toDisplayAmount(""+request.getTipAmt(),2)))
                .cashOutAmt(request.getCashOutAmt())
                .acqTxnTimestamp(data.getTransTime())
                .maskedPan(data.getShildedPan())
                .issuerCode(issuerCode)
                .cardType(Converter.getCardType(issuerCode))
                .issuerName(data.getCardIssuer())
                .entryMode(Converter.getEntryMode(data.getInputMode()))
                .txnCurrText(Converter.getCurrencyText(request.getTxnCurrCode()))
                .txnCurrCode(Converter.getCurrencyCode(request.getTxnCurrCode()))
                .traceNo(data.getTraceNo())
                .batchNo(data.getBatchNum())
                .merRef(data.getExtOrderNo())
                .refNo(data.getRefNo())
                .pan(data.getPan())
                .expDate(data.getExpDate())
                .authCode(data.getAuthId());

        fillDcc(builder, data);
    }
}
