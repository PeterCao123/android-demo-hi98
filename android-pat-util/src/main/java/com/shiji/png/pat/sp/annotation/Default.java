package com.shiji.png.pat.sp.annotation;

import android.view.View;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * preference default value
 *
 * @author bruce.wu
 * @date 2018/8/1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Default {

    String value() default "";

    /**
     * default value from string resource
     *
     * @return R.string.xxx
     */
    int resId() default View.NO_ID;

}
