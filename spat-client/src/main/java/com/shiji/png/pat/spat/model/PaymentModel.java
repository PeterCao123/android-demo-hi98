package com.shiji.png.pat.spat.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author bruce.wu
 * @date 2018/9/3
 */
@Builder
@Getter
@ToString
public class PaymentModel {

    /**
     * The merchant Id, provided by PNG
     */
    private final String merId;

    /**
     * The revenue center Id, provided by PNG
     */
    private final String rvcId;

    /**
     * The terminal/pinpad Id, provided by PNG
     */
    private final String terId;

    /**
     * The merchant Id from bank/acquirer
     */
    private final String acqMerId;

    /**
     * The terminal/pinpad Id from bank/acquirer
     */
    private final String acqTerId;

    /**
     * The tip amount
     */
    private final double tipAmt;

    /**
     * Surcharge amount
     */
    private final double surchargeAmt;

    /**
     * The base amount
     */
    private final double txnAmt;

    /**
     * The total payment amount. totalAmt = txnAmt + tipAmt + surchargeAmt
     */
    private final double totalAmt;

    /**
     * The payment txn no, provided by PNG
     */
    private final String txnNo;

    /**
     * The payment ref no, provided by bank/acquirer
     */
    private final String refNo;

    /**
     * The merchant/pos ref no
     */
    private final String merRef;

    /**
     * The payment datetime,in yyyyMMddHHmmss format
     */
    private final String txnDatetime;

    /**
     * The payment card token.
     */
    private final String token;

    /**
     * The encrypted card no.
     */
    private final String PAN;

    /**
     * The masked payment card no.
     */
    @Builder.Default
    private final String maskedPan = "000000";

    /**
     * The payment card type
     */
    private final String cardType;

    /**
     * The payment card issuer code by PNG
     */
    @Builder.Default
    private final String issuerCode = "Zx";

    /**
     * The payment card issuer name from bank/acquirer
     */
    private final String issuerName;

    /**
     * The payment entry mode, assigned by PNG
     */
    @Builder.Default
    private final String entryMode = "99";

    /**
     * The payment authorization
     */
    private final String authCode;

    /**
     * The payment txn  currency code.
     */
    private final String txnCurrCode;

    /**
     * The payment txn currency text.
     */
    private final String txnCurrText;

    /**
     * Indicate if dcc transaction
     */
    @Builder.Default
    private final String dccFlag = "0";

    /**
     * The payment dcc amount
     */
    private final String dccAmt;

    /**
     * The payment dcc exchange rate
     */
    private final String dccRate;

    /**
     * The payment dcc currency code
     */
    private final String dccCurrCode;

    /**
     * The payment dcc currency text
     */
    private final String dccCurrText;

    /**
     * The payment markup rate
     */
    private final String markup;

    /**
     * The payment response code
     */
    @Builder.Default
    @NonNull
    private final String respCode = "00";

    /**
     * The payment response text
     */
    @Builder.Default
    @NonNull
    private final String respText = "Approve";

    /**
     * Merchant copy receipt, formated and provided by bank/acquirer. Base64 encoded
     */
    private final String merchantCopy;

    /**
     * Customer copy receipt, formated and provided by bank/acquirer. Base64 encoded
     */
    private final String customerCopy;

    /**
     * Indicate transaction source
     */
    @NonNull
    private final String source;

    /**
     * Build.MANUFACTURER
     */
    private final String deviceBrand;

    /**
     * Build.PRODUCT
     */
    private final String deviceModel;

    /**
     * Indicate payment trace number, provided by bank/acquirer
     */
    private final String traceNo;

    /**
     * Indicate payment trace number, provided by bank/acquirer
     */
    private final String invNo;

    /**
     * Indicate payment trace number, provided by bank/acquirer
     */
    private final String batchNo;

    /**
     * Indicate payment trace number, provided by bank/acquirer
     */
    private final String acqRespCode;

    /**
     * Indicate payment trace number, provided by bank/acquirer
     */
    private final String acqTxnNo;

    /**
     * The payment datetime from bank/acquirer.
     */
    private final String acqTxnDatetime;

}
