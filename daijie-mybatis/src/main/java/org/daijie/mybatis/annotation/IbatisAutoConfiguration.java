package org.daijie.mybatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.daijie.mybatis.configure.IbatisConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

/**
 * 
 * @author daijie
 * @date 2017年6月5日
 * 通过IbatisAutoConfiguration注解加载ibatis配置，此注解继承了org.mybatis.spring.annotation.MapperScan
 * 
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@MapperScan
@Import(IbatisConfigure.class)
public @interface IbatisAutoConfiguration {

	@AliasFor(annotation = MapperScan.class, attribute = "basePackages")
	String[] basePackages() default {};
}
