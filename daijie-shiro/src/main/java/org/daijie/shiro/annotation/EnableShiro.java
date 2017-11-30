package org.daijie.shiro.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.daijie.core.kisso.KissoConfigure;
import org.daijie.core.util.bean.ApplicationContextHolderBean;
import org.daijie.shiro.configure.ShiroConfigure;
import org.daijie.shiro.session.bean.ShiroRedisSessionBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 基于spring+shiro框架的集成
 * 注解集成了spring boot，在任何被扫描类加上此注册即可使用
 * 适用于单机版redis和集群版redis，区分在ShiroConfigure和ClusterShiroConfigure装置类
 * @author daijie
 * @date 2017年6月22日
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableSwagger2
@Import({ShiroRedisSessionBean.class, ApplicationContextHolderBean.class, KissoConfigure.class})
public @interface EnableShiro {

	@AliasFor(annotation = Import.class, attribute = "value")
	Class<?> value() default ShiroConfigure.class;
}
