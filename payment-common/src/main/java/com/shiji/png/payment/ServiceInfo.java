package com.shiji.png.payment;

import com.shiji.png.payment.annotation.ServiceType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author bruce.wu
 * @since 2018/11/15 10:35
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ServiceInfo {

    private final String name;

    private final String displayText;

    private final ServiceType type;

    private final Class<? extends ServiceFactory> factory;

}
