package com.shiji.png.pat.model;

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
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigurationInfo {

    private String merId;

    private String rvcId;

    private boolean enableTip;

    private boolean enablePartialPayment;

    private boolean enableLogon;

    private boolean enableSurcharge;

    private String lastUpdateAt;

}
