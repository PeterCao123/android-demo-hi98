package com.shiji.png.pat.spat.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author bruce.wu
 * @date 2018/9/3
 */
@ToString(callSuper = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GetConfigurationDTO extends DataDTO<GetConfigurationDTO.Data> {

    @Getter
    @ToString
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Data {

        /**
         * True if support tip. Default is true
         */
        private boolean enableTip;

        /**
         * True if support partial payment.Default is true
         */
        private boolean enablePartialPayment;

        /**
         * True if support operator logon. Default is true
         */
        private boolean enableLogon;

        /**
         * True if support surcharge. Default is true
         */
        private boolean enableSurcharge;

    }

}
