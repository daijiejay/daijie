package org.daijie.jdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 选择指定数据源
 * jpa数据源使用在repository接口上
 * mybatis数据源使用在mapper接口上
 * @author daijie_jay
 * @since 2017年11月20日
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SelectDataSource {
	
	String name();
}
