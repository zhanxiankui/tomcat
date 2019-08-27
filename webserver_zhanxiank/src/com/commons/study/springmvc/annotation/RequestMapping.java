package com.commons.study.springmvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *  自定义的注解
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author zhanxiank
 * @createdate 2019年8月26日
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

	String value();
}
