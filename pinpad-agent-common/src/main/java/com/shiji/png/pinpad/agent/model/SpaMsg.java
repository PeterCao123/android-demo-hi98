package com.shiji.png.pinpad.agent.model;

import com.shiji.png.payment.util.Json;

import lombok.Builder;
import lombok.Getter;

/**
 * @author bruce.wu
 * @since 2018/11/27 11:49
 */
@Getter
@Builder
public class SpaMsg extends Json {

    private String txnType;

    private String mName;

    private String mVersion;

    private String type;

    private String merId;

    private String terId;

    private String respCode;

    private String respText;

    private String encryptedKek;

    private String checkValueKek;

    private String encryptedDek;

    private String checkValueDek;

}
