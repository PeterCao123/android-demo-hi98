package com.shiji.png.pat.sp.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * field type mapper
 *
 * @author bruce.wu
 * @date 2018/8/1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface FieldMapper {

    Class<?> type();

}
