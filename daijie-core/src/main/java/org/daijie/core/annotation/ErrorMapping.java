package org.daijie.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * 
 * @author daijie_jay
 * 需要结合org.springframework.stereotype.Controller注解一起使用，设置运行时异常时跳转的页面
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ErrorMapping {

	/**
	 * 要跳转页面的路径
	 * @return
	 */
	String path() default "";
}
