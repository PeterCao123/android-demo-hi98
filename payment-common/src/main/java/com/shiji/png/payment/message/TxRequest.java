package com.shiji.png.payment.message;

import com.shiji.png.payment.util.Json;
import com.shiji.png.payment.util.TxId;

import lombok.Builder;
import lombok.Getter;

/**
 * @author bruce.wu
 * @since 2018/11/15 10:39
 */
@Getter
@Builder
public class TxRequest extends Json {

    @Builder.Default
    private final long id = TxId.next();

    /**
     * @see <a href="https://www.iso.org/iso-4217-currency-codes.html">IOS-4217:2015</a>
     */
    private final String txnCurrCode;

    private final Double txnAmt;

    private final Double tipAmt;

    private final Double surchargeAmt;

    private final Double cashOutAmt;

    private final Double totalAmt;

    private final String merRef;

    private final String orgRefNo;

    private final String orgTraceNo;

    private final String orgTxnDatetime;

    private final String orgAuthCode;

    private final String orgAcqTerId;

    private final String extraArgs;

}
