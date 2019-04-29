package com.shiji.png.pinpad.agent.model;

import com.shiji.png.payment.util.Json;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bruce.wu
 * @date 2018/10/22
 */
@Getter
@Setter
public class TransactionDto extends Json implements Cloneable {

    private String type;

    private String mName;

    private String mVersion;

    private String specVer;

    private String encryptedDek;

    private String checkValue;

    private String checkSum;

    private String merId;

    private String terId;

    private String txnType;

    private String merRef;

    private String txnNo;

    private String respCode;

    private String respText;

    //////////////////////////////////

    private String guestNo;

    private String wsNo;

    private String chainId;

    private String propertyId;

    private String echoData;

    private String cashierId;

    private String cvv;

    private String channel;

    private String paymentMethod;

    private String surchargeFlag;

    private String txnAmt;

    private String tipAmt;

    private String surchargeAmt;

    private String cashOutAmt;

    private String totalAmt;

    private String grandTotalAmt;

    private String txnCurrCode;

    private String txnCurrText;

    private String pan;

    private String expDate;

    private String maskedPan;

    private String cardType;

    private String cardholderName;

    private String entryMode;

    private String authCode;

    private String issuerCode;

    private String batchNo;

    private String traceNo;

    private String invNo;

    private String refNo;

    private String token;

    private String dccFlag;

    private String dccAmt;

    private String dccTipAmt;

    private String dccRate;

    private String dccCurrCode;

    private String dccCurrText;

    private String markup;

    private String orderNo;

    private String emvFlag;

    private String emvData;

    private String cleanFlag;

    private String queryPinpadFlag;

    private String transactions;

    private String orgTxnNo;

    private String orgTxnType;

    private String orgRefNo;

    private String orgMerRef;

    private String orgInvNo;

    private String orgTraceNo;

    private String orgAuthCode;

    private String orgOrderNo;

    private String orgAcqTerId;

    private String orgBatchNo;

    private String orgTxnDatetime;

    private String acqRespCode;

    private String acqRespText;

    private String acqChannel;

    private String acqMerId;

    private String acqTerId;

    private String acqTxnDatetime;

    private String acqTxnName;

    private String acqTxnNo;

    private String acqTxnType;

    private String acqPrintData;

    private String signFreeFlag;

    private String signFreeComments;

    private String eSignatureData;

    private String customerCopy;

    private String merchantCopy;

    private String seqNoIcCard;

    private String traceNoIntlCard;

    private String balanceEcash;

    private String termSeqNo;

    private String source;

    private String clientIp;

    private MiscParamDto miscParam;

    private String workflowIndicator;

    private String terminalNum;

    private List<SettlementDto> settData;

    private String deviceBrand;

    private String deviceModel;

    public double getTxnAmtAsDouble() {
        return isEmpty(txnAmt) ? 0D : Double.valueOf(txnAmt);
    }

    public double getTipAmtAsDouble() {
        return isEmpty(tipAmt) ? 0D : Double.valueOf(tipAmt);
    }

    public double getTotalAmtAsDouble() {
        return isEmpty(totalAmt) ? 0D : Double.valueOf(totalAmt);
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

}
