package com.shiji.png.droid.icbc.simulator.parser;

import com.shiji.png.droid.icbc.simulator.model.Converter;
import com.shiji.png.droid.icbc.simulator.model.TransResponseData;
import com.shiji.png.payment.message.TxRequest;
import com.shiji.png.payment.message.TxResponse;
import com.shiji.png.payment.model.DccFlag;
import com.shiji.png.payment.model.RespCode;
import com.shiji.png.payment.model.RespText;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bruce.wu
 * @since 2018/11/22 15:39
 */
public abstract class AbstractParser implements Parser {

    private static final Logger logger = LoggerFactory.getLogger("Parser");

    @Override
    public final TxResponse parse(TxRequest request, TransResponseData data) {
        logger.trace("trans response: {}", data);

        TxResponse.TxResponseBuilder builder = TxResponse.builder();
        builder.acqRespCode(data.getAcqRespCode())
                .txnCurrText(Converter.getCurrencyText(request.getTxnCurrCode()))
                .txnCurrCode(request.getTxnCurrCode())
                .acqRespText(data.getAcqRespText())
                .acqMerId(data.getCustNo())
                .acqTerId(data.getTermNo());

        if (data.isApproved()) {
            builder.respCode(RespCode.Approve)
                    .respText(RespText.Approve);
            fill(builder, request, data);
        } else {
            builder.respCode(RespCode.AppError)
                    .respText(data.getDescription());
        }

        return builder.build();
    }

    void fill(TxResponse.TxResponseBuilder builder, TxRequest request, TransResponseData data) {}

    private static DccFlag getDccFlag(Long dccAmt) {
        return dccAmt == null ? DccFlag.DCC_NOT_OFFERED : DccFlag.DCC_OFFERED;
    }

    private static String getDccRate(Long rate) {
        return rate == null ? null : Double.toString(rate / 10000D);
    }

    private static String getDccMarkup(Long markup) {
        return markup == null ? null : Double.toString(markup / 10000D);
    }

    final void fillDcc(TxResponse.TxResponseBuilder builder, TransResponseData data) {
        builder.dccFlag(DccFlag.DCC_NOT_OFFERED);
    }

}
