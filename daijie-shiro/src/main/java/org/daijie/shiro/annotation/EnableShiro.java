package org.daijie.shiro.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.daijie.core.annotation.EnableParametersFilter;
import org.daijie.core.kisso.KissoConfigure;
import org.daijie.core.swagger.EnableMySwagger;
import org.daijie.core.util.bean.ApplicationContextHolderBean;
import org.daijie.shiro.configure.ShiroConfigure;
import org.springframework.context.annotation.Import;

/**
 * 基于spring+shiro框架的集成
 * 注解集成了spring boot，在任何被扫描类加上此注册即可使用
 * @author daijie
 * @since 2017年6月22日
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableParametersFilter
@EnableMySwagger
@Import({
	ShiroConfigure.class, 
	ApplicationContextHolderBean.class, 
	KissoConfigure.class
	})
public @interface EnableShiro {
}
