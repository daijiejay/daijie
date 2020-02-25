package org.daijie.swagger.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 结合@Controller注解一起使用，当出现异常时默认跳转的页面路径
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
