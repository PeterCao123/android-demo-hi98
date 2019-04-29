package com.shiji.png.pat.spat.service;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author bruce.wu
 * @date 2018/9/3
 */
@Getter
@Builder
@ToString
public class ApiConfig {

    /**
     * PNG merchant id
     */
    private final String merId;

    /**
     * PNG revenue center id
     */
    private final String rvcId;

    /**
     * PNG terminal id
     */
    private final String terId;

    /**
     * POS merchant id
     */
    private final String posMerId;

    /**
     * POS revenue center id
     */
    private final String posRvcId;

    /**
     * SPAT server address
     */
    private final String gateway;

    /**
     * SPAT user
     */
    private final String user;

    /**
     * SPAT signature
     */
    private final String signature;

    /**
     * SPAT password
     */
    private final String password;

    /**
     * SPAT connect timeout
     */
    private final int connectTimeout;

    /**
     * SPAT read timeout
     */
    private final int readTimeout;

}
