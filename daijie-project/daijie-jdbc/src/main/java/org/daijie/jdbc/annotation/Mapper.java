package org.daijie.jdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类注解，声明此类为mapper类
 * @author daijie_jay
 * @since 2019年02月22日
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapper {

	/**
	 * 声明类名称
	 * @return 类名称
	 */
	String name() default "";
}
