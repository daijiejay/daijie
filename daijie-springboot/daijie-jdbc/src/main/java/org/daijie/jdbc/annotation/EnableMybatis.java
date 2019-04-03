package org.daijie.jdbc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.daijie.jdbc.MultipleDataSourceProperties;
import org.daijie.jdbc.mybatis.MybatisMultipleDataSourceConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import tk.mybatis.spring.annotation.MapperScan;

/**
 * 启用mybatis配置访问数据库
 * @author daijie_jay
 * @since 2018年1月2日
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableAutoConfiguration(exclude = {JpaRepositoriesAutoConfiguration.class})
@EnableConfigurationProperties(MultipleDataSourceProperties.class)
@Import(MybatisMultipleDataSourceConfiguration.class)
@Inherited
@MapperScan
public @interface EnableMybatis {

	@AliasFor(annotation = MapperScan.class, attribute = "basePackages")
	String[] basePackages() default {};

}
