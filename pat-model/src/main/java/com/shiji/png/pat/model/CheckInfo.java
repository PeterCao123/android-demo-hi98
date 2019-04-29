package com.shiji.png.pat.model;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author bruce.wu
 * @date 2018/9/4
 */
@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckInfo {

    private static final String OPENED = "Opened";

    private static final String CLOSED = "Closed";

    private static final String LOCKED = "Locked";

    private String merId;

    private String rvcId;

    private String tableNo;

    private String checkNo;

    private String seatNo;

    private BigDecimal checkAmt;

    private BigDecimal svcTotalAmt;

    private BigDecimal surchargeAmt;

    private BigDecimal totalAmt;

    private BigDecimal amt;

    private BigDecimal paidTotalAmt;

    private String checkStatus;

    private boolean enableTip;

    private boolean enablePartialPayment;

    public boolean isOpened() {
        return OPENED.equalsIgnoreCase(checkStatus);
    }

}
