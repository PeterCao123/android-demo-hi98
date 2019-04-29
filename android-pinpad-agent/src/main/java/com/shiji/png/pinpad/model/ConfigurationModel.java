package com.shiji.png.pinpad.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Falcon.cao
 * @date 2019/2/2
 */
@Getter
@Setter
@ToString
public class ConfigurationModel {
    /**
     * The Ids, provided by PNG
     */
    private String terId;
    private String merId;
    private String merName;

    private String currencyText;
    private String currencyDecimal;

    private String serverUrl;
    private String clientId;
    private String secretKey;
}
