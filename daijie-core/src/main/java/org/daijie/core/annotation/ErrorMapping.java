package org.daijie.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * 需要继承org.daijie.core.controller.WebController配合使用,在自定义controller上加上这个注解
 * @author daijie_jay
 * @since 2018年1月1日
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ErrorMapping {

	/**
	 * 要跳转页面的路径
	 * @return 要跳转页面的路径
	 */
	String path() default "";
}
