package com.commons.study.springmvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 自定义的一个注解，表示一个类为Bean.value为入参路径。
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author zhanxiank
 * @createdate 2019年8月26日
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {

	String value() default "";
}
