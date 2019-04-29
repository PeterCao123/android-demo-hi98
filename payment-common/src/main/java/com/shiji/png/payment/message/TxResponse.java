package com.shiji.png.payment.message;

import com.shiji.png.payment.model.DccFlag;
import com.shiji.png.payment.model.EntryMode;
import com.shiji.png.payment.model.IssuerCode;
import com.shiji.png.payment.model.RespCode;
import com.shiji.png.payment.model.RespText;
import com.shiji.png.payment.util.Json;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author bruce.wu
 * @since 2018/11/15 10:39
 */
@Getter
@Builder
public class TxResponse extends Json {

    public static final String DEFAULT_MASKED_PAN = "000000";

    public static final String DEFAULT_AUTH_CODE = "000000";

    public boolean isApprove() {
        return RespCode.Approve.equals(this.respCode);
    }

    @Builder.Default
    private final String respCode = RespCode.Approve;

    @Builder.Default
    private final String respText = RespText.Approve;

    private final String markupRate;

    private final String token;

    private final String tokenBrand;

    private final String cashierId;

    private final String acqRespCode;

    private final String acqRespText;

    private final String acqMerId;

    private final String acqTerId;

    private final String acqTxnTimestamp;

    private final String acqTxnNo;

    private final Double txnAmt;

    private final Double tipAmt;

    private final Double surchargeAmt;

    private final Double cashOutAmt;

    private final Double totalAmt;

    /**
     * @see <a href="https://www.iso.org/iso-4217-currency-codes.html">IOS-4217:2015</a>
     * <p>example: 840</p>
     */
    private final String txnCurrCode;

    /**
     * @see <a href="https://www.iso.org/iso-4217-currency-codes.html">IOS-4217:2015</a>
     * <p>example: USD</p>
     */
    private final String txnCurrText;

    private final String pan;

    @Builder.Default
    private final String maskedPan = DEFAULT_MASKED_PAN;

    private final String expDate;

    private final String cardHolderName;

    private final String cardType;

    /**
     * PNG Entry Mode
     */
    @Builder.Default
    private final EntryMode entryMode = EntryMode.UNKNOWN;

    @Builder.Default
    private final String authCode = DEFAULT_AUTH_CODE;

    private final String issuerName;

    /**
     * PNG Issuer Code
     */
    @Builder.Default
    private final IssuerCode issuerCode = IssuerCode.Other;

    /**
     * PNG DCC Flag
     */
    @Builder.Default
    private final DccFlag dccFlag = DccFlag.DCC_NOT_OFFERED;

    private final String dccAmt;

    private final String dccRate;

    private final String dccMarkup;

    private final String dccCurrCode;

    private final String dccCurrText;

    private final String refNo;

    private final String merRef;

    private final String traceNo;

    private final String invNo;

    private final String batchNo;

    private final List<Settlement> settlements;

    @Builder
    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Settlement extends Json {

        private final String respCode;

        private final String respText;

        @Builder.Default
        private final String channel = "0";

        private final String acqMerId;

        private final String acqTerId;

        @Builder.Default
        private final String batchNo = "000000";

        private final Integer txnNum;

        private final Double txnAmt;

        private final Integer refundNum;

        private final Double refundAmt;

        @Builder.Default
        private final String acqTxnDatetime = "00000000000000";

    }

}
