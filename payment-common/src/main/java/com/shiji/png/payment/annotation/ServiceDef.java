package com.shiji.png.payment.annotation;

import com.shiji.png.payment.ServiceFactory;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author bruce.wu
 * @since 2018/11/15 10:35
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ServiceDef {

    String name();

    Class<? extends ServiceResource> resource();

    ServiceType type();

    Class<? extends ServiceFactory> factory();

}
