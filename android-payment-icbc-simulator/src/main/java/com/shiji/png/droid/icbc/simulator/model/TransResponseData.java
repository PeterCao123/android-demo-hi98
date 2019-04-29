package com.shiji.png.droid.icbc.simulator.model;

import android.os.Bundle;

import com.shiji.png.payment.model.RespCode;
import com.shiji.png.payment.util.Json;

import java.util.List;

import lombok.Getter;

/**
 * @author bruce.wu
 * @since 2018/11/23 9:10
 */

@Getter
public class TransResponseData extends Json {

    private static final long RESULT_SUCCESS = 0;

    private static final long RESULT_FAILURE = 1;

    public boolean isApproved() {
        return RESULT_SUCCESS == result;
    }

    public String getAcqRespCode() {
        return result == RESULT_SUCCESS ? RespCode.Approve : RespCode.Approve;
    }

    public String getAcqRespText() {
        return this.description;
    }

    private long sn;

    private final Long result;

    private final String description;

    private final String transType;

    private final String transTime;

    private final String type;

    private final String custName;

    private final String custNo;

    private final String termNo;

    private final Long transSequence;

    private final String extOrderNo;

    private final String payType;

    private final Long amount;

    private final Long retAmt;

    private final Long feeAmt;

    private final String batchNum;

    private final String refNo;

    private final String traceNo;

    private final String authId;

    private final String qrCodeOrder;

    private final Long qrDissCount;

    private final Long qrVoucher;

    private final Long integral;

    private final Long integralAmount;

    private final Long insTimes;

    private final String insCurType;

    private final Long insFirstPay;

    private final Long insEveryPay;

    private final Long insFeeType;

    private final Long insOnceFee;

    private final Long insFirstFee;

    private final Long insEveryFee;

    private final Long inputMode;

    private final Long idType;

    private final String shildedPan;

    private final String pan;

    private final String expDate;

    private final String cardSn;

    private final String cardName;

    private final String cardIssuer;

    private final String cardOrg;

    private final String tc;

    private final String upiCustNo;

    private final String foreignCustName;

    private final String foreignCustNo;

    private final String foreignTraceNum;

    private final String dccCurType;

    private final Long dccAmt;

    private final Long dccExRate;

    private final Long dccMarkup;

    private final Long bankReduceAmt;

    private final Long custReduceAmt;

    private final String custAwardInfo;

    private final String custAwardLevel;

    private final String awardPrjName;

    private final String awardPrjId;

    private final String awardNo;

    private final String awardName;

    private final Boolean eSignature;

    private final Boolean reqSignature;

    private  Integer txnState;

    public TransResponseData(TransResponse response) {
        Long inputMode1;
        this.sn = response.getSn();

        Bundle baseResult = response.getBaseResult();
        this.result = getLong(baseResult, "RESULT");
        this.description = getString(baseResult, "DESCRIPTION");
        this.transType = getString(baseResult, "TRANS_TYPE");
        this.transTime = getString(baseResult, "TRANS_TIME");
        this.custName = getString(baseResult, "CUST_NAME");
        this.custNo = getString(baseResult, "CUST_NO");
        this.termNo = getString(baseResult, "TERM_NO");
        this.transSequence = getLong(baseResult, "TRANS_SEQUENCE");
        this.extOrderNo = getString(baseResult, "EXT_ORDER_NO");

        Bundle transResult = response.getTransResult();
        this.payType = getString(transResult, "PAY_TYPE");
        this.amount = getLong(transResult, "AMOUNT");
        this.retAmt = getLong(transResult, "RET_AMT");
        this.feeAmt = getLong(transResult, "FEE_AMT");
        this.batchNum = getString(transResult, "BATCH_NUM");
        this.refNo = getString(transResult, "REF_NO");
        this.traceNo = getString(transResult, "TRACE_NO");
        this.authId = getString(transResult, "AUTH_ID");
        this.qrCodeOrder = getString(transResult, "QRCODE_ORDER");
        this.qrDissCount = getLong(transResult, "QR_DISSCOUNT");
        this.qrVoucher = getLong(transResult, "QR_VOUCHER");
        this.integral = getLong(transResult, "INTEGRAL");
        this.integralAmount = getLong(transResult, "INTEGRAL_AMOUNT");
        this.insTimes = getLong(transResult, "INS_TIMES");
        this.insCurType = getString(transResult, "INS_CUR_TYPE");
        this.insFirstPay = getLong(transResult, "INS_FIRST_PAY");
        this.insEveryPay = getLong(transResult, "INS_EVERY_PAY");
        this.insFeeType = getLong(transResult, "INS_FEE_TYPE");
        this.insOnceFee = getLong(transResult, "INS_ONCE_FEE");
        this.insFirstFee = getLong(transResult, "INS_FIRST_FEE");
        this.insEveryFee = getLong(transResult, "INS_EVERY_FEE");

        Bundle extraInfo = response.getExtraInfo();
        inputMode1 = getLong(transResult, "INPUT_MODE");
        if(inputMode1 == 0L){
            inputMode1 = Long.parseLong("1");
        }
        this.inputMode = inputMode1;
        this.idType = getLong(extraInfo, "ID_TYPE");
        this.shildedPan = getString(extraInfo, "SHILDED_PAN");
        this.pan = getString(extraInfo, "PAN");
        this.expDate = getString(extraInfo, "EXP_DATE");
        this.cardSn = getString(extraInfo, "CARD_SN");
        this.cardName = getString(extraInfo, "CARD_NAME");
        this.cardIssuer = getString(extraInfo, "CARD_ISSUER");
        this.cardOrg = getString(extraInfo, "CARD_ORG");
        this.tc = getString(extraInfo, "TC");
        this.upiCustNo = getString(extraInfo, "UPI_CUST_NO");
        this.foreignCustName = getString(extraInfo, "FOREIGN_CUST_NAME");
        this.foreignCustNo = getString(extraInfo, "FOREIGN_CUST_NO");
        this.foreignTraceNum = getString(extraInfo, "FOREIGN_TRACENUM");
        this.dccCurType = getString(extraInfo, "DCC_CUR_TYPE");
        this.dccAmt = 0L;//getLong(extraInfo, "DCC_AMT");
        this.dccExRate = getLong(extraInfo, "DCC_EX_RATE");
        this.dccMarkup = getLong(extraInfo, "DCC_MARK_UP");
        this.bankReduceAmt = getLong(extraInfo, "BANK_REDUCE_AMT");
        this.custReduceAmt = getLong(extraInfo, "CUST_REDUCE_AMT");
        this.custAwardInfo = getString(extraInfo, "CUST_AWARD_INFO");
        this.custAwardLevel = getString(extraInfo, "CUST_AWARD_LEVEL");
        this.awardPrjName = getString(extraInfo, "AWARD_PRJ_NAME");
        this.awardPrjId = getString(extraInfo, "AWARD_PRJ_ID");
        this.awardNo = getString(extraInfo, "AWARD_NO");
        this.awardName = getString(extraInfo, "AWARD_NAME");
        this.eSignature = getBoolean(extraInfo, "E_SIGNATURE");
        this.reqSignature = getBoolean(extraInfo, "REQ_SIGNATURE");
        this.type = getString(extraInfo,"type");
    }

    private static String getString(Bundle bundle, String key) {
        return bundle == null ? null : bundle.getString(key);
    }

    private static Long getLong(Bundle bundle, String key) {
        return bundle == null ? null : bundle.getLong(key);
    }

    private static Boolean getBoolean(Bundle bundle, String key) {
        return bundle == null ? null : bundle.getBoolean(key);
    }

    private static List<String> getStringList(Bundle bundle, String key) {
        return bundle == null ? null : bundle.getStringArrayList(key);
    }

    private static Bundle getBundle(Bundle bundle, String key) {
        return bundle == null ? null : bundle.getBundle(key);
    }

    public void setTxnState(Integer txnState) {
        this.txnState = txnState;
    }
    public int getTxnState() {
        return txnState;
    }
}
