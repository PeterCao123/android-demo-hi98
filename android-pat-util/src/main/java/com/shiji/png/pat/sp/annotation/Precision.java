package com.shiji.png.pat.sp.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.RoundingMode;

/**
 * assigned double type preference precision
 *
 * @author bruce.wu
 * @date 2018/8/1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Precision {

    int scale() default 2;

    RoundingMode roundingMode() default RoundingMode.HALF_EVEN;

}
