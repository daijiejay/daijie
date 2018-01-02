package org.daijie.jdbc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.core.annotation.AliasFor;

/**
 * 启用shardingjdbc配置访问数据库
 * @author daijie_jay
 * @since 2018年1月2日
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableAutoConfiguration(exclude = {MybatisAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})
@MapperScan
public @interface EnableShardingjdbc {

	@AliasFor(annotation = MapperScan.class, attribute = "basePackages")
	String[] basePackages() default {};

}
