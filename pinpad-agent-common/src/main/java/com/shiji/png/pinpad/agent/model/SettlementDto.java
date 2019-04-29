package com.shiji.png.pinpad.agent.model;

import com.shiji.png.payment.model.RespCode;
import com.shiji.png.payment.model.RespText;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

/**
 * @author bruce.wu
 * @date 2018/10/19
 */
@Builder
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SettlementDto {

    @Builder.Default
    private final String channel = "0";

    @Builder.Default
    private final String respCode = RespCode.Approve;

    @Builder.Default
    private final String respText = RespText.Approve;

    @Builder.Default
    private final String acqMerId = "0";

    @Builder.Default
    private final String acqTerId = "0";

    private final String batchNo;

    private final String txnNum;

    private final String txnAmt;

    private final String refundNum;

    private final String refundAmt;

    private final String acqTxnDatetime;

}
