package org.daijie.jdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在声明mapper类上配置指定数据源
 * @author daijie_jay
 * @since 2017年11月20日
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SelectDataSource {
	
	String name();
}
