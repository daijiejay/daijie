package org.daijie.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.daijie.core.annotation.ParametersFilter;
import org.daijie.core.util.bean.ApplicationContextHolderBean;
import org.daijie.shiro.annotation.ShiroAutoConfiguration;
import org.daijie.shiro.session.bean.ShiroRedisSessionBean;
import org.springframework.context.annotation.Import;

/**
 * 基于spring+mybatis+shiro框架的集成
 * 注解集成了spring boot，在任何被扫描类加上此注册即可使用
 * @author daijie
 * @date 2017年6月22日
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ParametersFilter
@Import({ShiroRedisSessionBean.class, ApplicationContextHolderBean.class})
public @interface SMSConfigurtion {

	Class<?> shiroAnnotationClass() default ShiroAutoConfiguration.class;
}
